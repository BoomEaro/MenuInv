package ru.boomearo.menuinv.api.icon;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.AsyncResetHandler;
import ru.boomearo.menuinv.api.InventoryPage;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class AsyncIconHandler extends IconHandler {

    private final ExecutorService executorService;
    private final IconHandler onLoadedHandler;
    private final IconHandler onLoadingHandler;
    private final AsyncResetHandler asyncResetHandler;

    private IconHandler currentHandler;

    private ItemStack itemResult = null;
    private Exception exceptionResult = null;

    private Future<?> task = null;
    private boolean forceUpdate = false;

    public AsyncIconHandler(@NonNull ExecutorService executorService,
                            @NonNull IconHandler onLoadedHandler,
                            @NonNull IconHandler onLoadingHandler,
                            @NonNull AsyncResetHandler asyncResetHandler
    ) {
        this.executorService = executorService;
        this.onLoadedHandler = onLoadedHandler;
        this.onLoadingHandler = onLoadingHandler;
        this.asyncResetHandler = asyncResetHandler;

        this.currentHandler = this.onLoadingHandler;
    }

    @Nullable
    @Override
    public ItemStack onUpdate(@NonNull InventoryPage page, @NonNull Player player) throws Exception {
        if (this.task != null) {
            if (this.task.isDone()) {
                this.task = null;
            }
        } else {
            this.task = this.executorService.submit(() -> {
                try {
                    if (page.isClosed() || !page.isHandlerExists(this)) {
                        return;
                    }

                    this.itemResult = this.onLoadedHandler.onUpdate(page, player);
                } catch (Exception e) {
                    this.exceptionResult = e;
                } finally {
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
    public void onClick(@NonNull InventoryPage page, @NonNull ItemIcon icon, @NonNull Player player, @NonNull ClickType click) {
        this.currentHandler.onClick(page, icon, player, click);
    }

    @Nullable
    @Override
    public Duration getClickTime(@NonNull InventoryPage page, @NonNull Player player, @NonNull ClickType click) {
        return this.currentHandler.getClickTime(page, player, click);
    }

    @Override
    public int compareTo(IconHandler other) {
        return this.currentHandler.compareTo(other);
    }

    @Nullable
    @Override
    public Duration onUpdateTime(@NonNull InventoryPage page, boolean force) {
        if (this.asyncResetHandler.onIconReset(page, force)) {
            this.currentHandler = this.onLoadingHandler;
            this.itemResult = null;
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
