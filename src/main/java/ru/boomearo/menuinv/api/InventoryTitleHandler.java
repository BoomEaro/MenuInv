package ru.boomearo.menuinv.api;

import ru.boomearo.menuinv.api.session.InventorySession;

@FunctionalInterface
public interface InventoryTitleHandler<SESSION extends InventorySession> {

    String createTitle(InventoryPage<SESSION> page);

}
