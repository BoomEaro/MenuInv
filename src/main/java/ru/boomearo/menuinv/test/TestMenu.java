package ru.boomearo.menuinv.test;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import ru.boomearo.menuinv.MenuInv;
import ru.boomearo.menuinv.api.*;
import ru.boomearo.menuinv.api.frames.PagedIconsBuilder;
import ru.boomearo.menuinv.api.icon.IconBuilder;
import ru.boomearo.menuinv.api.icon.IconHandler;
import ru.boomearo.menuinv.api.icon.scrolls.ScrollIconBuilder;
import ru.boomearo.menuinv.api.icon.scrolls.ScrollType;
import ru.boomearo.menuinv.api.frames.iteration.InverseIterationHandler;
import ru.boomearo.menuinv.api.session.InventorySessionImpl;
import ru.boomearo.menuinv.api.frames.PagedIcons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Just a test menu for debug
 */
public class TestMenu {

    private static final List<Material> MATERIALS;

    static {
        List<Material> tmp = new ArrayList<>();
        for (Material mat : Material.values()) {
            if (!mat.isBlock()) {
                tmp.add(mat);
            }
        }
        MATERIALS = tmp;
    }


    public static void setupTest(MenuInv menuInv) {
        if (menuInv.getConfig().getBoolean("debug")) {
            menuInv.getLogger().warning("Debug mode activated!");

            setupMenu(menuInv);
            menuInv.getServer().getPluginManager().registerEvents(new TestListener(), menuInv);
        }
    }

    private static void setupMenu(MenuInv menuInv) {
        {
            Menu.registerPages(menuInv)
                    .createTemplatePage(MenuPage.MAIN)
                    .setMenuType(MenuType.CHEST_9X6)
                    .setInventoryTitle((inventoryPage) -> {
                        PagedIcons piTest = inventoryPage.getListedIconsItems("test");
                        PagedIcons piTest2 = inventoryPage.getListedIconsItems("test2");

                        return "Test: " + piTest.getCurrentPage() + "/" + piTest.getMaxPage() + " ||| " + piTest2.getCurrentPage() + "/" + piTest2.getMaxPage();
                    })
                    .setIcon(1, new IconBuilder()
                            .setIconClick((inventoryPage, player, type) -> Menu.open(MenuPage.OTHER, player, inventoryPage.getSession()))
                            .setIconUpdate((consume, player) -> new ItemStack(Material.STONE, 1)))

                    .setIcon(3, new IconBuilder()
                            .setIconClick((inventoryPage, player, type) -> {
                                TestSession ts = (TestSession) inventoryPage.getSession();

                                List<ItemStack> items = ts.getItems();
                                if (items.isEmpty()) {
                                    return;
                                }

                                items.remove(items.size() - 1);

                                inventoryPage.setNeedUpdate();
                            })
                            .setIconUpdate((inventoryPage, player) -> new ItemStack(Material.REDSTONE_ORE, 1)))

                    .setIcon(4, new IconBuilder()
                            .setIconClick((inventoryPage, player, type) -> {
                                TestSession ts = (TestSession) inventoryPage.getSession();

                                ts.getItems().add(new ItemStack(Material.DIAMOND, 1));

                                inventoryPage.setNeedUpdate();
                            })
                            .setIconUpdate((inventoryPage, player) -> new ItemStack(Material.EMERALD_BLOCK, 1)))

                    .setPagedIcons("test", 0, 2, 3, 3, new PagedIconsBuilder()
                            .setPagedItemsUpdate((inventoryPage, player) -> {
                                List<IconHandler> tmp = new ArrayList<>();
                                for (int i = 1; i <= new Random().nextInt(5000); i++) {
                                    int t = i;
                                    tmp.add(new IconBuilder()
                                            .setIconClick((inventoryPage2, player2, type) -> player2.sendMessage("Diamond: " + t))
                                            .setIconUpdate((inventoryPage2, player2) -> new ItemStack(Material.DIAMOND, t))
                                            .build()
                                            .create());
                                }
                                return tmp;
                            })
                            .setIconUpdateDelay((inventoryPage) -> 250)
                            .setFrameIterationHandler(new InverseIterationHandler()))

                    .setPagedIcons("test2", 6, 2, 3, 3, new PagedIconsBuilder()
                            .setPagedItemsUpdate((inventoryPage, player) -> {
                                List<IconHandler> tmp = new ArrayList<>();
                                for (int i = 1; i <= new Random().nextInt(20); i++) {
                                    int t = i;
                                    tmp.add(new IconBuilder()
                                            .setIconClick((inventoryPage2, player2, clickType) -> player2.sendMessage("Resdstone: " + t))
                                            .setIconUpdate((inventoryPage2, player2) -> new ItemStack(Material.REDSTONE_ORE, t))
                                            .build()
                                            .create());
                                }
                                return tmp;
                            }))

                    .setStructure(
                            "# # # # # # # # #",
                            "# . . . . . < > #",
                            "# . . . ? . . . #",
                            "# . . . ? . . . #",
                            "# . . . . . . . #",
                            "# # # # # # # # #")
                    .setIngredient('#', new IconBuilder()
                            .setIconUpdate((inventoryPage, player) -> new ItemStack(Material.TNT, 1)))
                    .setIngredient('?', new IconBuilder()
                            .setIconUpdate((inventoryPage, player) -> new ItemStack(Material.CACTUS, 1))
                            .setIconClick((inventoryPage, player, clickType) -> player.sendMessage("It is a cactus?")))

                    .setIngredient('<', new ScrollIconBuilder()
                            .setName("test")
                            .setScrollType(ScrollType.PREVIOUSLY)
                            .setScrollVisibleUpdate((inventoryPage, player, scrollType, currentPage, maxPage) -> createScrollItems(scrollType, currentPage, maxPage)))

                    .setIngredient('>', new ScrollIconBuilder()
                            .setName("test")
                            .setScrollType(ScrollType.NEXT)
                            .setScrollVisibleUpdate((inventoryPage, player, scrollType, currentPage, maxPage) -> createScrollItems(scrollType, currentPage, maxPage)))

                    .setImmutableBackground(new IconBuilder()
                            .setIconUpdate((inventoryPage, player) -> new ItemStack(Material.COOKIE, 1)));
        }
        {
            Menu.registerPages(menuInv)
                    .createTemplatePage(MenuPage.OTHER)
                    .setMenuType(MenuType.WORKBENCH)
                    .setInventoryTitle((inventoryPage) -> "Hello2")
                    .setIcon(9, new IconBuilder()
                            .setIconClick((inventoryPage, player, click) -> Menu.open(MenuPage.MAIN, player, inventoryPage.getSession()))
                            .setIconUpdate((inventoryPage, player) -> {
                                ItemStack item = new ItemStack(MATERIALS.get(ThreadLocalRandom.current().nextInt(MATERIALS.size())), 1);
                                ItemMeta meta = item.getItemMeta();
                                meta.setDisplayName("Hello@");
                                meta.setLore(Arrays.asList("Time: " + System.currentTimeMillis()));
                                item.setItemMeta(meta);
                                return item;
                            })
                            .setIconUpdateDelay((inventortPage) -> 0))

                    .setIcon(0, new IconBuilder()
                            .setIconUpdate((inventoryPage, player) -> {
                                ItemStack item = new ItemStack(MATERIALS.get(ThreadLocalRandom.current().nextInt(MATERIALS.size())), 1);
                                ItemMeta meta = item.getItemMeta();
                                meta.setDisplayName("Hello!");
                                meta.setLore(Arrays.asList("Time2: " + System.currentTimeMillis()));
                                item.setItemMeta(meta);
                                return item;
                            })
                            .setIconUpdateDelay((inventoryPage) -> 500))

                    .setIcon(1, new IconBuilder()
                            .setIconClick((inventoryPage, player, clickType) -> inventoryPage.close())
                            .setIconUpdate((inventoryPage, player) -> {
                                ItemStack item = new ItemStack(Material.BARRIER, 1);
                                ItemMeta meta = item.getItemMeta();
                                meta.setDisplayName("Close");
                                meta.setLore(Arrays.asList("UwU"));
                                item.setItemMeta(meta);
                                return item;
                            }))

                    .setImmutableBackground(new IconBuilder()
                            .setIconUpdate((inventoryPage, player) -> new ItemStack(Material.COOKIE, 1)));
        }
    }

