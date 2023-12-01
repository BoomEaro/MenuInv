package ru.boomearo.menuinv.api;

import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import ru.boomearo.menuinv.api.session.InventorySession;

@Getter
public class MenuInventoryHolder<SESSION extends InventorySession> implements InventoryHolder {

    private final InventoryPageImpl<SESSION> page;

    public MenuInventoryHolder(InventoryPageImpl<SESSION> page) {
        this.page = page;
    }

    @Override
    public Inventory getInventory() {
        return this.page.getInventory();
    }

}
