package ru.boomearo.menuinv.api.icon;

import ru.boomearo.menuinv.api.Delayable;
import ru.boomearo.menuinv.api.InventoryPage;

public interface ElementBuilderUpdatable<T> extends ElementBuilder {

    T setUpdateDelay(Delayable<InventoryPage> updateDelay);

}
