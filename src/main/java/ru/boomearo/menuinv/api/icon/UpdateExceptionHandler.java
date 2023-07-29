package ru.boomearo.menuinv.api.icon;

import org.bukkit.entity.Player;
import ru.boomearo.menuinv.api.InventoryPage;

@FunctionalInterface
public interface UpdateExceptionHandler {

    void onException(InventoryPage inventoryPage, Player player, Exception exception);

}
