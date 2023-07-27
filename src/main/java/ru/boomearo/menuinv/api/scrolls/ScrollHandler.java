package ru.boomearo.menuinv.api.scrolls;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.InventoryPage;

public interface ScrollHandler {

    void onClick(InventoryPage inventoryPage, Player player, ClickType clickType);

    ItemStack onVisible(InventoryPage inventoryPage, Player player, ScrollType scrollType, int currentPage, int maxPage);

    ItemStack onHide(InventoryPage inventoryPage, Player player, ScrollType scrollType, int currentPage, int maxPage);

}
