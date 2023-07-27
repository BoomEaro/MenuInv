package ru.boomearo.menuinv.test;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import ru.boomearo.menuinv.MenuInv;
import ru.boomearo.menuinv.api.*;
import ru.boomearo.menuinv.api.frames.iteration.InverseIterationHandler;
import ru.boomearo.menuinv.api.scrolls.DefaultScrollHandlerFactory;
import ru.boomearo.menuinv.api.session.InventorySessionImpl;
import ru.boomearo.menuinv.api.frames.inventory.PagedItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Тестовый класс, используется только для отладки
 */
public class TestMenu {

    private static final List<Material> allMaterials;

    static {
        List<Material> tmp = new ArrayList<>();
        for (Material mat : Material.values()) {
            if (!mat.isBlock()) {
                tmp.add(mat);
            }
        }
        allMaterials = tmp;
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
                    .setInventoryCreationHandler((inventoryPage) -> {
                        PagedItems piTest = inventoryPage.getListedIconsItems("test");
                        PagedItems piTest2 = inventoryPage.getListedIconsItems("test2");

                        return "Test: " + piTest.getCurrentPage() + "/" + piTest.getMaxPage() + " ||| " + piTest2.getCurrentPage() + "/" + piTest2.getMaxPage();
                    })
                    .setInventoryReopenHandler((inventoryPage, force) -> {
                        return false;
                        //PagedItems piTest = page.getListedIconsItems("test");
                        //PagedItems piTest2 = page.getListedIconsItems("test2");

                        //return piTest.hasChanges() || piTest2.hasChanges();
                    })
                    .setItem(1, () -> new IconHandler() {

                        @Override
                        public void onClick(InventoryPage page, Player player, ClickType type) {
                            Menu.open(MenuPage.TEST2, player, page.getSession());
                        }

                        @Override
                        public ItemStack onUpdate(InventoryPage consume, Player player) {
                            return new ItemStack(Material.STONE, 1);
                        }
                    })
                    .setItem(3, () -> new IconHandler() {

                        @Override
                        public void onClick(InventoryPage page, Player player, ClickType type) {
                            TestSession ts = (TestSession) page.getSession();

                            List<ItemStack> items = ts.getItems();
                            if (items.isEmpty()) {
                                return;
                            }


                            items.remove(items.size() - 1);

                            page.setNeedUpdate();
                        }

                        @Override
                        public ItemStack onUpdate(InventoryPage consume, Player player) {
                            return new ItemStack(Material.REDSTONE_ORE, 1);
                        }
                    })
                    .setItem(4, () -> new IconHandler() {

                        @Override
                        public void onClick(InventoryPage page, Player player, ClickType type) {
                            TestSession ts = (TestSession) page.getSession();

                            ts.getItems().add(new ItemStack(Material.DIAMOND, 1));

                            page.setNeedUpdate();
                        }

                        @Override
                        public ItemStack onUpdate(InventoryPage consume, Player player) {
                            return new ItemStack(Material.EMERALD_BLOCK, 1);
                        }
                    })
                    .setPagedItems("test", 0, 2, 3, 3, () -> new FramedIconsHandler() {

                        @Override
                        public List<IconHandler> onUpdate(InventoryPage consume, Player player) {
                            List<IconHandler> tmp = new ArrayList<>();
                            for (int i = 1; i <= new Random().nextInt(5000); i++) {
                                int t = i;
                                tmp.add(new IconHandler() {

                                    @Override
                                    public void onClick(InventoryPage page, Player player, ClickType type) {
                                        player.sendMessage("Вот так вот алмазы: " + t);
                                    }

                                    @Override
                                    public ItemStack onUpdate(InventoryPage consume, Player player) {
                                        return new ItemStack(Material.DIAMOND, t);
                                    }

                                });
                            }
                            return tmp;
                        }

                        @Override
                        public long getUpdateTime() {
                            return 1500;
                        }

                    }, new InverseIterationHandler())
                    .setPagedItems("test2", 6, 2, 3, 3, () -> (pageInv, player) -> {
                        List<IconHandler> tmp = new ArrayList<>();
                        for (int i = 1; i <= new Random().nextInt(20); i++) {
                            int t = i;
                            tmp.add(new IconHandler() {

                                @Override
                                public void onClick(InventoryPage page, Player player, ClickType type) {
                                    player.sendMessage("Вот так вот редстоун: " + t);
                                }

                                @Override
                                public ItemStack onUpdate(InventoryPage consume, Player player) {
                                    return new ItemStack(Material.REDSTONE_ORE, t);
                                }

                            });
                        }
                        return tmp;
                    })
                    .setScrollItem(7, "test", PagedItems.ScrollType.PREVIOUSLY, new DefaultScrollHandlerFactory(PagedItems.ScrollType.PREVIOUSLY))
                    .setScrollItem(8, "test", PagedItems.ScrollType.NEXT, new DefaultScrollHandlerFactory(PagedItems.ScrollType.NEXT))
                    .setBackground(() -> new IconHandler() {

                        @Override
                        public void onClick(InventoryPage page, Player player, ClickType click) {

                        }

                        @Override
                        public ItemStack onUpdate(InventoryPage consume, Player player) {
                            return new ItemStack(Material.COOKIE, 1);
                        }

                        @Override
                        public boolean shouldUpdate() {
                            return false;
                        }

                    });
        }
        {
            Menu.registerPages(menuInv)
                    .createTemplatePage(MenuPage.TEST2)
                    .setMenuType(MenuType.WORKBENCH)
                    .setInventoryCreationHandler((inventoryPage) -> "Привет2")
                    .setItem(9, () -> new IconHandler() {

                        @Override
                        public void onClick(InventoryPage page, Player player, ClickType type) {
                            Menu.open(MenuPage.TEST, player, page.getSession());
                        }

                        @Override
                        public ItemStack onUpdate(InventoryPage consume, Player player) {
                            ItemStack item = new ItemStack(allMaterials.get(ThreadLocalRandom.current().nextInt(allMaterials.size())), 1);
                            ItemMeta meta = item.getItemMeta();
                            meta.setDisplayName("Привет тест!");
                            meta.setLore(Arrays.asList("Time: " + System.currentTimeMillis()));
                            item.setItemMeta(meta);
                            return item;
                        }

                        @Override
                        public long getUpdateTime() {
                            return 0;
                        }

                    })
                    .setItem(0, () -> new IconHandler() {

                        @Override
                        public void onClick(InventoryPage page, Player player, ClickType type) {

                        }

                        @Override
                        public ItemStack onUpdate(InventoryPage consume, Player player) {
                            ItemStack item = new ItemStack(allMaterials.get(ThreadLocalRandom.current().nextInt(allMaterials.size())), 1);
                            ItemMeta meta = item.getItemMeta();
                            meta.setDisplayName("Привет тест2!");
                            meta.setLore(Arrays.asList("Time2: " + System.currentTimeMillis()));
                            item.setItemMeta(meta);
                            return item;
                        }

                        @Override
                        public long getUpdateTime() {
                            return 500;
                        }
                    })
                    .setItem(1, () -> new IconHandler() {

                        @Override
                        public void onClick(InventoryPage page, Player player, ClickType type) {
                            page.close();
                        }

                        @Override
                        public ItemStack onUpdate(InventoryPage consume, Player player) {
                            ItemStack item = new ItemStack(Material.BARRIER, 1);
                            ItemMeta meta = item.getItemMeta();
                            meta.setDisplayName("Закрыть");
                            meta.setLore(Arrays.asList("Агагага22"));
                            item.setItemMeta(meta);
                            return item;
                        }
                    })
                    .setBackground(() -> new IconHandler() {

                        @Override
                        public void onClick(InventoryPage page, Player player, ClickType click) {

                        }

                        @Override
                        public ItemStack onUpdate(InventoryPage consume, Player player) {
                            return new ItemStack(Material.COOKIE, 1);
                        }

                        @Override
                        public boolean shouldUpdate() {
                            return false;
                        }

                    });
        }
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
