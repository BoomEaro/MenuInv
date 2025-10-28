package ru.boomearo.menuinv.api;

import lombok.NonNull;
import ru.boomearo.menuinv.api.frames.PagedElementBuilder;
import ru.boomearo.menuinv.api.icon.BottomInventoryClickHandler;
import ru.boomearo.menuinv.api.icon.ClickExceptionHandler;
import ru.boomearo.menuinv.api.icon.ElementBuilder;
import ru.boomearo.menuinv.api.icon.UpdateExceptionHandler;

import java.util.List;

public interface TemplatePage {

    @NonNull
    String getName();

    @NonNull
    PluginTemplatePages getPluginTemplatePages();

    @NonNull
    MenuType getMenuType();

    @NonNull
    TemplatePage setMenuType(@NonNull MenuType type);

    @NonNull
    InventoryTitleHandler getInventoryTitle();

    @NonNull
    TemplatePage setInventoryTitle(@NonNull InventoryTitleHandler inventoryTitleHandler);

    @NonNull
    InventoryReopenHandler getInventoryReopen();

    @NonNull
    TemplatePage setInventoryReopen(@NonNull InventoryReopenHandler inventoryReopenHandler);

    @NonNull
    ClickExceptionHandler getClickExceptionHandler();

    @NonNull
    TemplatePage setClickExceptionHandler(@NonNull ClickExceptionHandler clickExceptionHandler);

    @NonNull
    UpdateExceptionHandler getUpdateExceptionHandler();

    TemplatePage setUpdateExceptionHandler(@NonNull UpdateExceptionHandler updateExceptionHandler);

    @NonNull
    InventoryCloseHandler getInventoryCloseHandler();

    @NonNull
    TemplatePage setInventoryCloseHandler(@NonNull InventoryCloseHandler inventoryCloseHandler);

    @NonNull
    TemplatePage setBottomInventoryClickHandler(@NonNull BottomInventoryClickHandler bottomInventoryClickHandler);

    @NonNull
    Delayable<InventoryPage> getGlobalUpdateDelay();

    @NonNull
    TemplatePage setGlobalUpdateDelay(@NonNull Delayable<InventoryPage> updateDelay);

    @NonNull
    TemplatePage setIcon(int slot, @NonNull ElementBuilder elementBuilder);

    @NonNull
    TemplatePage setImmutableIcon(int slot, @NonNull ElementBuilder elementBuilder);

    @NonNull
    TemplatePage setPagedIcons(@NonNull String name, @NonNull InventoryLocation first, int width, int height, @NonNull PagedElementBuilder pagedIconsBuilder);

    @NonNull
    TemplatePage setPagedIcons(@NonNull String name, @NonNull InventoryLocation first, @NonNull InventoryLocation second, @NonNull PagedElementBuilder pagedIconsBuilder);

    @NonNull
    TemplatePage setPagedIconsIngredients(@NonNull String name, char first, char second, @NonNull PagedElementBuilder pagedIconsBuilder);

    @NonNull
    TemplatePage setImmutablePagedIcons(@NonNull String name, @NonNull InventoryLocation first, int width, int height, @NonNull PagedElementBuilder pagedIconsBuilder);

    @NonNull
    TemplatePage setImmutablePagedIcons(@NonNull String name, @NonNull InventoryLocation first, @NonNull InventoryLocation second, @NonNull PagedElementBuilder pagedIconsBuilder);

    @NonNull
    TemplatePage setImmutablePagedIconsIngredients(@NonNull String name, char first, char second, @NonNull PagedElementBuilder pagedIconsBuilder);

    @NonNull
    TemplatePage setBackground(@NonNull ElementBuilder elementBuilder);

    @NonNull
    TemplatePage setImmutableBackground(@NonNull ElementBuilder elementBuilder);

    @NonNull
    TemplatePage setStructure(@NonNull String... value);

    @NonNull
    TemplatePage setStructure(@NonNull List<String> value);

    @NonNull
    TemplatePage setIngredient(char value, @NonNull ElementBuilder elementBuilder);

    @NonNull
    TemplatePage setImmutableIngredient(char value, @NonNull ElementBuilder elementBuilder);
}
