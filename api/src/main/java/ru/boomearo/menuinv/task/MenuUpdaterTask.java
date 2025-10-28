package ru.boomearo.menuinv.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;

import ru.boomearo.menuinv.api.MenuInventoryHolder;

public class MenuUpdaterTask extends BukkitRunnable {

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Inventory inventory = player.getOpenInventory().getTopInventory();
            InventoryHolder holder = inventory.getHolder(false);
            if (holder instanceof MenuInventoryHolder menuInventoryHolder) {
                menuInventoryHolder.page().update();
            }
        }
    }
}
