package ru.boomearo.menuinv.api.frames;

import lombok.Getter;
import ru.boomearo.menuinv.api.InventoryLocation;
import ru.boomearo.menuinv.api.frames.iteration.FrameIterationHandler;

@Getter
public class FramedIconsTemplate extends Frame {

    protected final FramedIconsHandlerFactory iconsFactory;
    protected final FrameIterationHandler iterationHandler;
    protected final boolean permanentCached;

    public FramedIconsTemplate(String name,
                               InventoryLocation first,
                               InventoryLocation second,
                               FramedIconsHandlerFactory iconsFactory,
                               FrameIterationHandler iterationHandler,
                               boolean permanentCached) {
        super(name, first, second);
        this.iconsFactory = iconsFactory;
        this.iterationHandler = iterationHandler;
        this.permanentCached = permanentCached;
    }

    public FramedIconsTemplate(String name,
                               InventoryLocation first,
                               int width,
                               int height,
                               FramedIconsHandlerFactory iconsFactory,
                               FrameIterationHandler iterationHandler,
                               boolean permanentCached) {
        super(name, first, width, height);
        this.iconsFactory = iconsFactory;
        this.iterationHandler = iterationHandler;
        this.permanentCached = permanentCached;
    }

}
