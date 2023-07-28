package ru.boomearo.menuinv.api.icon;

import ru.boomearo.menuinv.api.InventoryPage;

public interface IconUpdateCondition {

    boolean shouldUpdate(InventoryPage consume);

}
