package ru.boomearo.menuinv.api;

import org.bukkit.entity.Player;
import java.util.List;

public interface PagedItemsUpdate {

    List<IconHandler> onUpdate(InventoryPage inventoryPage, Player player);

}
