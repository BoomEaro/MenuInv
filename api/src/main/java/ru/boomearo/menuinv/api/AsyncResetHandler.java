package ru.boomearo.menuinv.api;

import lombok.NonNull;

@FunctionalInterface
public interface AsyncResetHandler {

    boolean onIconReset(@NonNull InventoryPage page, boolean force);

}
