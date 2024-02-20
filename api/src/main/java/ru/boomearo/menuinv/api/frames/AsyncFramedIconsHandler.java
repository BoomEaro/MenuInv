package ru.boomearo.menuinv.api.frames;

import org.bukkit.entity.Player;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.AsyncResetHandler;
import ru.boomearo.menuinv.api.icon.IconHandler;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class AsyncFramedIconsHandler implements FramedIconsHandler {

    private final ExecutorService executorService;
    private final FramedIconsHandler onLoadedHandler;
    private final FramedIconsHandler onLoadingHandler;
    private final AsyncResetHandler asyncResetHandler;

    private FramedIconsHandler currentHandler;

    private List<IconHandler> handlersResult = null;
    private Exception exceptionResult = null;

    private Future<?> task = null;
    private boolean forceUpdate = false;

    public AsyncFramedIconsHandler(ExecutorService executorService,
                                   FramedIconsHandler onLoadedHandler,
                                   FramedIconsHandler onLoadingHandler,
                                   AsyncResetHandler asyncResetHandler
    ) {
        this.executorService = executorService;
        this.onLoadedHandler = onLoadedHandler;
        this.onLoadingHandler = onLoadingHandler;
        this.asyncResetHandler = asyncResetHandler;

        this.currentHandler = this.onLoadingHandler;
    }

    @Override
    public List<IconHandler> onUpdate(InventoryPage page, Player player) throws Exception {
        if (this.task != null) {
            if (this.task.isDone()) {
                this.task = null;
            }
        } else {
            this.task = this.executorService.submit(() -> {
                try {
                    if (page.isClosed()) {
                        return;
                    }

                    this.handlersResult = this.onLoadedHandler.onUpdate(page, player);
                } catch (Exception e) {
                    this.exceptionResult = e;
                } finally {
                    this.currentHandler = this.onLoadedHandler;
                    this.forceUpdate = true;
                }
            });
        }

        if (this.handlersResult != null) {
            return this.handlersResult;
        }

        if (this.exceptionResult != null) {
            throw exceptionResult;
        }

        return this.onLoadingHandler.onUpdate(page, player);
    }

    @Override
    public Duration onUpdateTime(InventoryPage page, boolean force) {
        if (this.asyncResetHandler.onIconReset(page, force)) {
            this.currentHandler = this.onLoadingHandler;
            this.handlersResult = null;
            this.exceptionResult = null;

            if (this.task != null) {
                this.task.cancel(false);
                this.task = null;
            }
        }

        if (this.forceUpdate) {
            this.forceUpdate = false;
            return Duration.ZERO;
        }

        return this.currentHandler.onUpdateTime(page, force);
    }
}
