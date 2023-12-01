package ru.boomearo.menuinv.api.icon;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.session.InventorySession;

@FunctionalInterface
public interface ClickExceptionHandler<SESSION extends InventorySession> {

    void onException(InventoryPage<SESSION> inventoryPage, Player player, ClickType clickType, Exception exception);

}
