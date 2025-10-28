package ru.boomearo.menuinv.listeners;

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
import ru.boomearo.menuinv.api.InventoryPageImpl;
import ru.boomearo.menuinv.api.Menu;
import ru.boomearo.menuinv.api.MenuInventoryHolder;

public class InventoryListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (view == null) {
            return;
        }

        Inventory topInventory = view.getTopInventory();
        if (topInventory == null) {
            return;
        }

        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) {
            return;
        }

        InventoryHolder holder = topInventory.getHolder();
        if (!(holder instanceof MenuInventoryHolder menuHolder)) {
            return;
        }

        if (!(event.getWhoClicked() instanceof Player pl)) {
            return;
        }

        InventoryPageImpl page = menuHolder.page();

        // Allowing the player to modify their inventory
        if (view.getBottomInventory() == clickedInventory) {
            InventoryAction action = event.getAction();

            // TODO More InventoryActions to block?
            if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY || action == InventoryAction.COLLECT_TO_CURSOR) {
                event.setCancelled(true);
            }

            if (!page.getBottomInventoryClickHandler().canClick(page, pl, event.getSlot(), event.getClick())) {
                event.setCancelled(true);
            }

            return;
        }

        // Now we cancel the event if it is a menu
        event.setCancelled(true);

        page.handleInventoryClick(event.getSlot(), event.getClick());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryDragEvent(InventoryDragEvent event) {
        InventoryView view = event.getView();
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

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        for (Integer slot : event.getRawSlots()) {
            Inventory i = view.getInventory(slot);
            if (i == null) {
                continue;
            }

            if (i == topInventory) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        InventoryView view = event.getView();
        if (view == null) {
            return;
        }

        Inventory topInventory = view.getTopInventory();
        if (topInventory == null) {
            return;
        }

        if (!(event.getPlayer() instanceof Player pl)) {
            return;
        }

        if (!(topInventory.getHolder() instanceof MenuInventoryHolder menuInventoryHolder)) {
            return;
        }

        InventoryPageImpl inventoryPage = menuInventoryHolder.page();

        inventoryPage.getInventoryCloseHandler().onClose(inventoryPage, pl);
        inventoryPage.setClosed(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginDisableEvent(PluginDisableEvent event) {
        Menu.unregisterPages(event.getPlugin());
    }
}
