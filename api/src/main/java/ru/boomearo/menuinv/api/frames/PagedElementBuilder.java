package ru.boomearo.menuinv.api.frames;

import lombok.NonNull;
import ru.boomearo.menuinv.api.Delayable;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.frames.iteration.FrameIterationHandler;

public interface PagedElementBuilder {

    @NonNull
    FramedIconsHandlerFactory build();

    @NonNull
    PagedElementBuilder setFrameIterationHandler(@NonNull FrameIterationHandler frameIterationHandler);

    @NonNull
    FrameIterationHandler getFrameIterationHandler();

    @NonNull
    PagedElementBuilder setCacheHandler(@NonNull Delayable<InventoryPage> cacheHandler);

    @NonNull
    Delayable<InventoryPage> getCacheHandler();

}
