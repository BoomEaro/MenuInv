package ru.boomearo.menuinv.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import ru.boomearo.menuinv.objects.InventoryPageImpl;
import ru.boomearo.menuinv.objects.MenuInvHolder;

public class InventoryListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDragEvent(InventoryDragEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        Inventory topInventory = e.getView().getTopInventory();
        if (topInventory == null) {
            return;
        }
        if (!(topInventory.getHolder() instanceof MenuInvHolder)) {
            return;
        }

        e.setCancelled(true);
        e.setResult(Event.Result.DENY);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClickEvent(InventoryClickEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        Inventory topInventory = e.getView().getTopInventory();
        if (topInventory == null) {
            return;
        }

        InventoryHolder holder = topInventory.getHolder();
        if (!(holder instanceof MenuInvHolder)) {
            return;
        }

        e.setCancelled(true);
        e.setResult(Event.Result.DENY);

        //Здесь очевидно если нажать на пустоту, то будет нулл, и игрок попытается выбросить вещь. Не даем это сделать.
        Inventory clickedInventory = e.getClickedInventory();
        if (clickedInventory == null) {
            return;
        }

        //Нижний инвентарь мы не обрабатываем здесь. Он не попадает в основную обработку.
        if (e.getView().getBottomInventory() == clickedInventory) {
            return;
        }

        MenuInvHolder menuHolder = (MenuInvHolder) holder;

        InventoryPageImpl page = menuHolder.getPage();

        page.handleInventoryClick(e.getSlot(), e.getClick());
    }
}
