package ru.boomearo.menuinv.api.frames;

import ru.boomearo.menuinv.api.frames.iteration.FrameIterationHandler;

public class FramedIcons extends Frame {

    protected final FramedIconsHandler iconsHandler;
    protected final FrameIterationHandler iterationHandler;
    protected final boolean permanentCached;

    public FramedIcons(String name,
                       int x,
                       int z,
                       int width,
                       int height,
                       FramedIconsHandler iconsHandler,
                       FrameIterationHandler iterationHandler,
                       boolean permanentCached) {
        super(name, x, z, width, height);
        this.iconsHandler = iconsHandler;
        this.iterationHandler = iterationHandler;
        this.permanentCached = permanentCached;
    }

    public FramedIconsHandler getIconsHandler() {
        return this.iconsHandler;
    }

    public FrameIterationHandler getIterationHandler() {
        return this.iterationHandler;
    }

    public boolean isPermanentCached() {
        return this.permanentCached;
    }
}
