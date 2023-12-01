package ru.boomearo.menuinv.api.icon;

import org.bukkit.entity.Player;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.session.InventorySession;

@FunctionalInterface
public interface UpdateExceptionHandler<SESSION extends InventorySession> {

    void onException(InventoryPage<SESSION> inventoryPage, Player player, Exception exception);

}
