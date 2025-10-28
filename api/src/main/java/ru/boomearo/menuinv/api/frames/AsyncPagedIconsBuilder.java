package ru.boomearo.menuinv.api.frames;

import lombok.Getter;
import lombok.NonNull;
import ru.boomearo.menuinv.api.AsyncResetHandler;
import ru.boomearo.menuinv.api.Delayable;
import ru.boomearo.menuinv.api.InfinityUpdateDelay;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.frames.iteration.DefaultIterationHandlerImpl;
import ru.boomearo.menuinv.api.frames.iteration.FrameIterationHandler;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

@Getter
public class AsyncPagedIconsBuilder implements PagedElementBuilder {

    private ExecutorService executorService = ForkJoinPool.commonPool();

    private PagedIconsBuilder loadedPagedIconsBuilder = new PagedIconsBuilder();
    private PagedIconsBuilder loadingPagedIconsBuilder = new PagedIconsBuilder();
    private AsyncResetHandler asyncResetHandler = (page, force) -> force;

    private FrameIterationHandler frameIterationHandler = DefaultIterationHandlerImpl.DEFAULT;
    private Delayable<InventoryPage> cacheHandler = (page, force) -> Duration.ZERO;

    @NonNull
    public AsyncPagedIconsBuilder setExecutorService(@NonNull ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }

    @NonNull
    public AsyncPagedIconsBuilder setLoadedPagedIcons(@NonNull PagedIconsBuilder loadedPagedIcons) {
        this.loadedPagedIconsBuilder = loadedPagedIcons;
        return this;
    }

    @NonNull
    public AsyncPagedIconsBuilder setImmutableLoadedPagedIcons(@NonNull PagedIconsBuilder loadedPagedIcons) {
        loadedPagedIcons.setUpdateDelay(new InfinityUpdateDelay<>(true));
        loadedPagedIcons.setCacheHandler(new InfinityUpdateDelay<>(true));

        this.loadedPagedIconsBuilder = loadedPagedIcons;
        return this;
    }

    @NonNull
    public AsyncPagedIconsBuilder setLoadingPagedIcons(@NonNull PagedIconsBuilder loadingPagedIcons) {
        this.loadingPagedIconsBuilder = loadingPagedIcons;
        return this;
    }

    @NonNull
    public AsyncPagedIconsBuilder setImmutableLoadingPagedIcons(@NonNull PagedIconsBuilder loadingPagedIcons) {
        loadingPagedIcons.setUpdateDelay(new InfinityUpdateDelay<>(true));
        loadingPagedIcons.setCacheHandler(new InfinityUpdateDelay<>(true));

        this.loadingPagedIconsBuilder = loadingPagedIcons;
        return this;
    }

    @NonNull
    public AsyncPagedIconsBuilder setAsyncIconResetHandler(@NonNull AsyncResetHandler asyncResetHandler) {
        this.asyncResetHandler = asyncResetHandler;
        return this;
    }

    @NonNull
    @Override
    public AsyncPagedIconsBuilder setFrameIterationHandler(@NonNull FrameIterationHandler frameIterationHandler) {
        this.frameIterationHandler = frameIterationHandler;
        return this;
    }

    @NonNull
    @Override
    public AsyncPagedIconsBuilder setCacheHandler(@NonNull Delayable<InventoryPage> cacheHandler) {
        this.cacheHandler = cacheHandler;
        return this;
    }

    @NonNull
    @Override
    public FramedIconsHandlerFactory build() {
        return () -> new AsyncFramedIconsHandler(
                this.executorService,
                this.loadedPagedIconsBuilder.build().create(),
                this.loadingPagedIconsBuilder.build().create(),
                this.asyncResetHandler
        );
    }
}
