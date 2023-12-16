## Plugin

MenuInv - Is a plugin that allows you to create a simple menu based on API.

## Why a plugin and not a standalone api?

The plugin allows you to register a menu, allowing other plugins to open it without worrying about implementation
details.

## Features

- Create a menu with any chest size or other type of inventory
- Flexible change of the item in the slot, which can depend on events
- Supports all kinds of interaction with inventory slots (LMB, RMB, Shift, etc.)
- Sessions for each open menu, with which you can change the behavior of the menu
- Scrollable item pages
- Easy creation and customization of the menu, no need to remember the position of the slots
- Opening other menus registered by other plugins

## EXAMPLE

First you need to create your own enum that implements PluginPage to store pages.
Note that getPlugin should return a reference to your plugin.

```
    public enum MenuPage implements PluginPage {

        MAIN(plugin.getInstance(), "main"),
        OTHER(plugin.getInstance(), "other");

        private final Plugin Plugin;
        private final String page;

        MenuPage(Plugin Plugin, String page) {
            this.Plugin = Plugin;
            this.page = page;
        }

        @Override
        public Plugin getPlugin() {
            return this.Plugin;
        }

        @Override
        public String getPage() {
            return this.page;
        }
       
    }
```

Menu creation and opening.
As arguments to registerPages you need to specify your page enum.
To open the menu, you also need to specify an enum.

```
    public class TestMenu {

        public static void registerMenu(Plugin plugin) {
            Menu.registerPages(plugin)
                    .createTemplatePage(MenuPage.MAIN)
                    .setInventoryTitle((inventoryPage) -> "Main page")
                    .setInventoryCloseHandler((inventoryPage, player) -> player.sendMessage("Menu was closed!"))
                    .setMenuType(MenuType.CHEST_9X6)
                    .setStructure(
                            "# # # # # # # # #",
                            "* . . . 4 . . . *",
                            "# . . . 4 . . . #",
                            "* 1 . . . . . . *",
                            "# . . . . . . 2 #",
                            "* # # < # > # # *")
                    .setIngredient('#', new IconBuilder()
                            .setIconUpdate((inventoryPage, player) -> new ItemStack(Material.COOKIE, 1))
                            .setIconClick((inventoryPage, player, clickType) -> {
                                player.sendMessage("Delicious cookies!");
                                Menu.open(MenuPage.OTHER, player, inventoryPage.getSession());
                            }))
                    .setIngredient('*', new IconBuilder()
                            .setIconUpdate((inventoryPage, player) -> new ItemStack(Material.COMPASS, 1))
                            .setIconClick((inventoryPage, player, clickType) -> player.sendMessage("COMPASS???")))
                    .setIngredient('4', new IconBuilder()
                            .setIconUpdate((inventoryPage, player) -> new ItemStack(Material.DIAMOND, 6))
                            .setIconClick((inventoryPage, player, clickType) -> player.sendMessage("Surprise!")))

                    .setPagedIconsIngredients("players", '1', '2', new PagedIconsBuilder()
                            .setPagedItemsUpdate((inventoryPage, player) -> {
                                List<IconHandler> tmp = new ArrayList<>();
                                for (Player pl : Bukkit.getOnlinePlayers()) {
                                    tmp.add(new IconBuilder()
                                            .setIconClick((inventoryPage2, player2, type) -> player2.sendMessage("Player: " + pl.getName()))
                                            .setIconUpdate((inventoryPage2, player2) -> {
                                                ItemStack item = new ItemStack(Material.SKULL_ITEM, 1);
                                                ItemMeta itemMeta = item.getItemMeta();
                                                itemMeta.setDisplayName("Player: " + pl.getName());
                                                item.setItemMeta(itemMeta);
                                                return item;
                                            })
                                            .build()
                                            .create());
                                }
                                return tmp;
                            }))
                    .setIngredient('<', new ScrollIconBuilder()
                            .setName("players")
                            .setScrollType(ScrollType.PREVIOUSLY)
                            .setScrollVisibleUpdate((inventoryPage, player, scrollType, currentPage, maxPage) -> createScrollItems(scrollType, currentPage, maxPage)))

                    .setIngredient('>', new ScrollIconBuilder()
                            .setName("players")
                            .setScrollType(ScrollType.NEXT)
                            .setScrollVisibleUpdate((inventoryPage, player, scrollType, currentPage, maxPage) -> createScrollItems(scrollType, currentPage, maxPage)));


        TemplatePage templatePage = Menu.registerPages(plugin)
                .createTemplatePage(MenuPage.OTHER)
                .setInventoryTitle((inventoryPage) -> "Other page")
                .setMenuType(MenuType.CHEST_9X1)
                .setStructure(
                        "1 . 2 . 3 . 4 . 5"
                );

        // Async icons
        for (int i = 1; i <= 5; i++) {
            final int finalI = i;
            char character = ("" + i).charAt(0);
            templatePage.setIngredient(character, new AsyncIconBuilder()
                    .setLoadedIcon(new IconBuilder()
                            .setUpdateDelay((data, force) -> Duration.ofSeconds(1))
                            .setIconUpdate((inventoryPage2, player2) -> {
                                try {
                                    Thread.sleep(150);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }

                                ItemStack itemStack = new ItemStack(Material.SIGN, finalI);
                                ItemMeta itemMeta = itemStack.getItemMeta();
                                itemMeta.setDisplayName("Loaded data " + new Random().nextInt(64) + " for data type #" + finalI);
                                itemStack.setItemMeta(itemMeta);

                                return itemStack;
                            })
                            .setIconClick((inventoryPage2, player2, clickType) -> player2.sendMessage("Data type #" + finalI + " was loaded!")))
                    .setLoadingIcon(new IconBuilder()
                            .setIconUpdate((inventoryPage2, player2) -> {
                                ItemStack itemStack = new ItemStack(Material.PAPER, finalI);
                                ItemMeta itemMeta = itemStack.getItemMeta();
                                itemMeta.setDisplayName("TIME: " + System.currentTimeMillis() + ". Loading data with type #" + finalI + "...");
                                itemStack.setItemMeta(itemMeta);
                                return itemStack;
                            })
                            .setIconClick((inventoryPage2, player2, clickType) -> player2.sendMessage("Data type #" + finalI + " is loading..."))));
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
```


