package ru.boomearo.menuinv.api.icon;

import ru.boomearo.menuinv.api.InventoryPage;

@FunctionalInterface
public interface IconUpdateDelay {

    long getUpdateTime(InventoryPage inventoryPage);

}
