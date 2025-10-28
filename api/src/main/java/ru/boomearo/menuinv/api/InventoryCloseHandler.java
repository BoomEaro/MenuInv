package ru.boomearo.menuinv.api;

import lombok.NonNull;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface InventoryCloseHandler {

    void onClose(@NonNull InventoryPage inventoryPage, @NonNull Player player);

}
