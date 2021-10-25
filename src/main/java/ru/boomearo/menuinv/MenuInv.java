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

        try {
            registerOwnMenu(this);
        }
        catch (MenuInvException e) {
            e.printStackTrace();
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

        this.getLogger().info("Плагин '" + plugin.getName() + "' успешно зарегистрировал страницы!");

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

        this.getLogger().info("Плагин '" + plugin.getName() + "' успешно отменил регистрацию страниц!");
    }

    /**
     * Открывает меню со страницей плагина
     * @param pageData Страница, которая была зарегистрирована плагином
     * @param player Игрок, которому надо открыть страницу
     */
    public void openMenu(PageData pageData, Player player) throws MenuInvException {
        openMenu(pageData, player, null);
    }

    /**
     * Открывает меню со страницей плагина
     * @param pageData Страница, которая была зарегистрирована плагином
     * @param player Игрок, которому надо открыть страницу
     * @param session Сессия, используемся для хранения внутренних параметров между страницами
     */
    public void openMenu(PageData pageData, Player player, InventorySession session) throws MenuInvException {
        if (pageData == null || player == null) {
            throw new MenuInvException("Указанные аргументы являются нулевыми!");
        }

        JavaPlugin plugin = pageData.getPlugin();
        String page = pageData.getPage();

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
                if (session != null) {
                    session.setCurrentPage(pageData);
                }

                InventoryPageImpl newPage = templatePage.createNewInventoryPage(player, session);

                newPage.update(true);

                player.openInventory(newPage.getInventory());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    //Регистрируем своё личное меню для остальных плагинов
    private static void registerOwnMenu(MenuInv menuInv) throws MenuInvException {
        PluginTemplatePages pages = menuInv.registerPages(menuInv);

        initConfirmMenu(pages);
    }

    //Страница подтверждения, для использования другими плагинами, чтобы не копировать код много раз
    private static void initConfirmMenu(PluginTemplatePages pages) throws MenuInvException {
        TemplatePage page = pages.createTemplatePage("confirm", InvType.HOPPER, (session) -> {
            if (session == null) {
                return null;
            }

            ConfirmData data = session.getConfirmData();

            if (data == null) {
                return null;
            }

            return data.getInventoryName();
        });

        //Кнопка отмены
        page.addItem(0, () -> new IconHandler() {

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

                confirm.executeCancel();
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

                return confirm.getCancelItem();
            }

        });

        //Кнопка подтверждения
        page.addItem(4, () -> new IconHandler() {

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

                confirm.executeConfirm();
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

                return confirm.getConfirmItem();
            }

        });
    }
}
