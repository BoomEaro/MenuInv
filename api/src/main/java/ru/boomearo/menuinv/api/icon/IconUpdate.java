package ru.boomearo.menuinv.api.icon;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.InventoryPage;

import javax.annotation.Nullable;

@FunctionalInterface
public interface IconUpdate {

    @Nullable
    ItemStack onUpdate(@NonNull InventoryPage consume, @NonNull Player player);

}
