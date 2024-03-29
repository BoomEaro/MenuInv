package ru.boomearo.menuinv.api.icon;

import com.google.common.base.Preconditions;
import ru.boomearo.menuinv.api.AsyncResetHandler;
import ru.boomearo.menuinv.api.InfinityUpdateDelay;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class AsyncIconBuilder implements ElementBuilder {

    private ExecutorService executorService = ForkJoinPool.commonPool();

    private ElementBuilder loadedIconBuilder = new IconBuilder();
    private ElementBuilder loadingIconBuilder = new IconBuilder();
    private AsyncResetHandler asyncResetHandler = (page, force) -> force;

    public AsyncIconBuilder setExecutorService(ExecutorService executorService) {
        Preconditions.checkArgument(executorService != null, "executorService is null!");
        this.executorService = executorService;
        return this;
    }

    public AsyncIconBuilder setLoadedIcon(ElementBuilder elementBuilder) {
        Preconditions.checkArgument(elementBuilder != null, "ElementBuilder is null!");
        this.loadedIconBuilder = elementBuilder;
        return this;
    }

    public AsyncIconBuilder setImmutableLoadedIcon(ElementBuilder elementBuilder) {
        if (elementBuilder instanceof ElementBuilderUpdatable) {
            ElementBuilderUpdatable<?> elementBuilderUpdatable = (ElementBuilderUpdatable<?>) elementBuilder;
            elementBuilderUpdatable.setUpdateDelay(new InfinityUpdateDelay<>(true));
        }

        return setLoadedIcon(elementBuilder);
    }

    public AsyncIconBuilder setLoadingIcon(ElementBuilder elementBuilder) {
        Preconditions.checkArgument(elementBuilder != null, "iconBuilder is null!");
        this.loadingIconBuilder = elementBuilder;
        return this;
    }

    public AsyncIconBuilder setImmutableLoadingIcon(ElementBuilder elementBuilder) {
        if (elementBuilder instanceof ElementBuilderUpdatable) {
            ElementBuilderUpdatable<?> elementBuilderUpdatable = (ElementBuilderUpdatable<?>) elementBuilder;
            elementBuilderUpdatable.setUpdateDelay(new InfinityUpdateDelay<>(true));
        }

        return setLoadingIcon(elementBuilder);
    }

    public AsyncIconBuilder setAsyncIconResetHandler(AsyncResetHandler asyncResetHandler) {
        Preconditions.checkArgument(asyncResetHandler != null, "asyncIconResetHandler is null!");
        this.asyncResetHandler = asyncResetHandler;
        return this;
    }

    @Override
    public IconHandlerFactory build() {
        return () -> new AsyncIconHandler(this.executorService,
                this.loadedIconBuilder.build().create(),
                this.loadingIconBuilder.build().create(),
                this.asyncResetHandler
        );
    }

}
