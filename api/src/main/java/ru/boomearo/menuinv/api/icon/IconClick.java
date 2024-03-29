package ru.boomearo.menuinv.api.icon;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import ru.boomearo.menuinv.api.InventoryPage;

@FunctionalInterface
public interface IconClick {

    void onClick(InventoryPage page, ItemIcon icon, Player player, ClickType click);

}