    private static ItemStack createScrollItems(ScrollType scrollType, int currentPage, int maxPage) {
        int nextPage = scrollType.getNextPage(currentPage);

        int amount = nextPage;
        if (amount <= 0) {
            amount = 1;
        }
        if (amount > 64) {
            amount = 64;
        }
        ItemStack item = new ItemStack(Material.PAPER, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6" + scrollType.name() + " §7[§c" + nextPage + "§7/§c" + maxPage + "§7]");
        meta.addItemFlags(ItemFlag.values());
        item.setItemMeta(meta);
        return item;
    }

    private static class TestListener implements Listener {

        @EventHandler
        public void onPlayerInteractEvent(PlayerInteractEvent e) {
            Player pl = e.getPlayer();

            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                e.setUseInteractedBlock(Event.Result.DENY);
                e.setUseItemInHand(Event.Result.DENY);

                Menu.open(MenuPage.MAIN, pl, new TestSession());
            }
        }
    }

    private static class TestSession extends InventorySessionImpl {

        private final List<ItemStack> items = new ArrayList<>();

        public List<ItemStack> getItems() {
            return this.items;
        }

    }

    private static enum MenuPage implements PluginPage {

        MAIN(MenuInv.getInstance(), "main"),
        OTHER(MenuInv.getInstance(), "other");

        private final Plugin javaPlugin;
        private final String page;

        MenuPage(Plugin javaPlugin, String page) {
            this.javaPlugin = javaPlugin;
            this.page = page;
        }

        @Override
        public Plugin getPlugin() {
            return this.javaPlugin;
        }

        @Override
        public String getPage() {
            return this.page;
        }
    }
}
