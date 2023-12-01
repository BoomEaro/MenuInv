package ru.boomearo.menuinv.api;

import ru.boomearo.menuinv.api.session.InventorySession;

public interface InventoryReopenHandler<SESSION extends InventorySession> {

    boolean reopenCondition(InventoryPage<SESSION> page, boolean forceUpdate);
}
