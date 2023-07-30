package ru.boomearo.menuinv.api.frames;

import org.bukkit.entity.Player;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.icon.IconHandler;

import java.util.List;

public interface PagedIconsUpdate {

    List<IconHandler> onUpdate(InventoryPage inventoryPage, Player player);

}
