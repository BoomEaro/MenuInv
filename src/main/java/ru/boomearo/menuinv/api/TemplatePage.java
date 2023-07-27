package ru.boomearo.menuinv.api;

import ru.boomearo.menuinv.api.frames.inventory.PagedItems;
import ru.boomearo.menuinv.api.frames.iteration.FrameIterationHandler;
import ru.boomearo.menuinv.api.scrolls.ScrollHandlerFactory;

/**
 * Представляет шаблон страницы меню
 */
public interface TemplatePage {

    String getName();

    MenuType getMenuType();

    TemplatePage setMenuType(MenuType type);

    InventoryCreationHandler getInventoryCreationHandler();

    TemplatePage setInventoryCreationHandler(InventoryCreationHandler inventoryCreationHandler);

    InventoryReopenHandler getInventoryReopenHandler();

    TemplatePage setInventoryReopenHandler(InventoryReopenHandler inventoryReopenHandler);

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
     * @param name    Название рамки предметов
     * @param x       Горизонтальная позиция рамки в инвентаре
     * @param z       Вертикальная позиция рамки в инвентаре
     * @param width   Ширина рамки
     * @param height  Высота рамки
     * @param factory Фабрика обработчика рамки предметов
     */
    TemplatePage setPagedItems(String name, int x, int z, int width, int height, FramedIconsHandlerFactory factory);

    /**
     * Добавляет рамку предметов, которая может быть прокручена вперед/назад и имеет страницы для просмотра
     *
     * @param name            Название рамки предметов
     * @param x               Горизонтальная позиция рамки в инвентаре
     * @param z               Вертикальная позиция рамки в инвентаре
     * @param width           Ширина рамки
     * @param height          Высота рамки
     * @param iconFactory     Фабрика обработчика рамки предметов
     * @param permanentCached Запомнить ли содержимое рамки навсегда (один раз получить список предметов и больше никогда не обновлять)
     */
    TemplatePage setPagedItems(String name, int x, int z, int width, int height, FramedIconsHandlerFactory iconFactory, boolean permanentCached);

    /**
     * Добавляет рамку предметов, которая может быть прокручена вперед/назад и имеет страницы для просмотра
     *
     * @param name             Название рамки предметов
     * @param x                Горизонтальная позиция рамки в инвентаре
     * @param z                Вертикальная позиция рамки в инвентаре
     * @param width            Ширина рамки
     * @param height           Высота рамки
     * @param iconFactory      Фабрика обработчика рамки предметов
     * @param iterationHandler Обработчик позиции элементов
     */
    TemplatePage setPagedItems(String name, int x, int z, int width, int height, FramedIconsHandlerFactory iconFactory, FrameIterationHandler iterationHandler);

    /**
     * Добавляет рамку предметов, которая может быть прокручена вперед/назад и имеет страницы для просмотра
     *
     * @param name             Название рамки предметов
     * @param x                Горизонтальная позиция рамки в инвентаре
     * @param z                Вертикальная позиция рамки в инвентаре
     * @param width            Ширина рамки
     * @param height           Высота рамки
     * @param iconFactory      Фабрика обработчика рамки предметов
     * @param iterationHandler Обработчик позиции элементов
     * @param permanentCached  Запомнить ли содержимое рамки навсегда (один раз получить список предметов и больше никогда не обновлять)
     */
    TemplatePage setPagedItems(String name, int x, int z, int width, int height, FramedIconsHandlerFactory iconFactory, FrameIterationHandler iterationHandler, boolean permanentCached);

    /**
     * Добавляет предмет, который имеет возможность прокручивать страницы в указанной предметной рамки.
     *
     * @param slot       Позиция предмета
     * @param pagedItems Название рамки, которая будет использоваться для прокручивания
     * @param type       Тип прокручивания (вперед/назад)
     * @param factory    Фабрика обработчика прокрутки
     */
    TemplatePage setScrollItem(int slot, String pagedItems, PagedItems.ScrollType type, ScrollHandlerFactory factory);

    /**
     * Заполняет предметами задний фон. Любой самостоятельный предмет или рамка предметов всегда будут на первом плане.
     *
     * @param factory Фабрика обработчика предмета
     */
    TemplatePage setBackground(IconBuilder iconBuilder);
}
