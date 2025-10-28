package ru.boomearo.menuinv.api;

import lombok.NonNull;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import javax.annotation.Nullable;

public interface InventoryFactory {

    @NonNull
    InventoryType getType();

    int getWidth();

    int getHeight();

    default int getSize() {
        return getWidth() * getHeight();
    }

    @NonNull
    Inventory createInventory(@NonNull InventoryPage inventoryPage,
                              @Nullable InventoryHolder holder,
                              @Nullable String title);

    @NonNull
    Inventory createInventory(@NonNull InventoryPage inventoryPage,
                              @Nullable InventoryHolder holder,
                              @Nullable Component title);

}
