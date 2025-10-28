package ru.boomearo.menuinv.api.frames;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;
import ru.boomearo.menuinv.api.DefaultUpdateDelay;
import ru.boomearo.menuinv.api.Delayable;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.frames.iteration.DefaultIterationHandlerImpl;
import ru.boomearo.menuinv.api.frames.iteration.FrameIterationHandler;
import ru.boomearo.menuinv.api.icon.IconHandler;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PagedIconsBuilder implements PagedElementBuilderUpdatable<PagedIconsBuilder> {

    private PagedIconsUpdate pagedIconsUpdate = (inventoryPage, player) -> new ArrayList<>();
    private Delayable<InventoryPage> updateDelay = new DefaultUpdateDelay<>();
    private FrameIterationHandler frameIterationHandler = DefaultIterationHandlerImpl.DEFAULT;
    private Delayable<InventoryPage> cacheHandler = (page, force) -> Duration.ZERO;

    @NonNull
    public PagedIconsBuilder setPagedItemsUpdate(@NonNull PagedIconsUpdate pagedIconsUpdate) {
        this.pagedIconsUpdate = pagedIconsUpdate;
        return this;
    }

    @NonNull
    @Override
    public PagedIconsBuilder setUpdateDelay(@NonNull Delayable<InventoryPage> updateDelay) {
        this.updateDelay = updateDelay;
        return this;
    }

    @NonNull
    @Override
    public PagedIconsBuilder setFrameIterationHandler(@NonNull FrameIterationHandler frameIterationHandler) {
        this.frameIterationHandler = frameIterationHandler;
        return this;
    }

    @NonNull
    @Override
    public PagedIconsBuilder setCacheHandler(@NonNull Delayable<InventoryPage> cacheHandler) {
        this.cacheHandler = cacheHandler;
        return this;
    }

    @NonNull
    @Override
    public FramedIconsHandlerFactory build() {
        return () -> new FramedIconsHandler() {

            @Nullable
            @Override
            public List<IconHandler> onUpdate(@NonNull InventoryPage inventoryPage, @NonNull Player player) {
                return PagedIconsBuilder.this.pagedIconsUpdate.onUpdate(inventoryPage, player);
            }

            @Nullable
            @Override
            public Duration onUpdateTime(@NonNull InventoryPage inventoryPage, boolean force) {
                return PagedIconsBuilder.this.updateDelay.onUpdateTime(inventoryPage, force);
            }
        };
    }

}
