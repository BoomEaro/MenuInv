package ru.boomearo.menuinv.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import org.bukkit.inventory.InventoryView;
import ru.boomearo.menuinv.objects.InventoryPageImpl;
import ru.boomearo.menuinv.objects.MenuInvHolder;

public class InventoryListener implements Listener {

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

        MenuInvHolder menuHolder = (MenuInvHolder) holder;

        //Здесь очевидно если нажать на пустоту, то будет нулл, и игрок попытается выбросить вещь. Не даем это сделать.
        Inventory clickedInventory = e.getClickedInventory();
        if (clickedInventory == null) {
            return;
        }

        //MenuInv.getInstance().getLogger().info("TEST " + e.getAction() + " " + e.getClick() + " " + clickedInventory + " " + e.getCursor());

        //Так-то разрешаем игроку перемещать вещи в своем инвентаре
        if (e.getView().getBottomInventory() == clickedInventory) {
            InventoryAction action = e.getAction();
            //Запрещаем перемещать из одного инвентаря в другой а так же собирать все похожие предметы. Нет способа узнать с какого инвентаря в какой перейдут предметы.
            //TODO Не известно, все ли способы здесь учтены.
            if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY || action == InventoryAction.COLLECT_TO_CURSOR) {
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);
            }

            return;
        }

        //Отменяем ивент только когда игрок пытается взаимодействовать с менюшкой.

        e.setCancelled(true);
        e.setResult(Event.Result.DENY);

        InventoryPageImpl page = menuHolder.getPage();

        page.handleInventoryClick(e.getSlot(), e.getClick());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDragEvent(InventoryDragEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        InventoryView view = e.getView();

        Inventory topInventory = view.getTopInventory();
        if (topInventory == null) {
            return;
        }
        if (!(topInventory.getHolder() instanceof MenuInvHolder)) {
            return;
        }

        // Всегда запрещаем модифицировать инвентарь
        e.setCancelled(true);
        e.setResult(Event.Result.DENY);
    }

}
