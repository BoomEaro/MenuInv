package ru.boomearo.menuinv.api.frames.inventory;

import ru.boomearo.menuinv.api.FramedIconsHandler;
import ru.boomearo.menuinv.api.frames.Frame;

/**
 * Представляет рамку, использующую обработчик рамочных предметов.
 */
public class FramedIcons extends Frame {

    private final FramedIconsHandler handler;
    private final boolean permanentCached;

    public FramedIcons(String name, int x, int z, int width, int height, FramedIconsHandler handler, boolean permanentCached) {
        super(name, x, z, width, height);
        this.handler = handler;
        this.permanentCached = permanentCached;
    }

    public FramedIconsHandler getHandler() {
        return this.handler;
    }

    public boolean isPermanentCached() {
        return this.permanentCached;
    }
}
