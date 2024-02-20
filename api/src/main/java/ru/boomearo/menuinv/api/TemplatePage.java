package ru.boomearo.menuinv.api;

import ru.boomearo.menuinv.api.frames.PagedElementBuilder;
import ru.boomearo.menuinv.api.icon.BottomInventoryClickHandler;
import ru.boomearo.menuinv.api.icon.ClickExceptionHandler;
import ru.boomearo.menuinv.api.icon.ElementBuilder;
import ru.boomearo.menuinv.api.icon.UpdateExceptionHandler;

import java.util.List;

public interface TemplatePage {

    String getName();

    PluginTemplatePages getPluginTemplatePages();

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

    TemplatePage setPagedIcons(String name, InventoryLocation first, int width, int height, PagedElementBuilder pagedIconsBuilder);

    TemplatePage setPagedIcons(String name, InventoryLocation first, InventoryLocation second, PagedElementBuilder pagedIconsBuilder);

    TemplatePage setPagedIconsIngredients(String name, char first, char second, PagedElementBuilder pagedIconsBuilder);

    TemplatePage setImmutablePagedIcons(String name, InventoryLocation first, int width, int height, PagedElementBuilder pagedIconsBuilder);

    TemplatePage setImmutablePagedIcons(String name, InventoryLocation first, InventoryLocation second, PagedElementBuilder pagedIconsBuilder);

    TemplatePage setImmutablePagedIconsIngredients(String name, char first, char second, PagedElementBuilder pagedIconsBuilder);

    TemplatePage setBackground(ElementBuilder elementBuilder);

    TemplatePage setImmutableBackground(ElementBuilder elementBuilder);

    TemplatePage setStructure(String... value);

    TemplatePage setStructure(List<String> value);

    TemplatePage setIngredient(char value, ElementBuilder elementBuilder);

    TemplatePage setImmutableIngredient(char value, ElementBuilder elementBuilder);
}
