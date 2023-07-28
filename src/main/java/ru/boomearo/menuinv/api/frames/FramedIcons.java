package ru.boomearo.menuinv.api.frames;

import ru.boomearo.menuinv.api.frames.iteration.FrameIterationHandler;

/**
 * Представляет рамку, использующую обработчик рамочных предметов.
 */
public class FramedIcons extends Frame {

    private final FramedIconsHandler iconsHandler;
    private final FrameIterationHandler iterationHandler;
    private final boolean permanentCached;

    public FramedIcons(String name, int x, int z, int width, int height, FramedIconsHandler iconsHandler, FrameIterationHandler iterationHandler, boolean permanentCached) {
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
