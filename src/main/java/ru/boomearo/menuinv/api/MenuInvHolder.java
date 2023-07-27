package ru.boomearo.menuinv.api;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MenuInvHolder implements InventoryHolder {

    private final InventoryPageImpl page;

    public MenuInvHolder(InventoryPageImpl page) {
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
