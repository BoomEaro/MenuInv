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
        // TODO Optimize
        for (Player pl : Bukkit.getOnlinePlayers()) {
            Inventory inv = pl.getOpenInventory().getTopInventory();

            InventoryHolder holder = inv.getHolder();

            if (holder instanceof MenuInventoryHolder mih) {
                mih.page().update();
            }
        }
    }
}
