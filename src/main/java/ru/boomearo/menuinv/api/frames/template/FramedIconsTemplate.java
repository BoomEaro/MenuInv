package ru.boomearo.menuinv.api.frames.template;

import ru.boomearo.menuinv.api.FramedIconsHandlerFactory;
import ru.boomearo.menuinv.api.frames.Frame;

/**
 * Представляет шаблон рамки, использующий фабрику рамочных предметов
 */
public class FramedIconsTemplate extends Frame {

    private final FramedIconsHandlerFactory factory;
    private final boolean permanentCached;

    public FramedIconsTemplate(String name, int x, int z, int width, int height, FramedIconsHandlerFactory factory, boolean permanentCached) {
        super(name, x, z, width, height);
        this.factory = factory;
        this.permanentCached = permanentCached;
    }

    public FramedIconsHandlerFactory getFactory() {
        return this.factory;
    }

    public boolean isPermanentCached() {
        return this.permanentCached;
    }
}
