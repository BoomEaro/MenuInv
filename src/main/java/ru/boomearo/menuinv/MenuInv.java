package ru.boomearo.menuinv;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

import ru.boomearo.menuinv.api.PluginTemplatePages;
import ru.boomearo.menuinv.exceptions.MenuInvException;
import ru.boomearo.menuinv.listeners.InventoryListener;
import ru.boomearo.menuinv.objects.InventoryPageImpl;
import ru.boomearo.menuinv.objects.MenuInvHolder;
import ru.boomearo.menuinv.objects.PluginTemplatePagesImpl;
import ru.boomearo.menuinv.objects.TemplatePageImpl;
import ru.boomearo.menuinv.runnable.MenuUpdater;
import ru.boomearo.menuinv.test.TestMenu;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class MenuInv extends JavaPlugin {

    private final ConcurrentMap<Class<? extends JavaPlugin>, PluginTemplatePagesImpl> menu = new ConcurrentHashMap<>();

    private MenuUpdater updater = null;

    private static MenuInv instance = null;

    @Override
    public void onEnable() {
        instance = this;

        this.getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        this.updater = new MenuUpdater();

        //TODO TEST
        try {
            TestMenu.setupTest();
        }
        catch (MenuInvException e) {
            e.printStackTrace();
        }
        //TODO

        this.getLogger().info("Плагин включен.");
    }

    @Override
    public void onDisable() {
        //Закрываем всем инвентари этого меню, вне зависимости от всего
        for (Player player : Bukkit.getOnlinePlayers()) {
            InventoryHolder holder = player.getOpenInventory().getTopInventory().getHolder();
            if (holder instanceof MenuInvHolder) {
                MenuInvHolder mih = (MenuInvHolder) holder;

                InventoryPageImpl page = mih.getPage();

                page.close(true);
            }
        }

        this.getLogger().info("Плагин выключен.");
    }

    public static MenuInv getInstance() {
        return instance;
    }

    public MenuUpdater getMenuUpdater() {
        return this.updater;
    }

    public PluginTemplatePages registerPages(JavaPlugin plugin) throws MenuInvException {
        if (plugin == null) {
            throw new MenuInvException("Аргумент являются нулл!");
        }

        PluginTemplatePagesImpl tmp = this.menu.get(plugin.getClass());
        if (tmp != null) {
            throw new MenuInvException("Страницы плагина '" + plugin.getName() + "' уже зарегистрированы!");
        }

        PluginTemplatePagesImpl pages = new PluginTemplatePagesImpl(plugin);

        this.menu.put(plugin.getClass(), pages);

        return pages;
    }

    public void unregisterPages(JavaPlugin plugin) throws MenuInvException {
        if (plugin == null) {
            throw new MenuInvException("Аргумент являются нулл!");
        }

        PluginTemplatePagesImpl tmp = this.menu.get(plugin.getClass());
        if (tmp == null) {
            throw new MenuInvException("Страницы плагина '" + plugin.getName() + "' еще не были зарегистрированы!");
        }

        this.menu.remove(plugin.getClass());

        for (Player player : Bukkit.getOnlinePlayers()) {
            InventoryHolder holder = player.getOpenInventory().getTopInventory().getHolder();
            if (holder instanceof MenuInvHolder) {
                MenuInvHolder mih = (MenuInvHolder) holder;

                InventoryPageImpl page = mih.getPage();

                //Не будем сравнивать ссылки, потому что думаю, что может быть такая ситуация когда плагин не выгрузился.
                //Поэтому просто сравниваем имена, а потом закрываем этим игрокам инвентари
                if (page.getTemplatePage().getPluginTemplatePages().getPlugin().getName().equals(plugin.getName())) {
                    page.close(true);
                }
            }
        }
    }


    public void openMenu(JavaPlugin plugin, String page, Player player) throws MenuInvException {
        if (plugin == null || page == null || player == null) {
            throw new MenuInvException("Аргументы являются нулл!");
        }

        PluginTemplatePagesImpl pp = this.menu.get(plugin.getClass());
        if (pp == null) {
            throw new MenuInvException("Плагин '" + plugin.getName() + "' не зарегистрировал страницы!");
        }

        TemplatePageImpl templatePage = pp.getTemplatePage(page);
        if (templatePage == null) {
            throw new MenuInvException("Плагин '" + plugin.getName() + "' еще не зарегистрировал страницу '" + page + "'");
        }

        Bukkit.getScheduler().runTask(this, () -> {
            try {
                //TODO
                InventoryPageImpl newPage = templatePage.createNewInventoryPage(player);

                newPage.update(true);

                player.openInventory(newPage.getInventory());
                //TODO добавить в this.menu
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
