package ru.boomearo.menuinv.api.frames;

import lombok.Getter;
import lombok.NonNull;
import ru.boomearo.menuinv.api.Delayable;
import ru.boomearo.menuinv.api.InventoryLocation;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.frames.iteration.FrameIterationHandler;

@Getter
public class FramedIconsTemplate extends Frame {

    protected final FramedIconsHandlerFactory iconsFactory;
    protected final FrameIterationHandler iterationHandler;
    protected final Delayable<InventoryPage> cacheHandler;

    public FramedIconsTemplate(@NonNull String name,
                               @NonNull InventoryLocation first,
                               @NonNull InventoryLocation second,
                               @NonNull FramedIconsHandlerFactory iconsFactory,
                               @NonNull FrameIterationHandler iterationHandler,
                               @NonNull Delayable<InventoryPage> cacheHandler) {
        super(name, first, second);
        this.iconsFactory = iconsFactory;
        this.iterationHandler = iterationHandler;
        this.cacheHandler = cacheHandler;
    }

    public FramedIconsTemplate(@NonNull String name,
                               @NonNull InventoryLocation first,
                               int width,
                               int height,
                               @NonNull FramedIconsHandlerFactory iconsFactory,
                               @NonNull FrameIterationHandler iterationHandler,
                               @NonNull Delayable<InventoryPage> cacheHandler) {
        super(name, first, width, height);
        this.iconsFactory = iconsFactory;
        this.iterationHandler = iterationHandler;
        this.cacheHandler = cacheHandler;
    }

}
