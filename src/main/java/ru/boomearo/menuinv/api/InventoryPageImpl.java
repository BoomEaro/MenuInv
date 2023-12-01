package ru.boomearo.menuinv.api;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ru.boomearo.menuinv.MenuInv;
import ru.boomearo.menuinv.api.icon.*;
import ru.boomearo.menuinv.api.session.InventorySession;
import ru.boomearo.menuinv.api.frames.PagedIcons;

import java.util.*;

@Getter
public class InventoryPageImpl<SESSION extends InventorySession> implements InventoryPage<SESSION> {

    private final String name;
    private final MenuType menuType;
    private final InventoryTitleHandler<SESSION> inventoryTitleHandler;
    private final InventoryReopenHandler<SESSION> inventoryReopenHandler;
    private final ClickExceptionHandler<SESSION> clickExceptionHandler;
    private final UpdateExceptionHandler<SESSION> updateExceptionHandler;
    private final InventoryCloseHandler<SESSION> inventoryCloseHandler;
    private final Delayable<InventoryPage<SESSION>> globalUpdateDelay;
    private final BottomInventoryClickHandler<SESSION> bottomInventoryClickHandler;

    private final Map<String, PagedIcons<SESSION>> listedIcons;

    private final ItemIcon<SESSION>[] activeIcons;

    private Inventory inventory;

    private final Player player;

    private final SESSION session;

    private final TemplatePageImpl<SESSION> templatePage;

    private long cooldown = 0;

    private boolean changes = false;

    public InventoryPageImpl(String name,
                             MenuType menuType,
                             Map<Integer, ItemIcon<SESSION>> iconsPosition,
                             Map<String, PagedIcons<SESSION>> listedIcons,
                             InventoryTitleHandler<SESSION> inventoryTitleHandler,
                             InventoryReopenHandler<SESSION> inventoryReopenHandler,
                             ClickExceptionHandler<SESSION> clickExceptionHandler,
                             UpdateExceptionHandler<SESSION> updateExceptionHandler,
                             InventoryCloseHandler<SESSION> inventoryCloseHandler,
                             Delayable<InventoryPage<SESSION>> globalUpdateDelay,
                             BottomInventoryClickHandler<SESSION> bottomInventoryClickHandler,
                             IconHandlerFactory<SESSION> background,
                             Player player,
                             SESSION session,
                             TemplatePageImpl<SESSION> templatePage) {
        this.name = name;
        this.menuType = menuType;
        this.listedIcons = listedIcons;
        this.inventoryTitleHandler = inventoryTitleHandler;
        this.inventoryReopenHandler = inventoryReopenHandler;
        this.clickExceptionHandler = clickExceptionHandler;
        this.updateExceptionHandler = updateExceptionHandler;
        this.inventoryCloseHandler = inventoryCloseHandler;
        this.globalUpdateDelay = globalUpdateDelay;
        this.bottomInventoryClickHandler = bottomInventoryClickHandler;
        this.player = player;
        this.session = session;
        this.templatePage = templatePage;

        // Create a new bukkit inventory and add our own holder to it to identify the inventory
        this.inventory = this.menuType.createInventory(new MenuInventoryHolder<>(this), this.inventoryTitleHandler.createTitle(this));

        // Create an array of active size items in the current inventory
        this.activeIcons = new ItemIcon[this.menuType.getSize()];
        // Filling the array with nulls
        Arrays.fill(this.activeIcons, null);

        // First, we fill the array of active objects with the background.
        if (background != null) {
            for (int i = 0; i < this.menuType.getSize(); i++) {
                this.activeIcons[i] = new ItemIcon<>(i, background.create());
            }
        }

        // Then we fill the array of active objects with independent objects.
        for (ItemIcon<SESSION> ii : iconsPosition.values()) {
            this.activeIcons[ii.getSlot()] = ii;
        }
    }

    @Override
    public void setNeedUpdate() {
        this.changes = true;
    }

    @Override
    public PagedIcons<SESSION> getListedIconsItems(String name) {
        return this.listedIcons.get(name);
    }

    public void handleInventoryClick(int slot, ClickType type) {
        ItemIcon<SESSION> ii = this.activeIcons[slot];
        if (ii != null) {

            try {
                ii.getHandler().handleClick(this, this.player, type);
            }
            catch (Exception e) {
                this.clickExceptionHandler.onException(this, this.player, type, e);
            }
        }
    }

    @Override
    public void update(boolean force) {
        performUpdate(force, true, false);
    }

    public void updateOnCreate() {
        performUpdate(true, false, true);
    }

    private void performUpdate() {
        performUpdate(false, false, false);
    }

    private void performUpdate(boolean force, boolean reopenIfNeed, boolean create) {
        boolean forceUpdate = this.changes || force;

        if (this.globalUpdateDelay.canUpdate(this, force, this.cooldown) || create) {
            this.cooldown = System.currentTimeMillis();

            if (reopenIfNeed) {
                try {
                    if (this.inventoryReopenHandler.reopenCondition(this, forceUpdate)) {
                        reopen(true);
                        return;
                    }
                }
                catch (Exception e) {
                    this.updateExceptionHandler.onException(this, this.player, e);
                }
            }

            // Update the current array of active items using frame items.
            for (PagedIcons<SESSION> lii : this.listedIcons.values()) {
                lii.updateActiveIcons(this, this.activeIcons, forceUpdate, create, this.updateExceptionHandler);
            }

            // Using an array of active items, we fill the array with Bukkit items
            for (ItemIcon<SESSION> ii : this.activeIcons) {
                if (ii == null) {
                    continue;
                }

                ItemStack itemStack = ii.getItemStack(this, forceUpdate, create, this.updateExceptionHandler);
                if (itemStack == null) {
                    continue;
                }

                this.inventory.setItem(ii.getSlot(), itemStack);
            }

            this.changes = false;
        }
    }

    @Override
    public void reopen(boolean force) {
        if (force) {
            performReopen();
            return;
        }

        Bukkit.getScheduler().runTask(MenuInv.getInstance(), this::performReopen);
    }

    // Recreate the page
    // TODO Works weird. Namely, if during the opening of the inventory the player closes it, then the player will open a phantom inventory.
    private void performReopen() {
        // First, create a new instance of bukkit's inventory
        this.inventory = this.menuType.createInventory(new MenuInventoryHolder<>(this), this.inventoryTitleHandler.createTitle(this));
        // Clear page scroll changes
        for (PagedIcons<SESSION> pi : this.listedIcons.values()) {
            pi.resetChanges();
        }
        // Filling inventory
        performUpdate();
        // Open this inventory to that player
        this.player.openInventory(this.inventory);
    }

    @Override
    public void close(boolean force) {
        if (force) {
            this.player.closeInventory();
            return;
        }

        Bukkit.getScheduler().runTask(MenuInv.getInstance(), this.player::closeInventory);
    }

}
