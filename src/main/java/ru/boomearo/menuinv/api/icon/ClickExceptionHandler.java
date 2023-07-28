package ru.boomearo.menuinv.api.icon;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import ru.boomearo.menuinv.api.InventoryPage;

public interface ClickExceptionHandler {

    void onException(InventoryPage inventoryPage, Player player, ClickType clickType, Exception exception);

}
