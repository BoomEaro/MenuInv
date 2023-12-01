package ru.boomearo.menuinv.api;

import ru.boomearo.menuinv.api.session.InventorySession;

public class DefaultUpdateDelay<SESSION extends InventorySession> implements Delayable<InventoryPage<SESSION>> {

    @Override
    public long onUpdateTime(InventoryPage<SESSION> inventoryPage, boolean force) {
        // If force then update immediately
        if (force) {
            return 0;
        }

        return 250;
    }
}
