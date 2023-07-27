package ru.boomearo.menuinv.api;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public interface IconClick {

    void onClick(InventoryPage page, Player player, ClickType click);

}
