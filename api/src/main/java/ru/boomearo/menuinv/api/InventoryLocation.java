package ru.boomearo.menuinv.api;

import lombok.Value;

@Value
public class InventoryLocation {

    int x;
    int z;

    public static InventoryLocation of(int x, int z) {
        return new InventoryLocation(x, z);
    }
}
