package ru.boomearo.menuinv.api.frames;

import org.bukkit.entity.Player;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.icon.IconHandler;
import ru.boomearo.menuinv.api.session.InventorySession;

import java.util.List;

@FunctionalInterface
public interface PagedIconsUpdate<SESSION extends InventorySession> {

    List<IconHandler<SESSION>> onUpdate(InventoryPage<SESSION> inventoryPage, Player player);

}
