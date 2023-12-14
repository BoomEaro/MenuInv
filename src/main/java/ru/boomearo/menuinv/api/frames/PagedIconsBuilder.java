package ru.boomearo.menuinv.api.frames;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.bukkit.entity.Player;
import ru.boomearo.menuinv.api.Delayable;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.frames.iteration.DefaultIterationHandlerImpl;
import ru.boomearo.menuinv.api.frames.iteration.FrameIterationHandler;
import ru.boomearo.menuinv.api.DefaultUpdateDelay;
import ru.boomearo.menuinv.api.icon.IconHandler;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PagedIconsBuilder {

    private PagedIconsUpdate pagedIconsUpdate = (inventoryPage, player) -> new ArrayList<>();
    private Delayable<InventoryPage> updateDelay = new DefaultUpdateDelay();
    private FrameIterationHandler frameIterationHandler = DefaultIterationHandlerImpl.DEFAULT;
    private Delayable<InventoryPage> cacheHandler = (page, force) -> 0;

    public PagedIconsBuilder setPagedItemsUpdate(PagedIconsUpdate pagedIconsUpdate) {
        Preconditions.checkArgument(pagedIconsUpdate != null, "pagedItemsUpdate is null!");
        this.pagedIconsUpdate = pagedIconsUpdate;
        return this;
    }

    public PagedIconsBuilder setUpdateDelay(Delayable<InventoryPage> updateDelay) {
        Preconditions.checkArgument(updateDelay != null, "updateDelay is null!");
        this.updateDelay = updateDelay;
        return this;
    }

    public PagedIconsBuilder setFrameIterationHandler(FrameIterationHandler frameIterationHandler) {
        Preconditions.checkArgument(frameIterationHandler != null, "frameIterationHandler is null!");
        this.frameIterationHandler = frameIterationHandler;
        return this;
    }

    public PagedIconsBuilder setCacheHandler(Delayable<InventoryPage> cacheHandler) {
        Preconditions.checkArgument(cacheHandler != null, "cacheHandler is null!");
        this.cacheHandler = cacheHandler;
        return this;
    }

    public FramedIconsHandlerFactory build() {
        return () -> new FramedIconsHandler() {

            @Override
            public List<IconHandler> onUpdate(InventoryPage inventoryPage, Player player) {
                return PagedIconsBuilder.this.pagedIconsUpdate.onUpdate(inventoryPage, player);
            }

            @Override
            public long onUpdateTime(InventoryPage inventoryPage, boolean force) {
                return PagedIconsBuilder.this.updateDelay.onUpdateTime(inventoryPage, force);
            }
        };
    }

}
