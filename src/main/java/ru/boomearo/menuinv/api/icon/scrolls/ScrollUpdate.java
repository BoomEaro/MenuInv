package ru.boomearo.menuinv.api.icon.scrolls;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.session.InventorySession;

@FunctionalInterface
public interface ScrollUpdate<SESSION extends InventorySession> {

    ItemStack onUpdate(InventoryPage<SESSION> inventoryPage, Player player, ScrollType scrollType, int currentPage, int maxPage);

}
