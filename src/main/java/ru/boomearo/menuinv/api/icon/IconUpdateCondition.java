package ru.boomearo.menuinv.api.icon;

import ru.boomearo.menuinv.api.InventoryPage;

@FunctionalInterface
public interface IconUpdateCondition {

    boolean shouldUpdate(InventoryPage consume);

}
