package ru.boomearo.menuinv.api;

public interface InventoryReopenHandler {

    /**
     * Условие, используемое для переоткрытия инвентаря
     *
     * @param page        Страница, в которой произошла проверка условия
     * @param forceUpdate Является ли вызов принудительным
     * @return Выполнить ли переоткрытие инвентаря
     */
    boolean reopenCondition(InventoryPage page, boolean forceUpdate);
}
