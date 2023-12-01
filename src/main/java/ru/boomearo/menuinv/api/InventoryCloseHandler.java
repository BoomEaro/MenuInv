package ru.boomearo.menuinv.api;

import org.bukkit.entity.Player;
import ru.boomearo.menuinv.api.session.InventorySession;

@FunctionalInterface
public interface InventoryCloseHandler<SESSION extends InventorySession> {

    void onClose(InventoryPage<SESSION> inventoryPage, Player player);

}
