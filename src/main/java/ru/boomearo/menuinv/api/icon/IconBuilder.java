package ru.boomearo.menuinv.api.icon;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.frames.PagedItems;
import ru.boomearo.menuinv.api.icon.scrolls.*;

import java.util.function.Predicate;

public class IconBuilder {

    private IconClick iconClick = (inventoryPage, player, clickType) -> {};
    private IconClickDelay iconClickDelay = (inventoryPage, player, clickType) -> 250;
    private IconUpdate iconUpdate = (inventoryPage, player) -> null;
    private IconUpdateDelay iconUpdateDelay = (inventoryPage) -> 250;
    private Predicate<InventoryPage> iconUpdateCondition = (inventoryPage) -> true;

    private ScrollIconBuilder scrollIconBuilder = null;

    public IconBuilder setIconClick(IconClick iconClick) {
        Preconditions.checkArgument(iconClick != null, "iconClick is null!");
        this.iconClick = iconClick;
        return this;
    }

    public IconBuilder setIconUpdate(IconUpdate iconUpdate) {
        Preconditions.checkArgument(iconUpdate != null, "iconUpdate is null!");
        this.iconUpdate = iconUpdate;
        return this;
    }

    public IconBuilder setIconUpdateDelay(IconUpdateDelay iconUpdateDelay) {
        Preconditions.checkArgument(iconUpdateDelay != null, "iconUpdateDelay is null!");
        this.iconUpdateDelay = iconUpdateDelay;
        return this;
    }

    public IconBuilder setIconUpdateCondition(Predicate<InventoryPage> iconUpdateCondition) {
        Preconditions.checkArgument(iconUpdateCondition != null, "iconUpdateCondition is null!");
        this.iconUpdateCondition = iconUpdateCondition;
        return this;
    }

    public IconBuilder setIconClickDelay(IconClickDelay iconClickDelay) {
        Preconditions.checkArgument(iconClickDelay != null, "iconClickDelay is null!");
        this.iconClickDelay = iconClickDelay;
        return this;
    }

    public IconBuilder setScrollIconBuilder(ScrollIconBuilder scrollIconBuilder) {
        this.scrollIconBuilder = scrollIconBuilder;

        return this;
    }

    public IconHandlerFactory build() {
        if (this.scrollIconBuilder != null) {
            return new ScrollIconHandlerFactory(this.scrollIconBuilder);
        }

        return () -> {
            return new IconHandler() {

                @Override
                public void onClick(InventoryPage page, Player player, ClickType click) {
                    IconBuilder.this.iconClick.onClick(page, player, click);
                }

                @Override
                public long getClickTime(InventoryPage page, Player player, ClickType click) {
                    return IconBuilder.this.iconClickDelay.getClickTime(page, player, click);
                }

                @Override
                public ItemStack onUpdate(InventoryPage consume, Player player) {
                    return IconBuilder.this.iconUpdate.onUpdate(consume, player);
                }

                @Override
                public long getUpdateTime(InventoryPage page) {
                    return IconBuilder.this.iconUpdateDelay.getUpdateTime(page);
                }

                @Override
                public boolean shouldUpdate(InventoryPage page) {
                    return IconBuilder.this.iconUpdateCondition.test(page);
                }
            };
        };
    }

    private static class ScrollIconHandlerFactory implements IconHandlerFactory {

        private final ScrollType type;
        private final String pagedItems;
        private final ScrollHandlerFactory scrollHandlerFactory;

        public ScrollIconHandlerFactory(ScrollIconBuilder scrollIconBuilder) {
            this.type = scrollIconBuilder.getScrollType();
            this.pagedItems = scrollIconBuilder.getName();
            this.scrollHandlerFactory = scrollIconBuilder.build();
        }

        @Override
        public IconHandler create() {
            ScrollHandler handler = this.scrollHandlerFactory.create(ScrollIconHandlerFactory.this.type);

            return new IconHandler() {

                @Override
                public void onClick(InventoryPage page, Player player, ClickType clickType) {
                    boolean change = page.getListedIconsItems(ScrollIconHandlerFactory.this.pagedItems).scrollPage(ScrollIconHandlerFactory.this.type);
                    if (change) {
                        page.setNeedUpdate();
                        handler.onClick(page, player, clickType);
                    }
                }

                @Override
                public ItemStack onUpdate(InventoryPage page, Player player) {
                    PagedItems lii = page.getListedIconsItems(ScrollIconHandlerFactory.this.pagedItems);

                    if (ScrollIconHandlerFactory.this.type == ScrollType.NEXT) {
                        if (lii.getCurrentPage() >= lii.getMaxPage()) {
                            return handler.onHide(page, player, ScrollIconHandlerFactory.this.type, lii.getCurrentPage(), lii.getMaxPage());
                        } else {
                            return handler.onVisible(page, player, ScrollIconHandlerFactory.this.type, lii.getCurrentPage(), lii.getMaxPage());
                        }
                    } else if (ScrollIconHandlerFactory.this.type == ScrollType.PREVIOUSLY) {
                        if (lii.getCurrentPage() <= 1) {
                            return handler.onHide(page, player, ScrollIconHandlerFactory.this.type, lii.getCurrentPage(), lii.getMaxPage());
                        } else {
                            return handler.onVisible(page, player, ScrollIconHandlerFactory.this.type, lii.getCurrentPage(), lii.getMaxPage());
                        }
                    }
                    return null;
                }

            };
        }
    }

}
