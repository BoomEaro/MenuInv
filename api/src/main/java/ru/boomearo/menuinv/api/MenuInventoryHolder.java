package ru.boomearo.menuinv.api;

import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@Getter
public class MenuInventoryHolder implements InventoryHolder {

    private final InventoryPageImpl page;

    public MenuInventoryHolder(InventoryPageImpl page) {
        this.page = page;
    }

    @Override
    public Inventory getInventory() {
        return this.page.getInventory();
    }

}
