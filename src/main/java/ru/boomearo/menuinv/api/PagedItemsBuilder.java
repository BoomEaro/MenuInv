package ru.boomearo.menuinv.api;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PagedItemsBuilder {

    private PagedItemsUpdate pagedItemsUpdate = (inventoryPage, player) -> new ArrayList<>();
    private IconUpdateDelay iconUpdateDelay = (inventoryPage) -> 250;

    public PagedItemsBuilder setPagedItemsUpdate(PagedItemsUpdate pagedItemsUpdate) {
        Preconditions.checkArgument(pagedItemsUpdate != null, "pagedItemsUpdate is null!");
        this.pagedItemsUpdate = pagedItemsUpdate;
        return this;
    }

    public PagedItemsBuilder setIconUpdateDelay(IconUpdateDelay iconUpdateDelay) {
        Preconditions.checkArgument(iconUpdateDelay != null, "iconUpdateDelay is null!");
        this.iconUpdateDelay = iconUpdateDelay;
        return this;
    }

    public FramedIconsHandlerFactory build() {
        return new FramedIconsHandlerFactory() {

            @Override
            public FramedIconsHandler create() {
                return new FramedIconsHandler() {

                    @Override
                    public List<IconHandler> onUpdate(InventoryPage inventoryPage, Player player) {
                        return PagedItemsBuilder.this.pagedItemsUpdate.onUpdate(inventoryPage, player);
                    }

                    @Override
                    public long getUpdateTime(InventoryPage inventoryPage) {
                        return PagedItemsBuilder.this.iconUpdateDelay.getUpdateTime(inventoryPage);
                    }

                };
            }
        };
    }

}
