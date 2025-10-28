package ru.boomearo.menuinv.api;

import lombok.NonNull;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public record MenuInventoryHolder(@NonNull InventoryPageImpl page) implements InventoryHolder {

    @Override
    public Inventory getInventory() {
        return this.page.getInventory();
    }

}
