package ru.boomearo.menuinv.api.icon;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.session.InventorySession;

@FunctionalInterface
public interface BottomInventoryClickHandler<SESSION extends InventorySession> {

    boolean canClick(InventoryPage<SESSION> inventoryPage, Player player, int slot, ClickType clickType);

}
