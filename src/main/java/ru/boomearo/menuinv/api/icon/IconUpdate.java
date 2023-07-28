package ru.boomearo.menuinv.api.icon;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.InventoryPage;

public interface IconUpdate {

    ItemStack onUpdate(InventoryPage consume, Player player);

}
