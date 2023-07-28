package ru.boomearo.menuinv.api.icon.scrolls;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.icon.IconClick;
import ru.boomearo.menuinv.api.InventoryPage;

public class ScrollIconBuilder {

    private IconClick iconClick = (inventoryPage, player, clickType) -> {};
    private ScrollUpdate scrollVisibleUpdate = (inventoryPage, player, scrollType, currentPage, maxPage) -> null;
    private ScrollUpdate scrollHideUpdate = (inventoryPage, player, scrollType, currentPage, maxPage) -> null;

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

    public ScrollHandlerFactory build() {
        return new ScrollHandlerFactory() {

            @Override
            public ScrollHandler create(ScrollType scrollType) {
                return new ScrollHandler() {

                    @Override
                    public void onClick(InventoryPage inventoryPage, Player player, ClickType clickType) {
                        ScrollIconBuilder.this.iconClick.onClick(inventoryPage, player, clickType);
                    }

                    @Override
                    public ItemStack onVisible(InventoryPage inventoryPage, Player player, ScrollType scrollType, int currentPage, int maxPage) {
                        return ScrollIconBuilder.this.scrollVisibleUpdate.onUpdate(inventoryPage, player, scrollType, currentPage, maxPage);
                    }

                    @Override
                    public ItemStack onHide(InventoryPage inventoryPage, Player player, ScrollType scrollType,int currentPage, int maxPage) {
                        return ScrollIconBuilder.this.scrollHideUpdate.onUpdate(inventoryPage, player, scrollType, currentPage, maxPage);
                    }
                };
            }

        };
    }


}
