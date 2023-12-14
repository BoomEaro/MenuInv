package ru.boomearo.menuinv.api.icon;

import com.google.common.base.Preconditions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class AsyncIconBuilder implements ElementBuilder {

    private ExecutorService executorService = ForkJoinPool.commonPool();

    private ElementBuilder loadedIconBuilder = new IconBuilder();
    private ElementBuilder loadingIconBuilder = new IconBuilder();
    private AsyncIconResetHandler asyncIconResetHandler = (page, force) -> force;

    public AsyncIconBuilder setExecutorService(ExecutorService executorService) {
        Preconditions.checkArgument(executorService != null, "executorService is null!");
        this.executorService = executorService;
        return this;
    }

    public AsyncIconBuilder setLoadedIconBuilder(ElementBuilder elementBuilder) {
        Preconditions.checkArgument(elementBuilder != null, "ElementBuilder is null!");
        this.loadedIconBuilder = elementBuilder;
        return this;
    }

    public AsyncIconBuilder setImmutableLoadedIconBuilder(ElementBuilder elementBuilder) {
        if (elementBuilder instanceof ElementBuilderUpdatable) {
            ElementBuilderUpdatable<?> elementBuilderUpdatable = (ElementBuilderUpdatable<?>) elementBuilder;
            elementBuilderUpdatable.setUpdateDelay((inventoryPage, force) -> Long.MAX_VALUE);
        }

        return setLoadedIconBuilder(elementBuilder);
    }

    public AsyncIconBuilder setLoadingIconBuilder(ElementBuilder elementBuilder) {
        Preconditions.checkArgument(elementBuilder != null, "iconBuilder is null!");
        this.loadingIconBuilder = elementBuilder;
        return this;
    }

    public AsyncIconBuilder setImmutableLoadingIconBuilder(ElementBuilder elementBuilder) {
        if (elementBuilder instanceof ElementBuilderUpdatable) {
            ElementBuilderUpdatable<?> elementBuilderUpdatable = (ElementBuilderUpdatable<?>) elementBuilder;
            elementBuilderUpdatable.setUpdateDelay((inventoryPage, force) -> Long.MAX_VALUE);
        }

        return setLoadingIconBuilder(elementBuilder);
    }

    public AsyncIconBuilder setAsyncIconResetHandler(AsyncIconResetHandler asyncIconResetHandler) {
        Preconditions.checkArgument(asyncIconResetHandler != null, "asyncIconResetHandler is null!");
        this.asyncIconResetHandler = asyncIconResetHandler;
        return this;
    }

    @Override
    public IconHandlerFactory build() {
        return () -> new AsyncIconHandler(this.executorService,
                this.loadedIconBuilder.build().create(),
                this.loadingIconBuilder.build().create(),
                this.asyncIconResetHandler) {
        };
    }

}
