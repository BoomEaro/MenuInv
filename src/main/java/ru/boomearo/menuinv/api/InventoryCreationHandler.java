package ru.boomearo.menuinv.api;

import ru.boomearo.menuinv.api.session.InventorySession;

/**
 * Обработчик создания тайтла для страницы
 */
public interface InventoryCreationHandler {

    public String createTitle(InventorySession session);

}
