package ru.boomearo.menuinv.api;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import ru.boomearo.menuinv.MenuInv;
import ru.boomearo.menuinv.api.icon.IconBuilder;
import ru.boomearo.menuinv.api.session.ConfirmData;
import ru.boomearo.menuinv.api.session.InventorySession;
import ru.boomearo.menuinv.api.session.InventorySessionImpl;

import java.util.HashMap;
import java.util.Map;

public class Menu {

    private static final Map<Class<? extends Plugin>, PluginTemplatePagesImpl> MENU_BY_PLUGIN = new HashMap<>();

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
                .setIcon(0, new IconBuilder()
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
                .setIcon(4, new IconBuilder()
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

    public static PluginTemplatePages registerPages(Plugin plugin) {
        Preconditions.checkArgument(plugin != null, "plugin is null!");

        PluginTemplatePagesImpl tmp = MENU_BY_PLUGIN.get(plugin.getClass());
        if (tmp != null) {
            return tmp;
        }

        PluginTemplatePagesImpl pages = new PluginTemplatePagesImpl(plugin);

        MENU_BY_PLUGIN.put(plugin.getClass(), pages);
        return pages;
    }

    public static void unregisterPages(Plugin plugin) {
        Preconditions.checkArgument(plugin != null, "plugin is null!");

        PluginTemplatePagesImpl tmp = MENU_BY_PLUGIN.get(plugin.getClass());
        if (tmp == null) {
            return;
        }

        MENU_BY_PLUGIN.remove(plugin.getClass());

        for (Player player : Bukkit.getOnlinePlayers()) {
            InventoryHolder holder = player.getOpenInventory().getTopInventory().getHolder();
            if (holder instanceof MenuInventoryHolder) {
                MenuInventoryHolder mih = (MenuInventoryHolder) holder;

                InventoryPageImpl page = mih.getPage();

                // We will not compare references, because I think that there may be such a situation when the plugin has not been unloaded.
                // Therefore, we simply compare the names, and then we close the inventories for these players

                if (page.getTemplatePage().getPluginTemplatePages().getPlugin().getName().equals(plugin.getName())) {
                    page.close(true);
                }
            }
        }
    }

    public static void open(PluginPage pluginPage, Player player) {
        open(pluginPage, player, null);
    }

    public static void open(PluginPage pluginPage, Player player, InventorySession session) {
        Inventory inventory = create(pluginPage, player, session);

        Bukkit.getScheduler().runTask(MenuInv.getInstance(), () -> {
            try {
                player.openInventory(inventory);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
    }

    public static void openNow(PluginPage pluginPage, Player player) {
        openNow(pluginPage, player, null);
    }

    public static void openNow(PluginPage pluginPage, Player player, InventorySession session) {
        Inventory inventory = create(pluginPage, player, session);

        player.openInventory(inventory);
    }

    public static Inventory create(PluginPage pluginPage, Player player) {
        return create(pluginPage, player, null);
    }

    public static Inventory create(PluginPage pluginPage, Player player, InventorySession session) {
        Preconditions.checkArgument(pluginPage != null, "pluginPage is null!");
        Preconditions.checkArgument(player != null, "player is null!");

        Plugin plugin = pluginPage.getPlugin();
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

        session.setCurrentPage(pluginPage);

        InventoryPageImpl newPage = templatePage.createNewInventoryPage(player, session);

        newPage.updateOnCreate();

        return newPage.getInventory();
    }

}
