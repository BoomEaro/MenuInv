package ru.boomearo.menuinv.api.icon;

import ru.boomearo.menuinv.api.Delayable;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.session.InventorySession;

public interface ElementBuilderUpdatable<T, SESSION extends InventorySession> extends ElementBuilder<SESSION> {

    T setUpdateDelay(Delayable<InventoryPage<SESSION>> updateDelay);

}
