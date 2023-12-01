package ru.boomearo.menuinv.api.frames;

import lombok.Getter;
import ru.boomearo.menuinv.api.InventoryLocation;
import ru.boomearo.menuinv.api.frames.iteration.FrameIterationHandler;
import ru.boomearo.menuinv.api.session.InventorySession;

@Getter
public class FramedIconsTemplate<SESSION extends InventorySession> extends Frame {

    protected final FramedIconsHandlerFactory<SESSION> iconsFactory;
    protected final FrameIterationHandler iterationHandler;
    protected final boolean permanentCached;

    public FramedIconsTemplate(String name,
                               InventoryLocation first,
                               InventoryLocation second,
                               FramedIconsHandlerFactory<SESSION> iconsFactory,
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
                               FramedIconsHandlerFactory<SESSION> iconsFactory,
                               FrameIterationHandler iterationHandler,
                               boolean permanentCached) {
        super(name, first, width, height);
        this.iconsFactory = iconsFactory;
        this.iterationHandler = iterationHandler;
        this.permanentCached = permanentCached;
    }

}
