package ru.boomearo.menuinv.api;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface InventoryCloseHandler {

    void onClose(InventoryPage inventoryPage, Player player);

}
