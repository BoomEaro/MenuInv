package ru.boomearo.menuinv.api;

import ru.boomearo.menuinv.api.frames.PagedItemsBuilder;
import ru.boomearo.menuinv.api.icon.ClickExceptionHandler;
import ru.boomearo.menuinv.api.icon.IconBuilder;
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

    TemplatePage setItem(int slot, IconBuilder iconBuilder);

    TemplatePage setPagedItems(String name, int x, int z, int width, int height, PagedItemsBuilder pagedItemsBuilder);

    TemplatePage setBackground(IconBuilder iconBuilder);

    TemplatePage setStructure(String... value);

    TemplatePage setIngredient(char value, IconBuilder iconBuilder);
}
