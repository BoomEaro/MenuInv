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
import ru.boomearo.menuinv.MenuInv;
import ru.boomearo.menuinv.api.*;
import ru.boomearo.menuinv.api.scrolls.DefaultScrollHandlerFactory;
import ru.boomearo.menuinv.exceptions.MenuInvException;
import ru.boomearo.menuinv.api.frames.inventory.PagedItems;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TestMenu {

    public static void setupTest() throws MenuInvException {
        setupMenu();
        registerEvents();
    }

    public static void setupMenu() throws MenuInvException {
        MenuInv inv = MenuInv.getInstance();
        PluginTemplatePages pages = inv.registerPages(inv);

        {
            TemplatePage page = pages.createTemplatePage("test", "Привет", InvType.CHEST_9X6);

            page.addButton(1, () -> new IconHandler() {

                @Override
                public void onClick(InventoryPage page, Player player, ClickType type) {
                    try {
                        inv.openMenu(inv, "test2", player, page.getSession());
                    }
                    catch (MenuInvException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public ItemStack onUpdate(InventoryPage consume, Player player) {
                    return new ItemStack(Material.BEDROCK, 1);
                }
            });

            page.addButton(3, () -> new IconHandler() {

                @Override
                public void onClick(InventoryPage page, Player player, ClickType type) {
                    TestSession ts = (TestSession) page.getSession();

                    List<ItemStack> items = ts.getItems();
                    if (items.isEmpty()) {
                        return;
                    }


                    items.remove(items.size() - 1);

                    page.update(true);
                }

                @Override
                public ItemStack onUpdate(InventoryPage consume, Player player) {
                    return new ItemStack(Material.REDSTONE_ORE, 1);
                }
            });

            page.addButton(4, () -> new IconHandler() {

                @Override
                public void onClick(InventoryPage page, Player player, ClickType type) {
                    TestSession ts = (TestSession) page.getSession();

                    ts.getItems().add(new ItemStack(Material.DIAMOND, 1));

                    page.update(true);
                }

                @Override
                public ItemStack onUpdate(InventoryPage consume, Player player) {
                    return new ItemStack(Material.EMERALD_BLOCK, 1);
                }
            });

            page.addPagedItems("test", 0, 2, 3, 3, () -> (pageInv, player) -> {
                TestSession ts = (TestSession) pageInv.getSession();

                List<IconHandler> tmp = new ArrayList<>();
                for (int i = 1; i <= new Random().nextInt(5000); i++) {
                    int t = i;
                    tmp.add(new IconHandler() {

                        @Override
                        public void onClick(InventoryPage page, Player player, ClickType type) {
                            player.sendMessage("Вот так вот: " + t);
                        }

                        @Override
                        public ItemStack onUpdate(InventoryPage consume, Player player) {
                            return new ItemStack(Material.DIAMOND, t);
                        }

                    });
                }
                return tmp;
            });

            page.addPagedItems("test2", 6, 2, 3, 3, () -> (pageInv, player) -> {
                TestSession ts = (TestSession) pageInv.getSession();

                List<IconHandler> tmp = new ArrayList<>();
                for (int i = 1; i <= new Random().nextInt(20); i++) {
                    int t = i;
                    tmp.add(new IconHandler() {

                        @Override
                        public void onClick(InventoryPage page, Player player, ClickType type) {
                            player.sendMessage("Вот так вот: " + t);
                        }

                        @Override
                        public ItemStack onUpdate(InventoryPage consume, Player player) {
                            return new ItemStack(Material.REDSTONE_ORE, t);
                        }

                    });
                }
                return tmp;
            });

            page.addScrollButton(7, "test", PagedItems.ScrollType.PREVIOUSLY, new DefaultScrollHandlerFactory(PagedItems.ScrollType.PREVIOUSLY));

            page.addScrollButton(8, "test", PagedItems.ScrollType.NEXT, new DefaultScrollHandlerFactory(PagedItems.ScrollType.NEXT));

            page.setBackground(() -> new IconHandler() {

                @Override
                public void onClick(InventoryPage page, Player player, ClickType click) {

                }

                @Override
                public ItemStack onUpdate(InventoryPage consume, Player player) {
                    return new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
                }

            });
        }
        {
            TemplatePage page = pages.createTemplatePage("test2", "Привет2", InvType.WORKBENCH);

            page.addButton(9, () -> new IconHandler() {

                @Override
                public void onClick(InventoryPage page, Player player, ClickType type) {
                    try {
                        inv.openMenu(inv, "test", player, page.getSession());
                    }
                    catch (MenuInvException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public ItemStack onUpdate(InventoryPage consume, Player player) {
                    ItemStack item = new ItemStack(Material.values()[ThreadLocalRandom.current().nextInt(Material.values().length)], 1);
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

            });
            page.addButton(0, () -> new IconHandler() {

                @Override
                public void onClick(InventoryPage page, Player player, ClickType type) {

                }

                @Override
                public ItemStack onUpdate(InventoryPage consume, Player player) {
                    ItemStack item = new ItemStack(Material.values()[ThreadLocalRandom.current().nextInt(Material.values().length)], 1);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName("Привет тест2!");
                    meta.setLore(Arrays.asList("Time2: " + System.currentTimeMillis()));
                    item.setItemMeta(meta);
                    return item;
                }

                @Override
                public long getUpdateTime() {
                    return 5;
                }
            });
            page.addButton(1, () -> new IconHandler() {

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
            });
        }
    }

    public static void registerEvents() {
        MenuInv.getInstance().getServer().getPluginManager().registerEvents(new TestListener(), MenuInv.getInstance());
    }

    public static class TestListener implements Listener {

        @EventHandler
        public void onPlayerInteractEvent(PlayerInteractEvent e) {
            Player pl = e.getPlayer();

            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                e.setUseInteractedBlock(Event.Result.DENY);
                e.setUseItemInHand(Event.Result.DENY);

                try {
                    MenuInv.getInstance().openMenu(MenuInv.getInstance(), "test", pl, new TestSession());
                }
                catch (MenuInvException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    private static class TestSession implements InventorySession{

        private final List<ItemStack> items = new ArrayList<>();

        public List<ItemStack> getItems() {
            return this.items;
        }

    }
}
