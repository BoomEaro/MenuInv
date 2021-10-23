package ru.boomearo.menuinv.api;

/**
 * Обработчик создания тайтла для страницы
 */
public interface InventoryCreationHandler {

    public String createTitle(InventorySession session);

}
