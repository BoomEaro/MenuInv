package ru.boomearo.menuinv.api.icon;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import ru.boomearo.menuinv.api.InventoryPage;

@FunctionalInterface
public interface ClickExceptionHandler {

    void onException(@NonNull InventoryPage inventoryPage, @NonNull Player player, @NonNull ClickType clickType, @NonNull Exception exception);

}
