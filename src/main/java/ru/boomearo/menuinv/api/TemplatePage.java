package ru.boomearo.menuinv.api;

import ru.boomearo.menuinv.api.frames.PagedIconsBuilder;
import ru.boomearo.menuinv.api.icon.ClickExceptionHandler;
import ru.boomearo.menuinv.api.icon.ElementBuilder;
import ru.boomearo.menuinv.api.icon.UpdateExceptionHandler;

public interface TemplatePage {

    String getName();

    MenuType getMenuType();

    TemplatePage setMenuType(MenuType type);

    InventoryTitleHandler getInventoryTitle();

    TemplatePage setInventoryTitle(InventoryTitleHandler inventoryTitleHandler);

    InventoryReopenHandler getInventoryReopen();

    TemplatePage setInventoryReopen(InventoryReopenHandler inventoryReopenHandler);

    ClickExceptionHandler getClickExceptionHandler();

    TemplatePage setClickExceptionHandler(ClickExceptionHandler clickExceptionHandler);

    UpdateExceptionHandler getUpdateExceptionHandler();

    TemplatePage setUpdateExceptionHandler(UpdateExceptionHandler updateExceptionHandler);

    InventoryCloseHandler getInventoryCloseHandler();

    TemplatePage setInventoryCloseHandler(InventoryCloseHandler inventoryCloseHandler);

    Delayable<InventoryPage> getGlobalUpdateDelay();

    TemplatePage setGlobalUpdateDelay(Delayable<InventoryPage> updateDelay);

    TemplatePage setIcon(int slot, ElementBuilder elementBuilder);

    TemplatePage setImmutableIcon(int slot, ElementBuilder elementBuilder);

    TemplatePage setPagedIcons(String name, int x, int z, int width, int height, PagedIconsBuilder pagedIconsBuilder);

    TemplatePage setImmutablePagedIcons(String name, int x, int z, int width, int height, PagedIconsBuilder pagedIconsBuilder);

    TemplatePage setBackground(ElementBuilder elementBuilder);

    TemplatePage setImmutableBackground(ElementBuilder elementBuilder);

    TemplatePage setStructure(String... value);

    TemplatePage setIngredient(char value, ElementBuilder elementBuilder);

    TemplatePage setImmutableIngredient(char value, ElementBuilder elementBuilderr);
}
