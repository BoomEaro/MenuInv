package ru.boomearo.menuinv.api;

import ru.boomearo.menuinv.api.frames.PagedIconsBuilder;
import ru.boomearo.menuinv.api.icon.BottomInventoryClickHandler;
import ru.boomearo.menuinv.api.icon.ClickExceptionHandler;
import ru.boomearo.menuinv.api.icon.ElementBuilder;
import ru.boomearo.menuinv.api.icon.UpdateExceptionHandler;

import java.util.List;

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

    TemplatePage setBottomInventoryClickHandler(BottomInventoryClickHandler bottomInventoryClickHandler);

    Delayable<InventoryPage> getGlobalUpdateDelay();

    TemplatePage setGlobalUpdateDelay(Delayable<InventoryPage> updateDelay);

    TemplatePage setIcon(int slot, ElementBuilder elementBuilder);

    TemplatePage setImmutableIcon(int slot, ElementBuilder elementBuilder);

    TemplatePage setPagedIcons(String name, InventoryLocation first, int width, int height, PagedIconsBuilder pagedIconsBuilder);

    TemplatePage setPagedIcons(String name, InventoryLocation first, InventoryLocation second, PagedIconsBuilder pagedIconsBuilder);

    TemplatePage setPagedIconsIngredients(String name, char first, char second, PagedIconsBuilder pagedIconsBuilder);

    TemplatePage setImmutablePagedIcons(String name, InventoryLocation first, int width, int height, PagedIconsBuilder pagedIconsBuilder);

    TemplatePage setImmutablePagedIcons(String name, InventoryLocation first, InventoryLocation second, PagedIconsBuilder pagedIconsBuilder);

    TemplatePage setImmutablePagedIconsIngredients(String name, char first, char second, PagedIconsBuilder pagedIconsBuilder);

    TemplatePage setBackground(ElementBuilder elementBuilder);

    TemplatePage setImmutableBackground(ElementBuilder elementBuilder);

    TemplatePage setStructure(String... value);

    TemplatePage setStructure(List<String> value);

    TemplatePage setIngredient(char value, ElementBuilder elementBuilder);

    TemplatePage setImmutableIngredient(char value, ElementBuilder elementBuilder);
}
