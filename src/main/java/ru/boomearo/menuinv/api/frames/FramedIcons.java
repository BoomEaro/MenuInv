package ru.boomearo.menuinv.api.frames;

import lombok.Getter;
import ru.boomearo.menuinv.api.InventoryLocation;
import ru.boomearo.menuinv.api.frames.iteration.FrameIterationHandler;

@Getter
public class FramedIcons extends Frame {

    protected final FramedIconsHandler iconsHandler;
    protected final FrameIterationHandler iterationHandler;
    protected final boolean permanentCached;

    public FramedIcons(String name,
                       InventoryLocation first,
                       InventoryLocation second,
                       FramedIconsHandler iconsHandler,
                       FrameIterationHandler iterationHandler,
                       boolean permanentCached) {
        super(name, first, second);
        this.iconsHandler = iconsHandler;
        this.iterationHandler = iterationHandler;
        this.permanentCached = permanentCached;
    }

}
