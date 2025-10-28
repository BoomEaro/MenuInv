package ru.boomearo.menuinv.api.icon;

import lombok.NonNull;
import org.bukkit.entity.Player;
import ru.boomearo.menuinv.api.InventoryPage;

@FunctionalInterface
public interface UpdateExceptionHandler {

    void onException(@NonNull InventoryPage inventoryPage, @NonNull Player player, @NonNull Exception exception);

}
