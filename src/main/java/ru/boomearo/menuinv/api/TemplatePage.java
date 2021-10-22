package ru.boomearo.menuinv.api;

import ru.boomearo.menuinv.api.frames.inventory.PagedItems;
import ru.boomearo.menuinv.api.scrolls.ScrollHandlerFactory;
import ru.boomearo.menuinv.exceptions.MenuInvException;

/**
 * Представляет шаблон страницы меню
 */
public interface TemplatePage {

    /**
     * Добавляет предмет в шаблонную страницу
     * @param slot Позиция предмета
     * @param factory Фабрика обработчика предмета
     */
    public void addItem(int slot, IconHandlerFactory factory) throws MenuInvException;

    /**
     * Добавляет рамку предметов, которая может быть прокручена вперед/назад и имеет страницы для просмотра
     * @param name Название рамки предметов
     * @param x Горизонтальная позиция рамки в инвентаре
     * @param z Вертикальная позиция рамки в инвентаре
     * @param width Ширина рамки
     * @param height Высота рамки
     * @param factory Фабрика обработчика рамки предметов
     */
    public void addPagedItems(String name, int x, int z, int width, int height, FramedIconsHandlerFactory factory) throws MenuInvException;

    /**
     * Добавляет предмет, который имеет возможность прокручивать страницы в указанной предметной рамки.
     * @param slot Позиция предмета
     * @param pagedItems Название рамки, которая будет использоваться для прокручивания
     * @param type Тип прокручивания (вперед/назад)
     * @param factory Фабрика обработчика прокрутки
     */
    public void addScrollButton(int slot, String pagedItems, PagedItems.ScrollType type, ScrollHandlerFactory factory) throws MenuInvException;

    /**
     * Заполняет предметами задний фон. Любой самостоятельный предмет или рамка предметов всегда будут на первом плане.
     * @param factory Фабрика обработчика предмета
     */
    public void setBackground(IconHandlerFactory factory) throws MenuInvException;
}
