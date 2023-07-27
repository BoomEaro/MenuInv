package ru.boomearo.menuinv.listeners;

import com.google.common.base.Preconditions;
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
import ru.boomearo.menuinv.api.InventoryPageImpl;
import ru.boomearo.menuinv.api.MenuInvHolder;

public class InventoryListener implements Listener {

    private static final int OUTSIDE = -999;

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

        //Разрешаем игроку модифицировать инвентарь через драг ивент, однако до тех пор, пока в нем не окажется части менюшки.
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

    // Copied from newer version to backend compatability
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
