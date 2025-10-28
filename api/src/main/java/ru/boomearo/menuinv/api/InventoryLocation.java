package ru.boomearo.menuinv.api;

import lombok.NonNull;

public record InventoryLocation(int x, int z) {

    @NonNull
    public static InventoryLocation of(int x, int z) {
        return new InventoryLocation(x, z);
    }
}
