package ru.boomearo.menuinv.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IconUpdate {

    ItemStack onUpdate(InventoryPage consume, Player player);

}
