package ru.boomearo.menuinv.api.icon;

import lombok.NonNull;
import ru.boomearo.menuinv.api.AsyncResetHandler;
import ru.boomearo.menuinv.api.InfinityUpdateDelay;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class AsyncIconBuilder implements ElementBuilder {

    private ExecutorService executorService = ForkJoinPool.commonPool();

    private ElementBuilder loadedIconBuilder = new IconBuilder();
    private ElementBuilder loadingIconBuilder = new IconBuilder();
    private AsyncResetHandler asyncResetHandler = (page, force) -> force;

    @NonNull
    public AsyncIconBuilder setExecutorService(@NonNull ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }

    @NonNull
    public AsyncIconBuilder setLoadedIcon(@NonNull ElementBuilder elementBuilder) {
        this.loadedIconBuilder = elementBuilder;
        return this;
    }

    @NonNull
    public AsyncIconBuilder setImmutableLoadedIcon(@NonNull ElementBuilder elementBuilder) {
        if (elementBuilder instanceof ElementBuilderUpdatable<?> elementBuilderUpdatable) {
            elementBuilderUpdatable.setUpdateDelay(new InfinityUpdateDelay<>(true));
        }

        return setLoadedIcon(elementBuilder);
    }

    @NonNull
    public AsyncIconBuilder setLoadingIcon(@NonNull ElementBuilder elementBuilder) {
        this.loadingIconBuilder = elementBuilder;
        return this;
    }

    @NonNull
    public AsyncIconBuilder setImmutableLoadingIcon(@NonNull ElementBuilder elementBuilder) {
        if (elementBuilder instanceof ElementBuilderUpdatable<?> elementBuilderUpdatable) {
            elementBuilderUpdatable.setUpdateDelay(new InfinityUpdateDelay<>(true));
        }

        return setLoadingIcon(elementBuilder);
    }

    @NonNull
    public AsyncIconBuilder setAsyncIconResetHandler(@NonNull AsyncResetHandler asyncResetHandler) {
        this.asyncResetHandler = asyncResetHandler;
        return this;
    }

    @NonNull
    @Override
    public IconHandlerFactory build() {
        return () -> new AsyncIconHandler(this.executorService,
                this.loadedIconBuilder.build().create(),
                this.loadingIconBuilder.build().create(),
                this.asyncResetHandler
        );
    }

}
