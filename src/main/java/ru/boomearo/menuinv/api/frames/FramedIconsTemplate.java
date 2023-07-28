package ru.boomearo.menuinv.api.frames;

import ru.boomearo.menuinv.api.frames.iteration.FrameIterationHandler;

public class FramedIconsTemplate extends Frame {

    private final FramedIconsHandlerFactory iconsFactory;
    private final FrameIterationHandler iterationHandler;
    private final boolean permanentCached;

    public FramedIconsTemplate(String name,
                               int x,
                               int z,
                               int width,
                               int height,
                               FramedIconsHandlerFactory iconsFactory,
                               FrameIterationHandler iterationHandler,
                               boolean permanentCached) {
        super(name, x, z, width, height);
        this.iconsFactory = iconsFactory;
        this.iterationHandler = iterationHandler;
        this.permanentCached = permanentCached;
    }

    public FramedIconsHandlerFactory getIconsFactory() {
        return this.iconsFactory;
    }

    public FrameIterationHandler getIterationHandler() {
        return this.iterationHandler;
    }

    public boolean isPermanentCached() {
        return this.permanentCached;
    }
}
