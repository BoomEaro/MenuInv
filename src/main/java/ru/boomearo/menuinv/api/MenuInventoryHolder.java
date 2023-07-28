package ru.boomearo.menuinv.api;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MenuInventoryHolder implements InventoryHolder {

    private final InventoryPageImpl page;

    public MenuInventoryHolder(InventoryPageImpl page) {
        this.page = page;
    }

    @Override
    public Inventory getInventory() {
        return this.page.getInventory();
    }

    public InventoryPageImpl getPage() {
        return this.page;
    }

}
