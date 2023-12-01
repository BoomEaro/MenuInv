package ru.boomearo.menuinv.api.icon;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.session.InventorySession;

@FunctionalInterface
public interface IconUpdate<SESSION extends InventorySession> {

    ItemStack onUpdate(InventoryPage<SESSION> consume, Player player);

}
