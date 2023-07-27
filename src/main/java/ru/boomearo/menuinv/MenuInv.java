package ru.boomearo.menuinv;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

import ru.boomearo.menuinv.api.*;
import ru.boomearo.menuinv.listeners.InventoryListener;
import ru.boomearo.menuinv.runnable.MenuUpdater;
import ru.boomearo.menuinv.test.TestMenu;

import java.io.File;

public final class MenuInv extends JavaPlugin {

    private MenuUpdater updater = null;

    private static MenuInv instance = null;

    @Override
    public void onEnable() {
        instance = this;

        File configFile = new File(getDataFolder() + File.separator + "config.yml");
        if (!configFile.exists()) {
            getLogger().info("Config not found, creating a new one...");
            saveDefaultConfig();
        }

        this.getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        Menu.initMenu(this);
        TestMenu.setupTest(this);

        this.updater = new MenuUpdater();

        this.getLogger().info("Plugin successfully enabled.");
    }

    @Override
    public void onDisable() {
        this.updater.cancel();

        //Закрываем всем инвентари этого меню, вне зависимости от всего
        for (Player player : Bukkit.getOnlinePlayers()) {
            InventoryHolder holder = player.getOpenInventory().getTopInventory().getHolder();
            if (holder instanceof MenuInvHolder) {
                MenuInvHolder mih = (MenuInvHolder) holder;
                InventoryPageImpl page = mih.getPage();

                page.close(true);
            }
        }

        this.getLogger().info("Plugin successfully disabled.");
    }

    public static MenuInv getInstance() {
        return instance;
    }
}
