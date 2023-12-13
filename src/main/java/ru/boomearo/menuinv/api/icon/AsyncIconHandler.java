package ru.boomearo.menuinv.api.icon;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.InventoryPage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public abstract class AsyncIconHandler extends IconHandler {

    private final ExecutorService executorService;
    private final IconHandler onLoadedHandler;
    private final IconHandler onLoadingHandler;

    private IconHandler currentHandler;
    private ItemStack result = null;
    private Future<?> task = null;
    private boolean forceUpdate = false;

    public AsyncIconHandler(ExecutorService executorService, IconHandler onLoadedHandler, IconHandler onLoadingHandler) {
        this.executorService = executorService;
        this.onLoadedHandler = onLoadedHandler;
        this.onLoadingHandler = onLoadingHandler;

        this.currentHandler = this.onLoadingHandler;
    }

    @Override
    public ItemStack onUpdate(InventoryPage page, Player player) {
        if (this.task != null) {
            if (this.task.isDone()) {
                this.task = null;
            }
        }
        else {
            this.task = this.executorService.submit(() -> {
                if (page.isClosed()) {
                    return;
                }

                this.result = this.onLoadedHandler.onUpdate(page, player);
                this.currentHandler = this.onLoadedHandler;
                this.forceUpdate = true;
            });
        }

        if (this.result != null) {
            return this.result;
        }

        return this.onLoadingHandler.onUpdate(page, player);
    }

    @Override
    public void onClick(InventoryPage page, Player player, ClickType click) {
        this.currentHandler.onClick(page, player, click);
    }

    @Override
    public long getClickTime(InventoryPage page, Player player, ClickType click) {
        return this.currentHandler.getClickTime(page, player, click);
    }

    @Override
    public int compareTo(IconHandler other) {
        return this.currentHandler.compareTo(other);
    }

    @Override
    public long onUpdateTime(InventoryPage page, boolean force) {
        if (force) {
            this.currentHandler = this.onLoadingHandler;
            this.result = null;

            if (this.task != null) {
                this.task.cancel(true);
                this.task = null;
            }
        }

        if (this.forceUpdate) {
            this.forceUpdate = false;
            return 0;
        }

        return this.currentHandler.onUpdateTime(page, force);
    }
}
