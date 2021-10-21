package ru.boomearo.menuinv.api.frames.template;

import ru.boomearo.menuinv.api.FramedIconsHandlerFactory;
import ru.boomearo.menuinv.api.frames.Frame;

public class FramedIconsTemplate extends Frame {

    private final FramedIconsHandlerFactory factory;

    public FramedIconsTemplate(String name, int x, int z, int width, int height, FramedIconsHandlerFactory factory) {
        super(name, x, z, width, height);
        this.factory = factory;
    }

    public FramedIconsHandlerFactory getFactory() {
        return this.factory;
    }
}
