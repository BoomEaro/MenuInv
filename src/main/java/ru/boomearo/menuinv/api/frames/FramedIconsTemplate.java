package ru.boomearo.menuinv.api.frames;

import lombok.Getter;
import ru.boomearo.menuinv.api.InventoryLocation;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.frames.iteration.FrameIterationHandler;

import java.util.function.Predicate;

@Getter
public class FramedIconsTemplate extends Frame {

    protected final FramedIconsHandlerFactory iconsFactory;
    protected final FrameIterationHandler iterationHandler;
    protected final Predicate<InventoryPage> cacheHandler;

    public FramedIconsTemplate(String name,
                               InventoryLocation first,
                               InventoryLocation second,
                               FramedIconsHandlerFactory iconsFactory,
                               FrameIterationHandler iterationHandler,
                               Predicate<InventoryPage> cacheHandler) {
        super(name, first, second);
        this.iconsFactory = iconsFactory;
        this.iterationHandler = iterationHandler;
        this.cacheHandler = cacheHandler;
    }

    public FramedIconsTemplate(String name,
                               InventoryLocation first,
                               int width,
                               int height,
                               FramedIconsHandlerFactory iconsFactory,
                               FrameIterationHandler iterationHandler,
                               Predicate<InventoryPage> cacheHandler) {
        super(name, first, width, height);
        this.iconsFactory = iconsFactory;
        this.iterationHandler = iterationHandler;
        this.cacheHandler = cacheHandler;
    }

}
