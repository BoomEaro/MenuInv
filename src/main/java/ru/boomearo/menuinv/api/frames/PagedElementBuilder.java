package ru.boomearo.menuinv.api.frames;

import ru.boomearo.menuinv.api.Delayable;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.frames.iteration.FrameIterationHandler;

public interface PagedElementBuilder {

    FramedIconsHandlerFactory build();

    PagedElementBuilder setFrameIterationHandler(FrameIterationHandler frameIterationHandler);

    FrameIterationHandler getFrameIterationHandler();

    PagedElementBuilder setCacheHandler(Delayable<InventoryPage> cacheHandler);

    Delayable<InventoryPage> getCacheHandler();

}
