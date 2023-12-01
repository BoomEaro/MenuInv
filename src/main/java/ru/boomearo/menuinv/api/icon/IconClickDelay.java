package ru.boomearo.menuinv.api.icon;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.session.InventorySession;

@FunctionalInterface
public interface IconClickDelay<SESSION extends InventorySession> {

   long getClickTime(InventoryPage<SESSION> page, Player player, ClickType click);

}
