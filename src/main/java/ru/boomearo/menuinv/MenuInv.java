package ru.boomearo.menuinv;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import ru.boomearo.menuinv.api.*;
import ru.boomearo.menuinv.api.session.ConfirmData;
import ru.boomearo.menuinv.api.session.InventorySession;
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

        registerOwnMenu(this);

        this.getServer().getPluginManager().registerEvents(new InventoryListener(), this);

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

    //Регистрируем своё личное меню для остальных плагинов
    private static void registerOwnMenu(MenuInv menuInv) {
        PluginTemplatePages pages = Menu.registerPages(menuInv);

        initConfirmMenu(pages);

        if (menuInv.getConfig().getBoolean("debug")) {
            menuInv.getLogger().warning("Активирован режим дебага!");

            TestMenu.setupTest(pages);
        }
    }

    //Страница подтверждения, для использования другими плагинами, чтобы не копировать код много раз
    private static void initConfirmMenu(PluginTemplatePages pages) {
        pages.createTemplatePage("confirm", InvType.HOPPER)
                .setInventoryCreationHandler((inventoryPage) -> {
                    InventorySession session = inventoryPage.getSession();
                    if (session == null) {
                        return null;
                    }

                    ConfirmData data = session.getConfirmData();

                    if (data == null) {
                        return null;
                    }

                    return data.getInventoryName(session);
                }).setItem(0, () -> new IconHandler() {

                    @Override
                    public void onClick(InventoryPage inventoryPage, Player player, ClickType clickType) {
                        InventorySession session = inventoryPage.getSession();

                        if (session == null) {
                            return;
                        }

                        ConfirmData confirm = session.getConfirmData();

                        if (confirm == null) {
                            return;
                        }

                        confirm.executeCancel(inventoryPage);
                    }

                    @Override
                    public ItemStack onUpdate(InventoryPage inventoryPage, Player player) {
                        InventorySession session = inventoryPage.getSession();

                        if (session == null) {
                            return null;
                        }

                        ConfirmData confirm = session.getConfirmData();

                        if (confirm == null) {
                            return null;
                        }

                        return confirm.getCancelItem(inventoryPage);
                    }

                }).setItem(4, () -> new IconHandler() {

                    @Override
                    public void onClick(InventoryPage inventoryPage, Player player, ClickType clickType) {
                        InventorySession session = inventoryPage.getSession();

                        if (session == null) {
                            return;
                        }

                        ConfirmData confirm = session.getConfirmData();

                        if (confirm == null) {
                            return;
                        }

                        confirm.executeConfirm(inventoryPage);
                    }

                    @Override
                    public ItemStack onUpdate(InventoryPage inventoryPage, Player player) {
                        InventorySession session = inventoryPage.getSession();

                        if (session == null) {
                            return null;
                        }

                        ConfirmData confirm = session.getConfirmData();

                        if (confirm == null) {
                            return null;
                        }

                        return confirm.getConfirmItem(inventoryPage);
                    }

                });
    }
}
