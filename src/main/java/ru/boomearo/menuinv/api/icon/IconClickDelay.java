package ru.boomearo.menuinv.api.icon;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import ru.boomearo.menuinv.api.InventoryPage;

import java.time.Duration;

@FunctionalInterface
public interface IconClickDelay {

   Duration getClickTime(InventoryPage page, Player player, ClickType click);

}
