package ru.boomearo.menuinv.example;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import ru.boomearo.menuinv.api.*;
import ru.boomearo.menuinv.api.frames.AsyncPagedIconsBuilder;
import ru.boomearo.menuinv.api.frames.PagedIcons;
import ru.boomearo.menuinv.api.frames.PagedIconsBuilder;
import ru.boomearo.menuinv.api.icon.AsyncIconBuilder;
import ru.boomearo.menuinv.api.icon.IconBuilder;
import ru.boomearo.menuinv.api.icon.IconHandler;
import ru.boomearo.menuinv.api.icon.scrolls.ScrollIconBuilder;
import ru.boomearo.menuinv.api.icon.scrolls.ScrollType;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

/**
 * Just an example menu for debug
 */
public class ExampleMenuManager {

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
    
    public static void setup(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder() + File.separator + "config.yml");
        if (!configFile.exists()) {
            plugin.getLogger().info("Config not found, creating a new one...");
            plugin.saveDefaultConfig();
        }

        try {
            if (plugin.getConfig().getBoolean("debug")) {
                plugin.getLogger().warning("Debug mode activated!");

                setupMenu(plugin);
                plugin.getServer().getPluginManager().registerEvents(new ExampleListener(), plugin);
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to configure debug mode", e);
        }
    }

