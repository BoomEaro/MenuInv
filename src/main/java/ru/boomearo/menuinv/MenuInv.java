package ru.boomearo.menuinv;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ru.boomearo.menuinv.api.IPluginTemplatePages;
import ru.boomearo.menuinv.exceptions.MenuInvException;
import ru.boomearo.menuinv.listeners.InventoryListener;
import ru.boomearo.menuinv.objects.InventoryPage;
import ru.boomearo.menuinv.objects.PluginTemplatePages;
import ru.boomearo.menuinv.objects.TemplatePage;
import ru.boomearo.menuinv.test.TestMenu;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class MenuInv extends JavaPlugin {

    private final ConcurrentMap<Class<? extends JavaPlugin>, PluginTemplatePages> menu = new ConcurrentHashMap<>();

    private static MenuInv instance = null;

    @Override
    public void onEnable() {
        instance = this;

        this.getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        try {
            TestMenu.setupTest();
        }
        catch (MenuInvException e) {
            e.printStackTrace();
        }

        this.getLogger().info("Плагин включен.");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Плагин выключен.");
    }

    public static MenuInv getInstance() {
        return instance;
    }

    public IPluginTemplatePages registerPages(JavaPlugin plugin) throws MenuInvException {
        if (plugin == null) {
            throw new MenuInvException("Аргумент являются нулл!");
        }

        PluginTemplatePages tmp = this.menu.get(plugin.getClass());
        if (tmp != null) {
            throw new MenuInvException("Страницы плагина '" + plugin.getName() + "' уже зарегистрированы!");
        }

        PluginTemplatePages pages = new PluginTemplatePages(plugin);

        this.menu.put(plugin.getClass(), pages);

        return pages;
    }

    public void unregisterPages(JavaPlugin plugin) throws MenuInvException {
        if (plugin == null) {
            throw new MenuInvException("Аргумент являются нулл!");
        }

        PluginTemplatePages tmp = this.menu.get(plugin.getClass());
        if (tmp == null) {
            throw new MenuInvException("Страницы плагина '" + plugin.getName() + "' еще не были зарегистрированы!");
        }

        this.menu.remove(plugin.getClass());
    }


    public void openMenu(JavaPlugin plugin, String page, Player player) throws MenuInvException {
        if (plugin == null || page == null || player == null) {
            throw new MenuInvException("Аргументы являются нулл!");
        }

        PluginTemplatePages pp = this.menu.get(plugin.getClass());
        if (pp == null) {
            throw new MenuInvException("Плагин '" + plugin.getName() + "' не зарегистрировал страницы!");
        }

        TemplatePage templatePage = pp.getTemplatePage(page);
        if (templatePage == null) {
            throw new MenuInvException("Плагин '" + plugin.getName() + "' еще не зарегистрировал страницу '" + page + "'");
        }

        Bukkit.getScheduler().runTask(this, () -> {
            try {
                //TODO
                InventoryPage newPage = templatePage.createNewInventoryPage(player);
                newPage.update();

                player.openInventory(newPage.getInventory());
                //TODO добавить в this.menu
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
