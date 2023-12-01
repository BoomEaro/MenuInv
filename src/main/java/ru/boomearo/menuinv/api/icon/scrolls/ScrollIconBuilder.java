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
import ru.boomearo.menuinv.api.icon.DefaultIconClickDelay;
import ru.boomearo.menuinv.api.session.InventorySession;

public class ScrollIconBuilder<SESSION extends InventorySession> implements ElementBuilderUpdatable<ScrollIconBuilder<SESSION>, SESSION> {

    private ScrollType scrollType = ScrollType.NEXT;
    private String name = "";

    private ScrollUpdate<SESSION> scrollVisibleUpdate = (inventoryPage, player, scrollType, currentPage, maxPage) -> null;
    private ScrollUpdate<SESSION> scrollHideUpdate = (inventoryPage, player, scrollType, currentPage, maxPage) -> null;

    private IconClick<SESSION> iconClick = (inventoryPage, player, clickType) -> {};
    private IconClickDelay<SESSION> iconClickDelay = new DefaultIconClickDelay<>();
    private Delayable<InventoryPage<SESSION>> updateDelay = new DefaultUpdateDelay<>();

    public ScrollIconBuilder<SESSION> setScrollType(ScrollType scrollType) {
        Preconditions.checkArgument(scrollType != null, "scrollType is null!");
        this.scrollType = scrollType;
        return this;
    }

    public ScrollIconBuilder<SESSION> setName(String name) {
        Preconditions.checkArgument(name != null, "name is null!");
        this.name = name;
        return this;
    }

    public ScrollIconBuilder<SESSION> setIconClick(IconClick<SESSION> iconClick) {
        Preconditions.checkArgument(iconClick != null, "iconClick is null!");
        this.iconClick = iconClick;
        return this;
    }

    public ScrollIconBuilder<SESSION> setScrollVisibleUpdate(ScrollUpdate<SESSION> scrollUpdate) {
        Preconditions.checkArgument(scrollUpdate != null, "scrollUpdate is null!");
        this.scrollVisibleUpdate = scrollUpdate;
        return this;
    }

    public ScrollIconBuilder<SESSION> setScrollHideUpdate(ScrollUpdate<SESSION> scrollUpdate) {
        Preconditions.checkArgument(scrollUpdate != null, "scrollUpdate is null!");
        this.scrollHideUpdate = scrollUpdate;
        return this;
    }

    @Override
    public ScrollIconBuilder<SESSION> setUpdateDelay(Delayable<InventoryPage<SESSION>> updateDelay) {
        Preconditions.checkArgument(updateDelay != null, "updateDelay is null!");
        this.updateDelay = updateDelay;
        return this;
    }

    public ScrollIconBuilder<SESSION> setIconClickDelay(IconClickDelay<SESSION> iconClickDelay) {
        Preconditions.checkArgument(iconClickDelay != null, "iconClickDelay is null!");
        this.iconClickDelay = iconClickDelay;
        return this;
    }

    @Override
    public IconHandlerFactory<SESSION> build() {
        return () -> new IconHandler<SESSION>() {

            @Override
            public void onClick(InventoryPage<SESSION> page, Player player, ClickType clickType) {
                PagedIcons<SESSION> pagedIcons = page.getListedIconsItems(ScrollIconBuilder.this.name);
                if (pagedIcons == null) {
                    return;
                }

                boolean change = pagedIcons.scrollPage(ScrollIconBuilder.this.scrollType);
                if (change) {
                    page.update(true);
                    ScrollIconBuilder.this.iconClick.onClick(page, player, clickType);
                }
            }

            @Override
            public ItemStack onUpdate(InventoryPage<SESSION> page, Player player) {
                PagedIcons<SESSION> pagedIcons = page.getListedIconsItems(ScrollIconBuilder.this.name);
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
            public long getClickTime(InventoryPage<SESSION> page, Player player, ClickType click) {
                return ScrollIconBuilder.this.iconClickDelay.getClickTime(page, player, click);
            }

            @Override
            public long onUpdateTime(InventoryPage<SESSION> page, boolean force) {
                return ScrollIconBuilder.this.updateDelay.onUpdateTime(page, force);
            }

        };
    }
}
