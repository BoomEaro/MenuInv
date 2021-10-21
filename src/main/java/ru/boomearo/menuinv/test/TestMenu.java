package ru.boomearo.menuinv.test;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import org.bukkit.inventory.meta.ItemMeta;
import ru.boomearo.menuinv.MenuInv;
import ru.boomearo.menuinv.api.*;
import ru.boomearo.menuinv.exceptions.MenuInvException;
import ru.boomearo.menuinv.objects.InventoryPage;
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
            TemplatePage page = pages.createTemplatePage("test", "Привет", 6);

            page.addButton(1, () -> new IconHandler() {

                @Override
                public void onClick(InventoryPage page, Player player, ClickType type) {
                    try {
                        inv.openMenu(inv, "test2", player);
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

            page.addListedButton("test", 2, 2, 5, 4, () -> (consume, player) -> {
                List<IconHandler> tmp = new ArrayList<>();
                for (Material mat : Material.values()) {
                    tmp.add(new IconHandler() {

                        @Override
                        public void onClick(InventoryPage page, Player player, ClickType type) {
                            player.sendMessage("Вот так вот: " + mat);
                        }

                        @Override
                        public ItemStack onUpdate(InventoryPage consume, Player player) {
                            return new ItemStack(mat, 1);
                        }

                    });
                }
                return tmp;
            });

            page.addScrollButton(7, "test", PagedItems.ScrollType.PREVIOUSLY, () -> new ScrollHandler() {

                @Override
                public ItemStack onVisible(int currentPage, int maxPage) {
                    ItemStack item = new ItemStack(Material.PAPER, 1);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName("");
                    meta.setLore(Arrays.asList("Назад (" + (currentPage - 1) + "/" + maxPage + ")"));
                    item.setItemMeta(meta);
                    return item;
                }

                @Override
                public ItemStack onHide(int currentPage, int maxPage) {
                    return null;
                }

            });

            page.addScrollButton(8, "test", PagedItems.ScrollType.NEXT, () -> new ScrollHandler() {

                @Override
                public ItemStack onVisible(int currentPage, int maxPage) {
                    ItemStack item = new ItemStack(Material.PAPER, 1);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName("");
                    meta.setLore(Arrays.asList("Вперед (" + (currentPage + 1) + "/" + maxPage + ")"));
                    item.setItemMeta(meta);
                    return item;
                }

                @Override
                public ItemStack onHide(int currentPage, int maxPage) {
                    return null;
                }

            });
        }
        {
            TemplatePage page = pages.createTemplatePage("test2", "Привет2", InvType.Hopper);

            page.addButton(0, () -> new IconHandler() {

                @Override
                public void onClick(InventoryPage page, Player player, ClickType type) {
                    try {
                        inv.openMenu(inv, "test", player);
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
            page.addButton(1, () -> new IconHandler() {

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
            page.addButton(4, () -> new IconHandler() {

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
                try {
                    MenuInv.getInstance().openMenu(MenuInv.getInstance(), "test", pl);
                }
                catch (MenuInvException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }
}
