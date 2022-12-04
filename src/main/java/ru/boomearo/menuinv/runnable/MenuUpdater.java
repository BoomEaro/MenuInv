package ru.boomearo.menuinv.runnable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;

import ru.boomearo.menuinv.MenuInv;
import ru.boomearo.menuinv.objects.MenuInvHolder;

public class MenuUpdater extends BukkitRunnable {

    public MenuUpdater() {
        runnable();
    }

    private void runnable() {
        this.runTaskTimer(MenuInv.getInstance(), 1, 1);
    }

    @Override
    public void run() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            Inventory inv = pl.getOpenInventory().getTopInventory();

            InventoryHolder holder = inv.getHolder();

            if (holder instanceof MenuInvHolder) {
                MenuInvHolder mih = (MenuInvHolder) holder;

                mih.getPage().update();
            }
        }
    }


}
