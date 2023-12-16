package ru.boomearo.menuinv.api;

import java.time.Duration;

public class DefaultUpdateDelay implements Delayable<InventoryPage> {

    @Override
    public Duration onUpdateTime(InventoryPage inventoryPage, boolean force) {
        // If force then update immediately
        if (force) {
            return Duration.ZERO;
        }

        return Duration.ofMillis(250);
    }
}
