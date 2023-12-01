package ru.boomearo.menuinv.api.icon;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.session.InventorySession;

@FunctionalInterface
public interface IconClick<SESSION extends InventorySession> {

    void onClick(InventoryPage<SESSION> page, Player player, ClickType click);

}
