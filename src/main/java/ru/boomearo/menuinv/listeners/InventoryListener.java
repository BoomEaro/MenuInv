package ru.boomearo.menuinv.listeners;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import org.bukkit.inventory.InventoryView;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.InventoryPageImpl;
import ru.boomearo.menuinv.api.Menu;
import ru.boomearo.menuinv.api.MenuInventoryHolder;

public class InventoryListener implements Listener {

    private static final int OUTSIDE = -999;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClickEvent(InventoryClickEvent e) {
        InventoryView view = e.getView();
        if (view == null) {
            return;
        }

        Inventory topInventory = view.getTopInventory();
        if (topInventory == null) {
            return;
        }

        Inventory clickedInventory = e.getClickedInventory();
        if (clickedInventory == null) {
            return;
        }

        InventoryHolder holder = topInventory.getHolder();
        if (!(holder instanceof MenuInventoryHolder)) {
            return;
        }

        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        Player pl = (Player) e.getWhoClicked();

        MenuInventoryHolder menuHolder = (MenuInventoryHolder) holder;
        InventoryPageImpl page = menuHolder.getPage();

        // Allowing the player to modify their inventory
        if (view.getBottomInventory() == clickedInventory) {
            InventoryAction action = e.getAction();

            // TODO More InventoryActions to block?
            if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY || action == InventoryAction.COLLECT_TO_CURSOR) {
                e.setCancelled(true);
            }

            if (!page.getBottomInventoryClickHandler().canClick(page, pl, e.getSlot(), e.getClick())) {
                e.setCancelled(true);
            }

            return;
        }

        // Now we cancel the event if it is a menu
        e.setCancelled(true);

        page.handleInventoryClick(e.getSlot(), e.getClick());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryDragEvent(InventoryDragEvent e) {
        InventoryView view = e.getView();

        if (view == null) {
            return;
        }

        Inventory topInventory = view.getTopInventory();
        if (topInventory == null) {
            return;
        }

        if (!(topInventory.getHolder() instanceof MenuInventoryHolder)) {
            return;
        }

        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        for (Integer slot : e.getRawSlots()) {
            Inventory i = getInventory(view, slot);
            if (i == null) {
                continue;
            }

            if (i == topInventory) {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryCloseEvent(InventoryCloseEvent e) {
        InventoryView view = e.getView();

        if (view == null) {
            return;
        }

        Inventory topInventory = view.getTopInventory();
        if (topInventory == null) {
            return;
        }

        if (!(e.getPlayer() instanceof Player)) {
            return;
        }

        if (!(topInventory.getHolder() instanceof MenuInventoryHolder)) {
            return;
        }

        Player pl = (Player) e.getPlayer();

        MenuInventoryHolder menuInventoryHolder = (MenuInventoryHolder) topInventory.getHolder();

        InventoryPage inventoryPage = menuInventoryHolder.getPage();

        inventoryPage.getInventoryCloseHandler().onClose(inventoryPage, pl);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginDisableEvent(PluginDisableEvent e) {
        Menu.unregisterPages(e.getPlugin());
    }

    // Copied from newer version for backend compatibility
    private static Inventory getInventory(InventoryView view, int rawSlot) {
        // Slot may be -1 if not properly detected due to client bug
        // e.g. dropping an item into part of the enchantment list section of an enchanting table
        if (rawSlot == OUTSIDE || rawSlot == -1) {
            return null;
        }
        Preconditions.checkArgument(rawSlot >= 0, "Negative, non outside slot %s", rawSlot);
        Preconditions.checkArgument(rawSlot < view.countSlots(), "Slot %s greater than inventory slot count", rawSlot);

        if (rawSlot < view.getTopInventory().getSize()) {
            return view.getTopInventory();
        }
        else {
            return view.getBottomInventory();
        }
    }

}
