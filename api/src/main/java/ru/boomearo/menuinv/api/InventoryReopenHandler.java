package ru.boomearo.menuinv.api;

public interface InventoryReopenHandler {

    boolean reopenCondition(InventoryPage page, boolean forceUpdate);
}
