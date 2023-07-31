package ru.boomearo.menuinv.api.frames;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import ru.boomearo.menuinv.api.Delayable;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.frames.iteration.DefaultIterationHandler;
import ru.boomearo.menuinv.api.frames.iteration.FrameIterationHandler;
import ru.boomearo.menuinv.api.DefaultUpdateDelay;
import ru.boomearo.menuinv.api.icon.IconHandler;

import java.util.ArrayList;
import java.util.List;

public class PagedIconsBuilder {

    private PagedIconsUpdate pagedIconsUpdate = (inventoryPage, player) -> new ArrayList<>();
    private Delayable<InventoryPage> updateDelay = new DefaultUpdateDelay();
    private FrameIterationHandler frameIterationHandler = new DefaultIterationHandler();
    private boolean permanent = false;

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

    public PagedIconsBuilder setPermanent(boolean permanent) {
        this.permanent = permanent;
        return this;
    }

    public PagedIconsBuilder permanent() {
        this.permanent = true;
        return this;
    }

    public FrameIterationHandler getFrameIterationHandler() {
        return this.frameIterationHandler;
    }

    public boolean isPermanent() {
        return this.permanent;
    }

    public FramedIconsHandlerFactory build() {
        return new FramedIconsHandlerFactory() {

            @Override
            public FramedIconsHandler create() {
                return new FramedIconsHandler() {

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
        };
    }

}
