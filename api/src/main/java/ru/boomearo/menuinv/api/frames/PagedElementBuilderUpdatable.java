package ru.boomearo.menuinv.api.frames;

import lombok.NonNull;
import ru.boomearo.menuinv.api.Delayable;
import ru.boomearo.menuinv.api.InventoryPage;

public interface PagedElementBuilderUpdatable<T> extends PagedElementBuilder {

    @NonNull
    T setUpdateDelay(@NonNull Delayable<InventoryPage> updateDelay);

}
