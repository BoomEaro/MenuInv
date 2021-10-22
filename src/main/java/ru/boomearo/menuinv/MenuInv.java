package ru.boomearo.menuinv;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

import ru.boomearo.menuinv.api.InventorySession;
import ru.boomearo.menuinv.api.PluginTemplatePages;
import ru.boomearo.menuinv.exceptions.MenuInvException;
import ru.boomearo.menuinv.listeners.InventoryListener;
import ru.boomearo.menuinv.objects.InventoryPageImpl;
import ru.boomearo.menuinv.objects.MenuInvHolder;
import ru.boomearo.menuinv.objects.PluginTemplatePagesImpl;
import ru.boomearo.menuinv.objects.TemplatePageImpl;
import ru.boomearo.menuinv.runnable.MenuUpdater;
import ru.boomearo.menuinv.test.TestMenu;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class MenuInv extends JavaPlugin {

    private final Map<Class<? extends JavaPlugin>, PluginTemplatePagesImpl> menu = new HashMap<>();

    private MenuUpdater updater = null;

    private static MenuInv instance = null;

    @Override
    public void onEnable() {
        instance = this;

        File configFile = new File(getDataFolder() + File.separator + "config.yml");
        if (!configFile.exists()) {
            getLogger().info("Конфиг не найден, создаю новый...");
            saveDefaultConfig();
        }

        if (this.getConfig().getBoolean("debug")) {
            try {
                TestMenu.setupTest();
                this.getLogger().warning("Активирован режим дебага!");
            }
            catch (MenuInvException e) {
                e.printStackTrace();
            }
        }

        this.getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        this.updater = new MenuUpdater();

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

                //TODO Я не знаю, плохо ли принудительно закрывать инвентарь.. Возможно смогут стырить в какой то момент вещь
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

    /**
     * Регистрирует плагин для создания шаблона страниц
     * @param plugin Плагин, который регистрирует страницы
     * @return Шаблон страниц плагина
     */
    public PluginTemplatePages registerPages(JavaPlugin plugin) throws MenuInvException {
        if (plugin == null) {
            throw new MenuInvException("plugin является нулевым!");
        }

        PluginTemplatePagesImpl tmp = this.menu.get(plugin.getClass());
        if (tmp != null) {
            throw new MenuInvException("Страницы плагина '" + plugin.getName() + "' уже зарегистрированы!");
        }

        PluginTemplatePagesImpl pages = new PluginTemplatePagesImpl(plugin);

        this.menu.put(plugin.getClass(), pages);

        return pages;
    }

    /**
     * Отменяет регистрацию плагина, удаляя все страницы которые были им добавлены.
     * При удалении страниц, игроки у которых эти страницы остались открыты, будут принудительно закрыты.
     * @param plugin Плагин, который зарегистрировал страницы
     */
    public void unregisterPages(JavaPlugin plugin) throws MenuInvException {
        if (plugin == null) {
            throw new MenuInvException("plugin является нулевым!");
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
                    //TODO Я не знаю, плохо ли принудительно закрывать инвентарь.. Возможно смогут стырить в какой то момент вещь
                    page.close(true);
                }
            }
        }
    }

    /**
     * Открывает меню со страницей плагина
     * @param plugin Плагин, который зарегистрировал страницу
     * @param page Страница, которая была зарегистрирована плагином
     * @param player Игрок, которому надо открыть страницу
     */
    public void openMenu(JavaPlugin plugin, String page, Player player) throws MenuInvException {
        openMenu(plugin, page, player, null);
    }

    /**
     * Открывает меню со страницей плагина
     * @param plugin Плагин, который зарегистрировал страницу
     * @param page Страница, которая была зарегистрирована плагином
     * @param player Игрок, которому надо открыть страницу
     * @param session Сессия, используемся для хранения внутренних параметров между страницами
     */
    public void openMenu(JavaPlugin plugin, String page, Player player, InventorySession session) throws MenuInvException {
        if (plugin == null || page == null || player == null) {
            throw new MenuInvException("Указанные аргументы являются нулевыми!");
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
                InventoryPageImpl newPage = templatePage.createNewInventoryPage(player, session);

                newPage.update(true);

                player.openInventory(newPage.getInventory());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
