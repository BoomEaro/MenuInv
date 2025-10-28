package ru.boomearo.menuinv.api;

import lombok.NonNull;

import javax.annotation.Nullable;

@FunctionalInterface
public interface InventoryTitleHandler {

    @Nullable
    String createTitle(@NonNull InventoryPage page);

}
