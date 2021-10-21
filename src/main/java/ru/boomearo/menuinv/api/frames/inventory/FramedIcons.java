package ru.boomearo.menuinv.api.frames.inventory;

import ru.boomearo.menuinv.api.FramedIconsHandler;
import ru.boomearo.menuinv.api.frames.Frame;

public class FramedIcons extends Frame {

    private final FramedIconsHandler handler;

    public FramedIcons(String name, int x, int z, int width, int height, FramedIconsHandler handler) {
        super(name, x, z, width, height);
        this.handler = handler;
    }

    public FramedIconsHandler getHandler() {
        return this.handler;
    }
}
