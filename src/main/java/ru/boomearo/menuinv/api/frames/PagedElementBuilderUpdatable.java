package ru.boomearo.menuinv.api.frames;

import ru.boomearo.menuinv.api.Delayable;
import ru.boomearo.menuinv.api.InventoryPage;

public interface PagedElementBuilderUpdatable<T> extends PagedElementBuilder {

    T setUpdateDelay(Delayable<InventoryPage> updateDelay);

}
