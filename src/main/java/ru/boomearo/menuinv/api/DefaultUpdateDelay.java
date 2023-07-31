package ru.boomearo.menuinv.api;

public class DefaultUpdateDelay implements Delayable<InventoryPage> {

    @Override
    public long onUpdateTime(InventoryPage inventoryPage, boolean force) {
        // If force then update immediately
        if (force) {
            return 0;
        }

        return 250;
    }
}
