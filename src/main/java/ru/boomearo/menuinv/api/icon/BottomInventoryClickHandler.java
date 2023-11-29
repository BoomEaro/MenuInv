package ru.boomearo.menuinv.api.icon;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import ru.boomearo.menuinv.api.InventoryPage;

public interface BottomInventoryClickHandler {

    boolean canClick(InventoryPage inventoryPage, Player player, ClickType clickType);

}