    private static void setupMenu(Plugin plugin) {
        ExecutorService executorService = Executors.newFixedThreadPool(2, new ThreadFactoryBuilder()
                .setNameFormat("exampleMenu-%d")
                .build());
        {
            Menu.registerPages(plugin)
                    .createTemplatePage(ExampleMenuPage.MAIN)
                    .setMenuType(MenuType.CHEST_9X6)
                    .setInventoryCloseHandler((inventoryPage, player) -> player.sendMessage("Inventory closed!"))
                    .setInventoryTitle((inventoryPage) -> {
                        PagedIcons piExample1 = inventoryPage.getListedIconsItems("example");
                        PagedIcons piExample2 = inventoryPage.getListedIconsItems("example2");

                        return "example: " + piExample1.getCurrentPage() + "/" + piExample1.getMaxPage() + " ||| " + piExample2.getCurrentPage() + "/" + piExample2.getMaxPage();
                    })
                    .setIcon(1, new IconBuilder()
                            .setIconClick((inventoryPage, icon, player, type) -> Menu.open(ExampleMenuPage.OTHER, player, inventoryPage.getSession()))
                            .setIconUpdate((consume, player) -> new ItemStack(Material.STONE, 1)))

                    .setIcon(3, new IconBuilder()
                            .setIconClick((inventoryPage, icon, player, type) -> {
                                ExampleSession ts = (ExampleSession) inventoryPage.getSession();

                                List<ItemStack> items = ts.getItems();
                                if (items.isEmpty()) {
                                    return;
                                }

                                items.remove(items.size() - 1);

                                inventoryPage.update(true);
                            })
                            .setIconUpdate((inventoryPage, player) -> new ItemStack(Material.REDSTONE_ORE, 1)))

                    .setIcon(4, new IconBuilder()
                            .setIconClick((inventoryPage, icon, player, type) -> {
                                ExampleSession ts = (ExampleSession) inventoryPage.getSession();

                                ts.getItems().add(new ItemStack(Material.DIAMOND, 1));

                                inventoryPage.update(true);
                            })
                            .setIconUpdate((inventoryPage, player) -> new ItemStack(Material.EMERALD_BLOCK, 1)))

                    .setStructure(
                            "# # # # # # # # #",
                            "# . . . . . < > #",
                            "1 . . . ? . 3 . #",
                            "# . . . ? . . . #",
                            "# . . . . . . . 4",
                            "# # 2 # # # # # #")

                    .setIngredient('#', new IconBuilder()
                            .setIconUpdate((inventoryPage, player) -> new ItemStack(Material.TNT, 1)))
                    .setIngredient('?', new IconBuilder()
                            .setIconUpdate((inventoryPage, player) -> new ItemStack(Material.TNT, 1)))

                    .setIngredient('<', new ScrollIconBuilder()
                            .setName("example")
                            .setScrollType(ScrollType.PREVIOUSLY)
                            .setScrollVisibleUpdate((inventoryPage, player, scrollType, currentPage, maxPage) -> createScrollItems(scrollType, currentPage, maxPage)))

                    .setIngredient('>', new ScrollIconBuilder()
                            .setName("example")
                            .setScrollType(ScrollType.NEXT)
                            .setScrollVisibleUpdate((inventoryPage, player, scrollType, currentPage, maxPage) -> createScrollItems(scrollType, currentPage, maxPage)))

                    .setPagedIconsIngredients("example", '1', '2', new PagedIconsBuilder()
                            .setPagedItemsUpdate((inventoryPage, player) -> {
                                List<IconHandler> tmp = new ArrayList<>();
                                for (int i = 1; i <= new Random().nextInt(1000); i++) {
                                    int t = i;
                                    tmp.add(new AsyncIconBuilder()
                                            .setExecutorService(executorService)
                                            .setLoadedIcon(new IconBuilder()
                                                    .setUpdateDelay((data, force) -> Duration.ofSeconds(1))
                                                    .setIconUpdate((inventoryPage2, player2) -> {
                                                        try {
                                                            Thread.sleep(250);
                                                        } catch (InterruptedException e) {
                                                            throw new RuntimeException(e);
                                                        }

                                                        ItemStack itemStack = new ItemStack(Material.SIGN, t);
                                                        ItemMeta itemMeta = itemStack.getItemMeta();
                                                        itemMeta.setDisplayName("Loaded data " + new Random().nextInt(64) + " for data type #" + t);
                                                        itemStack.setItemMeta(itemMeta);

                                                        return itemStack;
                                                    })
                                                    .setIconClick((inventoryPage2, icon, player2, clickType) -> player2.sendMessage("Data type #" + t + " was loaded!")))
                                            .setLoadingIcon(new IconBuilder()
                                                    .setIconUpdate((inventoryPage2, player2) -> {
                                                        ItemStack itemStack = new ItemStack(Material.PAPER, t);
                                                        ItemMeta itemMeta = itemStack.getItemMeta();
                                                        itemMeta.setDisplayName("TIME: " + System.currentTimeMillis() + ". Loading data with type #" + t + "...");
                                                        itemStack.setItemMeta(itemMeta);
                                                        return itemStack;
                                                    })
                                                    .setIconClick((inventoryPage2, icon, player2, clickType) -> player2.sendMessage("Data type #" + t + " is loading...")))
                                            .build()
                                            .create());
                                }
                                return tmp;
                            })
                            .setCacheHandler((page, force) -> Duration.ofSeconds(5))
                            .setUpdateDelay(new InfinityUpdateDelay<>()))

                    /*.setPagedIconsIngredients("example2", '3', '4', new PagedIconsBuilder()
                            .setPagedItemsUpdate((inventoryPage, player) -> {
                                List<IconHandler> tmp = new ArrayList<>();
                                for (int i = 1; i <= new Random().nextInt(20); i++) {
                                    int t = i;
                                    tmp.add(new IconBuilder()
                                            .setIconClick((inventoryPage2, icon, player2, clickType) -> player2.sendMessage("REDSTONE: " + t))
                                            .setIconUpdate((inventoryPage2, player2) -> new ItemStack(Material.REDSTONE_ORE, t))
                                            .build()
                                            .create());
                                }
                                return tmp;
                            }))*/

                    .setPagedIconsIngredients("example2", '3', '4', new AsyncPagedIconsBuilder()
                            .setExecutorService(executorService)
                            .setLoadedPagedIcons(new PagedIconsBuilder()
                                    .setPagedItemsUpdate((inventoryPage, player) -> {
                                        List<IconHandler> tmp = new ArrayList<>();
                                        for (int i = 1; i < 20; i++) {
                                            int finalI = i;
                                            tmp.add(new IconBuilder()
                                                    .setIconUpdate((inventoryPage2, player2) -> {
                                                        ItemStack itemStack = new ItemStack(Material.SIGN, finalI);
                                                        ItemMeta itemMeta = itemStack.getItemMeta();
                                                        itemMeta.setDisplayName("Loaded data " + new Random().nextInt(64) + " for data type #" + finalI);
                                                        itemStack.setItemMeta(itemMeta);

                                                        return itemStack;
                                                    })
                                                    .build()
                                                    .create());
                                        }

                                        try {
                                            Thread.sleep(2000);
                                        } catch (InterruptedException e) {
                                            throw new RuntimeException(e);
                                        }

                                        return tmp;
                                    }))
                            .setLoadingPagedIcons(new PagedIconsBuilder()
                                    .setPagedItemsUpdate((inventoryPage, player) -> {
                                        List<IconHandler> tmp = new ArrayList<>();
                                        tmp.add(new IconBuilder()
                                                .setIconUpdate((inventoryPage2, player2) -> {
                                                    ItemStack itemStack = new ItemStack(Material.PAPER, 1);
                                                    ItemMeta itemMeta = itemStack.getItemMeta();
                                                    itemMeta.setDisplayName("Just loading holder!");
                                                    itemStack.setItemMeta(itemMeta);

                                                    return itemStack;
                                                })
                                                .build()
                                                .create());
                                        return tmp;
                                    }))
                    )

                    .setImmutableBackground(new IconBuilder()
                            .setIconUpdate((inventoryPage, player) -> new ItemStack(Material.COOKIE, 1)));
        }
        {
            Menu.registerPages(plugin)
                    .createTemplatePage(ExampleMenuPage.OTHER)
                    .setMenuType(MenuType.WORKBENCH)
                    .setGlobalUpdateDelay((data, force) -> Duration.ZERO)
                    .setInventoryTitle((inventoryPage) -> "Hello2")
                    .setIcon(9, new IconBuilder()
                            .setIconClick((inventoryPage, icon, player, click) -> Menu.open(ExampleMenuPage.MAIN, player, inventoryPage.getSession()))
                            .setIconUpdate((inventoryPage, player) -> {
                                ItemStack item = new ItemStack(MATERIALS.get(ThreadLocalRandom.current().nextInt(MATERIALS.size())), 1);
                                ItemMeta meta = item.getItemMeta();
                                meta.setDisplayName("Hello@");
                                meta.setLore(Collections.singletonList("Time: " + System.currentTimeMillis()));
                                item.setItemMeta(meta);
                                return item;
                            })
                            .setUpdateDelay((inventoryPage, force) -> Duration.ZERO))

                    .setIcon(0, new IconBuilder()
                            .setIconUpdate((inventoryPage, player) -> {
                                ItemStack item = new ItemStack(MATERIALS.get(ThreadLocalRandom.current().nextInt(MATERIALS.size())), 1);
                                ItemMeta meta = item.getItemMeta();
                                meta.setDisplayName("Hello!");
                                meta.setLore(Collections.singletonList("Time2: " + System.currentTimeMillis()));
                                item.setItemMeta(meta);
                                return item;
                            })
                            .setUpdateDelay((inventoryPage, force) -> {
                                if (force) {
                                    return Duration.ZERO;
                                }

                                return Duration.ofMillis(500);
                            }))

                    .setIcon(1, new IconBuilder()
                            .setIconClick((inventoryPage, icon, player, clickType) -> inventoryPage.close())
                            .setIconUpdate((inventoryPage, player) -> {
                                ItemStack item = new ItemStack(Material.BARRIER, 1);
                                ItemMeta meta = item.getItemMeta();
                                meta.setDisplayName("Close");
                                meta.setLore(Collections.singletonList("UwU"));
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

}
