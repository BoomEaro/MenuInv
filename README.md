## Plugin
MenuInv - Is a plugin that allows you to create a simple menu based on API.

## Why a plugin and not a standalone api?
The plugin allows you to register a menu, allowing other plugins to open it without worrying about implementation details.

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
    private static enum CustomMenuPage implements PluginPage {

        MAIN(MenuInv.getInstance(), "main"),
        OTHER(MenuInv.getInstance(), "other");

        private final Plugin Plugin;
        private final String page;

        CustomMenuPage(Plugin Plugin, String page) {
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
        Menu.registerPages(menuInv)
                .createTemplatePage(CustomMenuPage.MAIN)
                .setMenuType(MenuType.CHEST_9X6)
                .setStructure(
                        "# # # # # # # # #",
                        "* . . . . . . . *",
                        "# . . . 4 . . . #",
                        "* . . . 4 . . . *",
                        "# . . . . . . . #",
                        "* # # # # # # # *")
                .setIngredient('#', new IconBuilder()
                        .setIconUpdate((inventoryPage, player) -> new ItemStack(Material.COOKIE, 1))
                        .setIconClick((inventoryPage, player, clickType) -> {
                            player.sendMessage("Delicious cookies!");
                            Menu.open(CustomMenuPage.OTHER, player);
                        }))
                .setIngredient('*', new IconBuilder()
                        .setIconUpdate((inventoryPage, player) -> new ItemStack(Material.COMPASS, 1))
                        .setIconClick((inventoryPage, player, clickType) -> player.sendMessage("COMPASS???")))
                .setIngredient('4', new IconBuilder()
                        .setIconUpdate((inventoryPage, player) -> new ItemStack(Material.DIAMOND, 6))
                        .setIconClick((inventoryPage, player, clickType) -> player.sendMessage("Surprise!")));

        Menu.registerPages(menuInv)
                .createTemplatePage(CustomMenuPage.OTHER)
                .setMenuType(MenuType.CHEST_9X1)
                .setStructure(
                        "# . # . # . # . #"
                )
                .setIngredient('#', new IconBuilder()
                        .setIconUpdate((inventoryPage, player) -> new ItemStack(Material.PAPER, 1))
                        .setIconClick((inventoryPage, player, clickType) -> {
                            player.sendMessage("Returning back");
                            Menu.open(CustomMenuPage.MAIN, player);
                        }));
```


