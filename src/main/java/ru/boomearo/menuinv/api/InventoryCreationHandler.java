package ru.boomearo.menuinv.api;

/**
 * Обработчик создания нового баккитовского инвентаря.
 */
public interface InventoryCreationHandler {

    /**
     * Создает новый тайтл для инвентаря
     * @param page Страница, в которой произошло создание тайтла
     * @return Новый тайтл который будет применен в баккитовском инвентаре
     */
    public String createTitle(InventoryPage page);

    /**
     * Условие, используемое для переоткрытия инвентаря
     * @param page Страница, в которой произошла проверка условия
     * @param forceUpdate Является ли вызов принудительным
     * @return Выполнить ли переоткрытие инвентаря
     */
    public default boolean reopenCondition(InventoryPage page, boolean forceUpdate) {
        return false;
    }
}
