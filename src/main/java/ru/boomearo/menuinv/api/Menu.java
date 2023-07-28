package ru.boomearo.menuinv.api;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;
import ru.boomearo.menuinv.MenuInv;
import ru.boomearo.menuinv.api.icon.IconBuilder;
import ru.boomearo.menuinv.api.session.ConfirmData;
import ru.boomearo.menuinv.api.session.InventorySession;
import ru.boomearo.menuinv.api.session.InventorySessionImpl;

import java.util.HashMap;
import java.util.Map;

public class Menu {

    private static final Map<Class<? extends JavaPlugin>, PluginTemplatePagesImpl> MENU_BY_PLUGIN = new HashMap<>();

    public static void initMenu(MenuInv menuInv) {
        registerPages(menuInv)
                .createTemplatePage(DefaultMenuPage.CONFIRM)
                .setMenuType(MenuType.HOPPER)
                .setInventoryTitle((inventoryPage) -> {
                    InventorySession session = inventoryPage.getSession();
                    if (session == null) {
                        return null;
                    }

                    ConfirmData data = session.getConfirmData();

                    if (data == null) {
                        return null;
                    }

                    return data.getInventoryName(session);
                })
                .setItem(0, new IconBuilder()
                        .setIconClick((inventoryPage, player, clickType) -> {
                            InventorySession session = inventoryPage.getSession();

                            if (session == null) {
                                return;
                            }

                            ConfirmData confirm = session.getConfirmData();

                            if (confirm == null) {
                                return;
                            }

                            confirm.executeCancel(inventoryPage);
                        })
                        .setIconUpdate((inventoryPage, player) -> {
                            InventorySession session = inventoryPage.getSession();

                            if (session == null) {
                                return null;
                            }

                            ConfirmData confirm = session.getConfirmData();

                            if (confirm == null) {
                                return null;
                            }

                            return confirm.getCancelItem(inventoryPage);
                        }))
                .setItem(4, new IconBuilder()
                        .setIconClick((inventoryPage, player, clickType) -> {
                            InventorySession session = inventoryPage.getSession();

                            if (session == null) {
                                return;
                            }

                            ConfirmData confirm = session.getConfirmData();

                            if (confirm == null) {
                                return;
                            }

                            confirm.executeConfirm(inventoryPage);
                        })
                        .setIconUpdate((inventoryPage, player) -> {
                            InventorySession session = inventoryPage.getSession();

                            if (session == null) {
                                return null;
                            }

                            ConfirmData confirm = session.getConfirmData();

                            if (confirm == null) {
                                return null;
                            }

                            return confirm.getConfirmItem(inventoryPage);
                        }));
    }

    /**
     * Регистрирует плагин для создания шаблона страниц
     *
     * @param plugin Плагин, который регистрирует страницы
     * @return Шаблон страниц плагина
     */
    public static PluginTemplatePages registerPages(JavaPlugin plugin) {
        Preconditions.checkArgument(plugin != null, "plugin is null!");

        PluginTemplatePagesImpl tmp = MENU_BY_PLUGIN.get(plugin.getClass());
        if (tmp != null) {
            return tmp;
        }

        PluginTemplatePagesImpl pages = new PluginTemplatePagesImpl(plugin);

        MENU_BY_PLUGIN.put(plugin.getClass(), pages);
        return pages;
    }

    /**
     * Отменяет регистрацию плагина, удаляя все страницы которые были им добавлены.
     * При удалении страниц, игроки у которых эти страницы остались открыты, будут принудительно закрыты.
     *
     * @param plugin Плагин, который зарегистрировал страницы
     */
    public static void unregisterPages(JavaPlugin plugin) {
        Preconditions.checkArgument(plugin != null, "plugin is null!");

        PluginTemplatePagesImpl tmp = MENU_BY_PLUGIN.get(plugin.getClass());
        if (tmp == null) {
            throw new IllegalStateException("Plugin '" + plugin.getName() + "' not registered pages yet!");
        }

        MENU_BY_PLUGIN.remove(plugin.getClass());

        for (Player player : Bukkit.getOnlinePlayers()) {
            InventoryHolder holder = player.getOpenInventory().getTopInventory().getHolder();
            if (holder instanceof MenuInventoryHolder) {
                MenuInventoryHolder mih = (MenuInventoryHolder) holder;

                InventoryPageImpl page = mih.getPage();

                //Не будем сравнивать ссылки, потому что думаю, что может быть такая ситуация когда плагин не выгрузился.
                //Поэтому просто сравниваем имена, а потом закрываем этим игрокам инвентари
                if (page.getTemplatePage().getPluginTemplatePages().getPlugin().getName().equals(plugin.getName())) {
                    page.close(true);
                }
            }
        }
    }

    /**
     * Открывает меню со страницей плагина
     *
     * @param pluginPage Страница, которая была зарегистрирована плагином
     * @param player     Игрок, которому надо открыть страницу
     */
    public static void open(PluginPage pluginPage, Player player) {
        open(pluginPage, player, null);
    }

    /**
     * Открывает меню со страницей плагина
     *
     * @param pluginPage Страница, которая была зарегистрирована плагином
     * @param player     Игрок, которому надо открыть страницу
     * @param session    Сессия, используемся для хранения внутренних параметров между страницами
     */
    public static void open(PluginPage pluginPage, Player player, InventorySession session) {
        Preconditions.checkArgument(pluginPage != null, "pluginPage is null!");
        Preconditions.checkArgument(player != null, "player is null!");

        JavaPlugin plugin = pluginPage.getPlugin();
        String page = pluginPage.getPage();

        PluginTemplatePagesImpl pp = MENU_BY_PLUGIN.get(plugin.getClass());
        if (pp == null) {
            throw new IllegalStateException("Plugin '" + plugin.getName() + "' not registered pages yet!");
        }

        TemplatePageImpl templatePage = pp.getTemplatePage(page);
        if (templatePage == null) {
            throw new IllegalStateException("Plugin '" + plugin.getName() + "' not registered '" + page + "' page yet!");
        }

        if (session == null) {
            session = new InventorySessionImpl();
        }

        InventorySession finalSession = session;

        Bukkit.getScheduler().runTask(MenuInv.getInstance(), () -> {
            try {
                finalSession.setCurrentPage(pluginPage);

                InventoryPageImpl newPage = templatePage.createNewInventoryPage(player, finalSession);

                newPage.update(true);

                player.openInventory(newPage.getInventory());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
    }

}