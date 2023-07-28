package ru.boomearo.menuinv.api;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public interface UpdateExceptionHandler {

    void onException(InventoryPage inventoryPage, Player player, Exception exception);

}
