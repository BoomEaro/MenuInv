package ru.boomearo.menuinv.objects;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MenuInvHolder implements InventoryHolder {

    private final InventoryPage page;

    public MenuInvHolder(InventoryPage page) {
        this.page = page;
    }

    @Override
    public Inventory getInventory() {
        return this.page.getInventory();
    }

    public InventoryPage getPage() {
        return this.page;
    }

}
