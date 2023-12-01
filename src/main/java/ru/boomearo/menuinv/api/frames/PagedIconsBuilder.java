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
import ru.boomearo.menuinv.api.session.InventorySession;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PagedIconsBuilder<SESSION extends InventorySession> {

    private PagedIconsUpdate<SESSION> pagedIconsUpdate = (inventoryPage, player) -> new ArrayList<>();
    private Delayable<InventoryPage<SESSION>> updateDelay = new DefaultUpdateDelay<>();
    private FrameIterationHandler frameIterationHandler = DefaultIterationHandlerImpl.DEFAULT;
    private boolean permanent = false;

    public PagedIconsBuilder<SESSION> setPagedItemsUpdate(PagedIconsUpdate<SESSION> pagedIconsUpdate) {
        Preconditions.checkArgument(pagedIconsUpdate != null, "pagedItemsUpdate is null!");
        this.pagedIconsUpdate = pagedIconsUpdate;
        return this;
    }

    public PagedIconsBuilder<SESSION> setUpdateDelay(Delayable<InventoryPage<SESSION>> updateDelay) {
        Preconditions.checkArgument(updateDelay != null, "updateDelay is null!");
        this.updateDelay = updateDelay;
        return this;
    }

    public PagedIconsBuilder<SESSION> setFrameIterationHandler(FrameIterationHandler frameIterationHandler) {
        Preconditions.checkArgument(frameIterationHandler != null, "frameIterationHandler is null!");
        this.frameIterationHandler = frameIterationHandler;
        return this;
    }

    public PagedIconsBuilder<SESSION> setPermanent(boolean permanent) {
        this.permanent = permanent;
        return this;
    }

    public PagedIconsBuilder<SESSION> permanent() {
        this.permanent = true;
        return this;
    }

    public FramedIconsHandlerFactory<SESSION> build() {
        return () -> new FramedIconsHandler<SESSION>() {

            @Override
            public List<IconHandler<SESSION>> onUpdate(InventoryPage<SESSION> inventoryPage, Player player) {
                return PagedIconsBuilder.this.pagedIconsUpdate.onUpdate(inventoryPage, player);
            }

            @Override
            public long onUpdateTime(InventoryPage<SESSION> inventoryPage, boolean force) {
                return PagedIconsBuilder.this.updateDelay.onUpdateTime(inventoryPage, force);
            }
        };
    }

}
