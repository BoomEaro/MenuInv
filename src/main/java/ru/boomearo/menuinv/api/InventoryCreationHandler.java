package ru.boomearo.menuinv.api;

/**
 * Обработчик создания нового баккитовского инвентаря.
 */
public interface InventoryCreationHandler {

    /**
     * Создает новый тайтл для инвентаря
     *
     * @param page Страница, в которой произошло создание тайтла
     * @return Новый тайтл который будет применен в баккитовском инвентаре
     */
    String createTitle(InventoryPage page);

}
