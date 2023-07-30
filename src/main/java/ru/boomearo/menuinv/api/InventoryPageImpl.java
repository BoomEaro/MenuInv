package ru.boomearo.menuinv.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ru.boomearo.menuinv.MenuInv;
import ru.boomearo.menuinv.api.icon.ClickExceptionHandler;
import ru.boomearo.menuinv.api.icon.IconHandlerFactory;
import ru.boomearo.menuinv.api.icon.ItemIcon;
import ru.boomearo.menuinv.api.icon.UpdateExceptionHandler;
import ru.boomearo.menuinv.api.session.InventorySession;
import ru.boomearo.menuinv.api.frames.PagedIcons;

import java.util.*;

public class InventoryPageImpl implements InventoryPage {

    private final String name;
    private final MenuType menuType;
    private final InventoryTitleHandler inventoryTitleHandler;
    private final InventoryReopenHandler inventoryReopenHandler;
    private final ClickExceptionHandler clickExceptionHandler;
    private final UpdateExceptionHandler updateExceptionHandler;

    private final Map<String, PagedIcons> listedIcons;

    private final ItemIcon[] activeIcons;

    private Inventory inventory;

    private final Player player;

    private final InventorySession session;

    private final TemplatePageImpl templatePage;

    private boolean changes = false;

    public InventoryPageImpl(String name,
                             MenuType menuType,
                             Map<Integer, ItemIcon> iconsPosition,
                             Map<String, PagedIcons> listedIcons,
                             InventoryTitleHandler inventoryTitleHandler,
                             InventoryReopenHandler inventoryReopenHandler,
                             ClickExceptionHandler clickExceptionHandler,
                             UpdateExceptionHandler updateExceptionHandler,
                             IconHandlerFactory background,
                             Player player,
                             InventorySession session,
                             TemplatePageImpl templatePage) {
        this.name = name;
        this.menuType = menuType;
        this.listedIcons = listedIcons;
        this.inventoryTitleHandler = inventoryTitleHandler;
        this.inventoryReopenHandler = inventoryReopenHandler;
        this.clickExceptionHandler = clickExceptionHandler;
        this.updateExceptionHandler = updateExceptionHandler;
        this.player = player;
        this.session = session;
        this.templatePage = templatePage;

        // Create a new bukkit inventory and add our own holder to it to identify the inventory
        this.inventory = this.menuType.createInventory(new MenuInventoryHolder(this), this.inventoryTitleHandler.createTitle(this));

        // Create an array of active size items in the current inventory
        this.activeIcons = new ItemIcon[this.menuType.getSize()];
        // Filling the array with nulls
        Arrays.fill(this.activeIcons, null);

        // First, we fill the array of active objects with the background.
        if (background != null) {
            for (int i = 0; i < this.menuType.getSize(); i++) {
                this.activeIcons[i] = new ItemIcon(i, background.create());
            }
        }

        // Then we fill the array of active objects with independent objects.
        for (ItemIcon ii : iconsPosition.values()) {
            this.activeIcons[ii.getSlot()] = ii;
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public MenuType getMenuType() {
        return this.menuType;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public InventorySession getSession() {
        return this.session;
    }

    @Override
    public void setNeedUpdate() {
        this.changes = true;
    }

    @Override
    public PagedIcons getListedIconsItems(String name) {
        return this.listedIcons.get(name);
    }

    public ItemIcon[] getUnsafeActiveIcons() {
        return this.activeIcons;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void handleInventoryClick(int slot, ClickType type) {
        ItemIcon ii = this.activeIcons[slot];
        if (ii != null) {

            try {
                ii.getHandler().handleClick(this, this.player, type);
            }
            catch (Exception e) {
                this.clickExceptionHandler.onException(this, this.player, type, e);
            }
        }
    }

    public TemplatePageImpl getTemplatePage() {
        return this.templatePage;
    }

    @Override
    public void update(boolean force) {
        performUpdate(force, true);
    }

    private void performUpdate(boolean force, boolean reopenIfNeed) {
        boolean forceUpdate = this.changes || force;

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

        ItemStack[] array = new ItemStack[this.menuType.getSize()];
        Arrays.fill(array, null);

        // Update the current array of active items using frame items.
        for (PagedIcons lii : this.listedIcons.values()) {
            lii.updateActiveIcons(this, forceUpdate, this.updateExceptionHandler);
        }

        // Using an array of active items, we fill the array of Bakkit items
        for (ItemIcon ii : this.activeIcons) {
            if (ii == null) {
                continue;
            }

            array[ii.getSlot()] = ii.getItemStack(this, forceUpdate, this.updateExceptionHandler);
        }

        this.inventory.setContents(array);

        this.changes = false;
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
        this.inventory = this.menuType.createInventory(new MenuInventoryHolder(this), this.inventoryTitleHandler.createTitle(this));
        // Clear page scroll changes
        for (PagedIcons pi : this.listedIcons.values()) {
            pi.resetChanges();
        }
        // Filling inventory
        performUpdate(false, false);
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
