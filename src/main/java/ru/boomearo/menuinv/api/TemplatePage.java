package ru.boomearo.menuinv.api;

import ru.boomearo.menuinv.api.frames.PagedItemsBuilder;
import ru.boomearo.menuinv.api.icon.ClickExceptionHandler;
import ru.boomearo.menuinv.api.icon.IconBuilder;
import ru.boomearo.menuinv.api.icon.UpdateExceptionHandler;
import ru.boomearo.menuinv.api.icon.scrolls.ScrollIconBuilder;
import ru.boomearo.menuinv.api.icon.scrolls.ScrollType;

/**
 * Представляет шаблон страницы меню
 */
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

    /**
     * Добавляет предмет в шаблонную страницу
     *
     * @param slot    Позиция предмета
     * @param factory Фабрика обработчика предмета
     */
    TemplatePage setItem(int slot, IconBuilder iconBuilder);

    /**
     * Добавляет рамку предметов, которая может быть прокручена вперед/назад и имеет страницы для просмотра
     *
     * @param name             Название рамки предметов
     * @param x                Горизонтальная позиция рамки в инвентаре
     * @param z                Вертикальная позиция рамки в инвентаре
     * @param width            Ширина рамки
     * @param height           Высота рамки
     * @param iconFactory      Фабрика обработчика рамки предметов
     */
    TemplatePage setPagedItems(String name, int x, int z, int width, int height, PagedItemsBuilder pagedItemsBuilder);

    /**
     * Добавляет предмет, который имеет возможность прокручивать страницы в указанной предметной рамки.
     *
     * @param slot       Позиция предмета
     * @param pagedItems Название рамки, которая будет использоваться для прокручивания
     * @param type       Тип прокручивания (вперед/назад)
     * @param factory    Фабрика обработчика прокрутки
     */
    TemplatePage setScrollItem(int slot, String pagedItems, ScrollType type, ScrollIconBuilder scrollIconBuilder);

    /**
     * Заполняет предметами задний фон. Любой самостоятельный предмет или рамка предметов всегда будут на первом плане.
     *
     * @param factory Фабрика обработчика предмета
     */
    TemplatePage setBackground(IconBuilder iconBuilder);
}
