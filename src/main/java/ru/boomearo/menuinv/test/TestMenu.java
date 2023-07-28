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
import org.bukkit.plugin.java.JavaPlugin;

import ru.boomearo.menuinv.MenuInv;
import ru.boomearo.menuinv.api.*;
import ru.boomearo.menuinv.api.frames.PagedItemsBuilder;
import ru.boomearo.menuinv.api.icon.IconBuilder;
import ru.boomearo.menuinv.api.icon.IconHandler;
import ru.boomearo.menuinv.api.icon.scrolls.ScrollIconBuilder;
import ru.boomearo.menuinv.api.icon.scrolls.ScrollType;
import ru.boomearo.menuinv.api.frames.iteration.InverseIterationHandler;
import ru.boomearo.menuinv.api.session.InventorySessionImpl;
import ru.boomearo.menuinv.api.frames.PagedItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Тестовый класс, используется только для отладки
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
            menuInv.getLogger().warning("Активирован режим дебага!");

            setupMenu(menuInv);
            menuInv.getServer().getPluginManager().registerEvents(new TestListener(), menuInv);
        }
    }

    private static void setupMenu(MenuInv menuInv) {
        {
            Menu.registerPages(menuInv)
                    .createTemplatePage(MenuPage.TEST)
                    .setMenuType(MenuType.CHEST_9X6)
                    .setInventoryTitle((inventoryPage) -> {
                        PagedItems piTest = inventoryPage.getListedIconsItems("test");
                        PagedItems piTest2 = inventoryPage.getListedIconsItems("test2");

                        return "Test: " + piTest.getCurrentPage() + "/" + piTest.getMaxPage() + " ||| " + piTest2.getCurrentPage() + "/" + piTest2.getMaxPage();
                    })
                    .setItem(1, new IconBuilder()
                            .setIconClick((inventoryPage, player, type) -> Menu.open(MenuPage.TEST2, player, inventoryPage.getSession()))
                            .setIconUpdate((consume, player) -> new ItemStack(Material.STONE, 1)))

                    .setItem(3, new IconBuilder()
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

                    .setItem(4, new IconBuilder()
                            .setIconClick((inventoryPage, player, type) -> {
                                TestSession ts = (TestSession) inventoryPage.getSession();

                                ts.getItems().add(new ItemStack(Material.DIAMOND, 1));

                                inventoryPage.setNeedUpdate();
                            })
                            .setIconUpdate((inventoryPage, player) -> new ItemStack(Material.EMERALD_BLOCK, 1)))

                    .setPagedItems("test", 0, 2, 3, 3, new PagedItemsBuilder()
                            .setPagedItemsUpdate((inventoryPage, player) -> {
                                List<IconHandler> tmp = new ArrayList<>();
                                for (int i = 1; i <= new Random().nextInt(5000); i++) {
                                    int t = i;
                                    tmp.add(new IconBuilder()
                                            .setIconClick((inventoryPage2, player2, type) -> player2.sendMessage("Вот так вот алмазы: " + t))
                                            .setIconUpdate((inventoryPage2, player2) -> new ItemStack(Material.DIAMOND, t))
                                            .build()
                                            .create());
                                }
                                return tmp;
                            })
                            .setIconUpdateDelay((inventoryPage) -> 250)
                            .setFrameIterationHandler(new InverseIterationHandler()))

                    .setPagedItems("test2", 6, 2, 3, 3, new PagedItemsBuilder()
                            .setPagedItemsUpdate((inventoryPage, player) -> {
                                List<IconHandler> tmp = new ArrayList<>();
                                for (int i = 1; i <= new Random().nextInt(20); i++) {
                                    int t = i;
                                    tmp.add(new IconBuilder()
                                            .setIconClick((inventoryPage2, player2, clickType) -> player2.sendMessage("Вот так вот редстоун: " + t))
                                            .setIconUpdate((inventoryPage2, player2) -> new ItemStack(Material.REDSTONE_ORE, t))
                                            .build()
                                            .create());
                                }
                                return tmp;
                            }))

                    .setScrollItem(7, "test", ScrollType.PREVIOUSLY, new ScrollIconBuilder()
                            .setScrollVisibleUpdate((inventoryPage, player, scrollType, currentPage, maxPage) -> createScrollItems(scrollType, currentPage, maxPage)))

                    .setScrollItem(8, "test", ScrollType.NEXT, new ScrollIconBuilder()
                            .setScrollVisibleUpdate((inventoryPage, player, scrollType, currentPage, maxPage) -> createScrollItems(scrollType, currentPage, maxPage)))

                    .setBackground(new IconBuilder()
                            .setIconUpdate((inventoryPage, player) -> new ItemStack(Material.COOKIE, 1))
                            .setIconUpdateCondition((inventoryPage) -> false))
                    .setStructure(
                            "# # # # # # # # #",
                            "# . . . . . . . #",
                            "# . . . ? . . . #",
                            "# . . . ? . . . #",
                            "# . . . . . . . #",
                            "# # # # # # # # #")
                    .setIngredient('#', new IconBuilder()
                            .setIconUpdate((inventoryPage, player) -> new ItemStack(Material.TNT, 1)))
                    .setIngredient('?', new IconBuilder()
                            .setIconUpdate((inventoryPage, player) -> new ItemStack(Material.CACTUS, 1))
                            .setIconClick((inventoryPage, player, clickType) -> player.sendMessage("Это кактус!")));
        }
        {
            Menu.registerPages(menuInv)
                    .createTemplatePage(MenuPage.TEST2)
                    .setMenuType(MenuType.WORKBENCH)
                    .setInventoryTitle((inventoryPage) -> "Привет2")
                    .setItem(9, new IconBuilder()
                            .setIconClick((inventoryPage, player, click) -> Menu.open(MenuPage.TEST, player, inventoryPage.getSession()))
                            .setIconUpdate((inventoryPage, player) -> {
                                ItemStack item = new ItemStack(MATERIALS.get(ThreadLocalRandom.current().nextInt(MATERIALS.size())), 1);
                                ItemMeta meta = item.getItemMeta();
                                meta.setDisplayName("Привет тест!");
                                meta.setLore(Arrays.asList("Time: " + System.currentTimeMillis()));
                                item.setItemMeta(meta);
                                return item;
                            })
                            .setIconUpdateDelay((inventortPage) -> 0))

                    .setItem(0, new IconBuilder()
                            .setIconUpdate((inventoryPage, player) -> {
                                ItemStack item = new ItemStack(MATERIALS.get(ThreadLocalRandom.current().nextInt(MATERIALS.size())), 1);
                                ItemMeta meta = item.getItemMeta();
                                meta.setDisplayName("Привет тест2!");
                                meta.setLore(Arrays.asList("Time2: " + System.currentTimeMillis()));
                                item.setItemMeta(meta);
                                return item;
                            })
                            .setIconUpdateDelay((inventoryPage) -> 500))

                    .setItem(1, new IconBuilder()
                            .setIconClick((inventoryPage, player, clickType) -> inventoryPage.close())
                            .setIconUpdate((inventoryPage, player) -> {
                                ItemStack item = new ItemStack(Material.BARRIER, 1);
                                ItemMeta meta = item.getItemMeta();
                                meta.setDisplayName("Закрыть");
                                meta.setLore(Arrays.asList("Агагага22"));
                                item.setItemMeta(meta);
                                return item;
                            }))

                    .setBackground(new IconBuilder()
                            .setIconUpdate((inventoryPage, player) -> new ItemStack(Material.COOKIE, 1))
                            .setIconUpdateCondition((inventoryPage) -> false));
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

                Menu.open(MenuPage.TEST, pl, new TestSession());
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

        TEST(MenuInv.getInstance(), "test"),
        TEST2(MenuInv.getInstance(), "test2");

        private final JavaPlugin javaPlugin;
        private final String page;

        MenuPage(JavaPlugin javaPlugin, String page) {
            this.javaPlugin = javaPlugin;
            this.page = page;
        }

        @Override
        public JavaPlugin getPlugin() {
            return this.javaPlugin;
        }

        @Override
        public String getPage() {
            return this.page;
        }
    }
}
