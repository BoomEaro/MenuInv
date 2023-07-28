package ru.boomearo.menuinv.api;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public interface ClickExceptionHandler {

    void onException(InventoryPage inventoryPage, Player player, ClickType clickType, Exception exception);

}
