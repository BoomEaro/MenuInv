package ru.boomearo.menuinv.api.frames;

import lombok.Getter;
import ru.boomearo.menuinv.api.Delayable;
import ru.boomearo.menuinv.api.InventoryLocation;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.frames.iteration.FrameIterationHandler;

@Getter
public class FramedIcons extends Frame {

    protected final FramedIconsHandler iconsHandler;
    protected final FrameIterationHandler iterationHandler;
    protected final Delayable<InventoryPage> cacheHandler;

    public FramedIcons(String name,
                       InventoryLocation first,
                       InventoryLocation second,
                       FramedIconsHandler iconsHandler,
                       FrameIterationHandler iterationHandler,
                       Delayable<InventoryPage> cacheHandler) {
        super(name, first, second);
        this.iconsHandler = iconsHandler;
        this.iterationHandler = iterationHandler;
        this.cacheHandler = cacheHandler;
    }

}
