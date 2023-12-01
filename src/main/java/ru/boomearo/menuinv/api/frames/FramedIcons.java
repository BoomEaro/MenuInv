package ru.boomearo.menuinv.api.frames;

import lombok.Getter;
import ru.boomearo.menuinv.api.InventoryLocation;
import ru.boomearo.menuinv.api.frames.iteration.FrameIterationHandler;
import ru.boomearo.menuinv.api.session.InventorySession;

@Getter
public class FramedIcons<SESSION extends InventorySession> extends Frame {

    protected final FramedIconsHandler<SESSION> iconsHandler;
    protected final FrameIterationHandler iterationHandler;
    protected final boolean permanentCached;

    public FramedIcons(String name,
                       InventoryLocation first,
                       InventoryLocation second,
                       FramedIconsHandler<SESSION> iconsHandler,
                       FrameIterationHandler iterationHandler,
                       boolean permanentCached) {
        super(name, first, second);
        this.iconsHandler = iconsHandler;
        this.iterationHandler = iterationHandler;
        this.permanentCached = permanentCached;
    }

}
