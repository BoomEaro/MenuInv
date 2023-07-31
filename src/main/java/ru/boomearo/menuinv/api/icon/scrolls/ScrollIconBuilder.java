package ru.boomearo.menuinv.api.icon.scrolls;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.DefaultUpdateDelay;
import ru.boomearo.menuinv.api.Delayable;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.frames.PagedIcons;
import ru.boomearo.menuinv.api.icon.*;

public class ScrollIconBuilder implements ElementBuilderUpdatable<ScrollIconBuilder> {

    private ScrollType scrollType = ScrollType.NEXT;
    private String name = "";

    private ScrollUpdate scrollVisibleUpdate = (inventoryPage, player, scrollType, currentPage, maxPage) -> null;
    private ScrollUpdate scrollHideUpdate = (inventoryPage, player, scrollType, currentPage, maxPage) -> null;

    private IconClick iconClick = (inventoryPage, player, clickType) -> {};
    private IconClickDelay iconClickDelay = new DefaultIconClickDelay();
    private Delayable<InventoryPage> updateDelay = new DefaultUpdateDelay();

    public ScrollIconBuilder setScrollType(ScrollType scrollType) {
        Preconditions.checkArgument(scrollType != null, "scrollType is null!");
        this.scrollType = scrollType;
        return this;
    }

    public ScrollIconBuilder setName(String name) {
        Preconditions.checkArgument(name != null, "name is null!");
        this.name = name;
        return this;
    }

    public ScrollIconBuilder setIconClick(IconClick iconClick) {
        Preconditions.checkArgument(iconClick != null, "iconClick is null!");
        this.iconClick = iconClick;
        return this;
    }

    public ScrollIconBuilder setScrollVisibleUpdate(ScrollUpdate scrollUpdate) {
        Preconditions.checkArgument(scrollUpdate != null, "scrollUpdate is null!");
        this.scrollVisibleUpdate = scrollUpdate;
        return this;
    }

    public ScrollIconBuilder setScrollHideUpdate(ScrollUpdate scrollUpdate) {
        Preconditions.checkArgument(scrollUpdate != null, "scrollUpdate is null!");
        this.scrollHideUpdate = scrollUpdate;
        return this;
    }

    @Override
    public ScrollIconBuilder setUpdateDelay(Delayable<InventoryPage> updateDelay) {
        Preconditions.checkArgument(updateDelay != null, "updateDelay is null!");
        this.updateDelay = updateDelay;
        return this;
    }

    public ScrollIconBuilder setIconClickDelay(IconClickDelay iconClickDelay) {
        Preconditions.checkArgument(iconClickDelay != null, "iconClickDelay is null!");
        this.iconClickDelay = iconClickDelay;
        return this;
    }

    @Override
    public IconHandlerFactory build() {
        return () -> {

            return new IconHandler() {

                @Override
                public void onClick(InventoryPage page, Player player, ClickType clickType) {
                    PagedIcons pagedIcons = page.getListedIconsItems(ScrollIconBuilder.this.name);
                    if (pagedIcons == null) {
                        return;
                    }

                    boolean change = pagedIcons.scrollPage(ScrollIconBuilder.this.scrollType);
                    if (change) {
                        page.setNeedUpdate();
                        ScrollIconBuilder.this.iconClick.onClick(page, player, clickType);
                    }
                }

                @Override
                public ItemStack onUpdate(InventoryPage page, Player player) {
                    PagedIcons pagedIcons = page.getListedIconsItems(ScrollIconBuilder.this.name);
                    if (pagedIcons == null) {
                        return null;
                    }

                    if (ScrollIconBuilder.this.scrollType == ScrollType.NEXT) {
                        if (pagedIcons.getCurrentPage() >= pagedIcons.getMaxPage()) {
                            return ScrollIconBuilder.this.scrollHideUpdate.onUpdate(page, player, ScrollIconBuilder.this.scrollType, pagedIcons.getCurrentPage(), pagedIcons.getMaxPage());
                        } else {
                            return ScrollIconBuilder.this.scrollVisibleUpdate.onUpdate(page, player, ScrollIconBuilder.this.scrollType, pagedIcons.getCurrentPage(), pagedIcons.getMaxPage());
                        }
                    } else if (ScrollIconBuilder.this.scrollType == ScrollType.PREVIOUSLY) {
                        if (pagedIcons.getCurrentPage() <= 1) {
                            return ScrollIconBuilder.this.scrollHideUpdate.onUpdate(page, player, ScrollIconBuilder.this.scrollType, pagedIcons.getCurrentPage(), pagedIcons.getMaxPage());
                        } else {
                            return ScrollIconBuilder.this.scrollVisibleUpdate.onUpdate(page, player, ScrollIconBuilder.this.scrollType, pagedIcons.getCurrentPage(), pagedIcons.getMaxPage());
                        }
                    }
                    return null;
                }

                @Override
                public long getClickTime(InventoryPage page, Player player, ClickType click) {
                    return ScrollIconBuilder.this.iconClickDelay.getClickTime(page, player, click);
                }

                @Override
                public long onUpdateTime(InventoryPage page, boolean force) {
                    return ScrollIconBuilder.this.updateDelay.onUpdateTime(page, force);
                }

            };
        };
    }
}
