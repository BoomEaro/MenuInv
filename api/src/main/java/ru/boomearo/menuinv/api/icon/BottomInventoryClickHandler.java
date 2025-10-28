package ru.boomearo.menuinv.api.icon;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import ru.boomearo.menuinv.api.InventoryPage;

@FunctionalInterface
public interface BottomInventoryClickHandler {

    boolean canClick(@NonNull InventoryPage inventoryPage, @NonNull Player player, int slot, @NonNull ClickType clickType);

}
