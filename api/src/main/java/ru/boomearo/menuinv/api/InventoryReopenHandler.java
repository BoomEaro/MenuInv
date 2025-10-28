package ru.boomearo.menuinv.api;

import lombok.NonNull;

@FunctionalInterface
public interface InventoryReopenHandler {

    boolean reopenCondition(@NonNull InventoryPage page, boolean forceUpdate);
}
