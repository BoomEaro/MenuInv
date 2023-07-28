package ru.boomearo.menuinv.api.icon.scrolls;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.InventoryPage;

public interface ScrollUpdate {

    ItemStack onUpdate(InventoryPage inventoryPage, Player player, ScrollType scrollType, int currentPage, int maxPage);

}
