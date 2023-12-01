package ru.boomearo.menuinv.api;

import ru.boomearo.menuinv.api.frames.PagedIconsBuilder;
import ru.boomearo.menuinv.api.icon.BottomInventoryClickHandler;
import ru.boomearo.menuinv.api.icon.ClickExceptionHandler;
import ru.boomearo.menuinv.api.icon.ElementBuilder;
import ru.boomearo.menuinv.api.icon.UpdateExceptionHandler;
import ru.boomearo.menuinv.api.session.InventorySession;

import java.util.List;

public interface TemplatePage<SESSION extends InventorySession> {

    String getName();

    MenuType getMenuType();

    TemplatePage<SESSION> setMenuType(MenuType type);

    InventoryTitleHandler<SESSION> getInventoryTitle();

    TemplatePage<SESSION> setInventoryTitle(InventoryTitleHandler<SESSION> inventoryTitleHandler);

    InventoryReopenHandler<SESSION> getInventoryReopen();

    TemplatePage<SESSION> setInventoryReopen(InventoryReopenHandler<SESSION> inventoryReopenHandler);

    ClickExceptionHandler<SESSION> getClickExceptionHandler();

    TemplatePage<SESSION> setClickExceptionHandler(ClickExceptionHandler<SESSION> clickExceptionHandler);

    UpdateExceptionHandler<SESSION> getUpdateExceptionHandler();

    TemplatePage<SESSION> setUpdateExceptionHandler(UpdateExceptionHandler<SESSION> updateExceptionHandler);

    InventoryCloseHandler<SESSION> getInventoryCloseHandler();

    TemplatePage<SESSION> setInventoryCloseHandler(InventoryCloseHandler<SESSION> inventoryCloseHandler);

    TemplatePage<SESSION> setBottomInventoryClickHandler(BottomInventoryClickHandler<SESSION> bottomInventoryClickHandler);

    Delayable<InventoryPage<SESSION>> getGlobalUpdateDelay();

    TemplatePage<SESSION> setGlobalUpdateDelay(Delayable<InventoryPage<SESSION>> updateDelay);

    TemplatePage<SESSION> setIcon(int slot, ElementBuilder<SESSION> elementBuilder);

    TemplatePage<SESSION> setImmutableIcon(int slot, ElementBuilder<SESSION> elementBuilder);

    TemplatePage<SESSION> setPagedIcons(String name, InventoryLocation first, int width, int height, PagedIconsBuilder<SESSION> pagedIconsBuilder);

    TemplatePage<SESSION> setPagedIcons(String name, InventoryLocation first, InventoryLocation second, PagedIconsBuilder<SESSION> pagedIconsBuilder);

    TemplatePage<SESSION> setPagedIconsIngredients(String name, char first, char second, PagedIconsBuilder<SESSION> pagedIconsBuilder);

    TemplatePage<SESSION> setImmutablePagedIcons(String name, InventoryLocation first, int width, int height, PagedIconsBuilder<SESSION> pagedIconsBuilder);

    TemplatePage<SESSION> setImmutablePagedIcons(String name, InventoryLocation first, InventoryLocation second, PagedIconsBuilder<SESSION> pagedIconsBuilder);

    TemplatePage<SESSION> setImmutablePagedIconsIngredients(String name, char first, char second, PagedIconsBuilder<SESSION> pagedIconsBuilder);

    TemplatePage<SESSION> setBackground(ElementBuilder<SESSION> elementBuilder);

    TemplatePage<SESSION> setImmutableBackground(ElementBuilder<SESSION> elementBuilder);

    TemplatePage<SESSION> setStructure(String... value);

    TemplatePage<SESSION> setStructure(List<String> value);

    TemplatePage<SESSION> setIngredient(char value, ElementBuilder<SESSION> elementBuilder);

    TemplatePage<SESSION> setImmutableIngredient(char value, ElementBuilder<SESSION> elementBuilder);
}
