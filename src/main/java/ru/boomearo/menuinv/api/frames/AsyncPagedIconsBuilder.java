package ru.boomearo.menuinv.api.frames;

import com.google.common.base.Preconditions;
import lombok.Getter;
import ru.boomearo.menuinv.api.AsyncResetHandler;
import ru.boomearo.menuinv.api.Delayable;
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

    public AsyncPagedIconsBuilder setExecutorService(ExecutorService executorService) {
        Preconditions.checkArgument(executorService != null, "executorService is null!");
        this.executorService = executorService;
        return this;
    }

    public AsyncPagedIconsBuilder setLoadedPagedIcons(PagedIconsBuilder loadedPagedIcons) {
        Preconditions.checkArgument(loadedPagedIcons != null, "loadedPagedIcons is null!");
        this.loadedPagedIconsBuilder = loadedPagedIcons;
        return this;
    }

    public AsyncPagedIconsBuilder setLoadingPagedIcons(PagedIconsBuilder loadingPagedIcons) {
        Preconditions.checkArgument(loadingPagedIcons != null, "loadingPagedIcons is null!");
        this.loadingPagedIconsBuilder = loadingPagedIcons;
        return this;
    }

    public AsyncPagedIconsBuilder setAsyncIconResetHandler(AsyncResetHandler asyncResetHandler) {
        Preconditions.checkArgument(asyncResetHandler != null, "asyncIconResetHandler is null!");
        this.asyncResetHandler = asyncResetHandler;
        return this;
    }

    @Override
    public AsyncPagedIconsBuilder setFrameIterationHandler(FrameIterationHandler frameIterationHandler) {
        Preconditions.checkArgument(frameIterationHandler != null, "frameIterationHandler is null!");
        this.frameIterationHandler = frameIterationHandler;
        return this;
    }

    @Override
    public AsyncPagedIconsBuilder setCacheHandler(Delayable<InventoryPage> cacheHandler) {
        Preconditions.checkArgument(cacheHandler != null, "cacheHandler is null!");
        this.cacheHandler = cacheHandler;
        return this;
    }

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
