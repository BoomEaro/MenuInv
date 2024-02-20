package ru.boomearo.menuinv.api;

import com.google.common.base.Preconditions;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import ru.boomearo.menuinv.api.session.InventorySession;
import ru.boomearo.menuinv.api.session.InventorySessionImpl;
import ru.boomearo.menuinv.listeners.InventoryListener;
import ru.boomearo.menuinv.task.MenuUpdaterTask;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class Menu {

    private static final Map<Class<? extends Plugin>, PluginTemplatePagesImpl> MENU_BY_PLUGIN = new HashMap<>();

    private static Plugin plugin = null;
    private static BukkitTask updaterTask = null;
    private static InventoryListener inventoryListener = null;

    public static void initMenu(Plugin plugin) {
        if (Menu.plugin != null) {
            throw new IllegalStateException("API is already initialized by " + Menu.plugin.getName());
        }

        Menu.plugin = plugin;

        updaterTask = new MenuUpdaterTask().runTaskTimer(plugin, 1, 1);

        inventoryListener = new InventoryListener();

        plugin.getServer().getPluginManager().registerEvents(inventoryListener, plugin);
    }

    public static void unloadMenu(Plugin plugin) {
        if (Menu.plugin != plugin) {
            throw new IllegalStateException("API is already unloaded or provided plugin is incorrect");
        }

        if (inventoryListener != null) {
            HandlerList.unregisterAll(inventoryListener);
            inventoryListener = null;
        }

        if (updaterTask != null) {
            updaterTask.cancel();
            updaterTask = null;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            InventoryHolder holder = player.getOpenInventory().getTopInventory().getHolder();
            if (holder instanceof MenuInventoryHolder) {
                MenuInventoryHolder mih = (MenuInventoryHolder) holder;
                InventoryPageImpl page = mih.getPage();

                page.close(true);
            }
        }
    }

    public static PluginTemplatePages registerPages(Plugin plugin) {
        Preconditions.checkArgument(plugin != null, "plugin is null!");

        PluginTemplatePagesImpl tmp = MENU_BY_PLUGIN.get(plugin.getClass());
        if (tmp != null) {
            return tmp;
        }

        PluginTemplatePagesImpl pages = new PluginTemplatePagesImpl(Menu.plugin, plugin);

        MENU_BY_PLUGIN.put(plugin.getClass(), pages);

        plugin.getLogger().info("Registered PluginTemplatePages for " + plugin.getName());
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

        plugin.getLogger().info("Unregistered PluginTemplatePages for " + plugin.getName());
    }

    public static InventoryPage open(PluginPage pluginPage, Player player) {
        return open(pluginPage, player, null);
    }

    public static InventoryPage open(PluginPage pluginPage, Player player, InventorySession session) {
        InventoryPage inventoryPage = create(pluginPage, player, session);

        Bukkit.getScheduler().runTask(plugin, () -> {
            try {
                player.openInventory(inventoryPage.getInventory());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });

        return inventoryPage;
    }

    public static InventoryPage openNow(PluginPage pluginPage, Player player) {
        return openNow(pluginPage, player, null);
    }

    public static InventoryPage openNow(PluginPage pluginPage, Player player, InventorySession session) {
        InventoryPage inventoryPage = create(pluginPage, player, session);

        player.openInventory(inventoryPage.getInventory());
        return inventoryPage;
    }

    public static InventoryPage create(PluginPage pluginPage, Player player) {
        return create(pluginPage, player, null);
    }

    public static InventoryPage create(PluginPage pluginPage, Player player, InventorySession session) {
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

        return newPage;
    }

}
