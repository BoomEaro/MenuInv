package ru.boomearo.menuinv.api.icon;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.InventoryPage;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public abstract class AsyncIconHandler extends IconHandler {

    private final ExecutorService executorService;
    private final IconHandler onLoadedHandler;
    private final IconHandler onLoadingHandler;
    private final AsyncIconResetHandler asyncIconResetHandler;

    private IconHandler currentHandler;

    private ItemStack itemResult = null;
    private Exception exceptionResult = null;

    private Future<?> task = null;
    private boolean forceUpdate = false;

    public AsyncIconHandler(ExecutorService executorService,
                            IconHandler onLoadedHandler,
                            IconHandler onLoadingHandler,
                            AsyncIconResetHandler asyncIconResetHandler
    ) {
        this.executorService = executorService;
        this.onLoadedHandler = onLoadedHandler;
        this.onLoadingHandler = onLoadingHandler;
        this.asyncIconResetHandler = asyncIconResetHandler;

        this.currentHandler = this.onLoadingHandler;
    }

    @Override
    public ItemStack onUpdate(InventoryPage page, Player player) throws Exception {
        if (this.task != null) {
            if (this.task.isDone()) {
                this.task = null;
            }
        }
        else {
            this.task = this.executorService.submit(() -> {
                try {
                    if (page.isClosed() || !page.isHandlerExists(this)) {
                        return;
                    }

                    this.itemResult = this.onLoadedHandler.onUpdate(page, player);
                }
                catch (Exception e) {
                    this.exceptionResult = e;
                }
                finally {
                    this.currentHandler = this.onLoadedHandler;
                    this.forceUpdate = true;
                }
            });
        }

        if (this.itemResult != null) {
            return this.itemResult;
        }

        if (this.exceptionResult != null) {
            throw exceptionResult;
        }

        return this.onLoadingHandler.onUpdate(page, player);
    }

    @Override
    public void onClick(InventoryPage page, ItemIcon icon, Player player, ClickType click) {
        this.currentHandler.onClick(page, icon, player, click);
    }

    @Override
    public Duration getClickTime(InventoryPage page, Player player, ClickType click) {
        return this.currentHandler.getClickTime(page, player, click);
    }

    @Override
    public int compareTo(IconHandler other) {
        return this.currentHandler.compareTo(other);
    }

    @Override
    public Duration onUpdateTime(InventoryPage page, boolean force) {
        if (this.asyncIconResetHandler.onIconReset(page, force)) {
            this.currentHandler = this.onLoadingHandler;
            this.itemResult = null;
            this.exceptionResult = null;

            if (this.task != null) {
                this.task.cancel(true);
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
