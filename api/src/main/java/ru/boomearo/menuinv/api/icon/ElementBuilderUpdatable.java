package ru.boomearo.menuinv.api.icon;

import lombok.NonNull;
import ru.boomearo.menuinv.api.Delayable;
import ru.boomearo.menuinv.api.InventoryPage;

public interface ElementBuilderUpdatable<T> extends ElementBuilder {

    @NonNull
    T setUpdateDelay(@NonNull Delayable<InventoryPage> updateDelay);

}
