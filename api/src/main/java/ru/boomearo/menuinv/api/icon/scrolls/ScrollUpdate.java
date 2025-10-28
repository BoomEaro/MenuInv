package ru.boomearo.menuinv.api.icon.scrolls;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.InventoryPage;

import javax.annotation.Nullable;

@FunctionalInterface
public interface ScrollUpdate {

    @Nullable
    ItemStack onUpdate(@NonNull InventoryPage inventoryPage, @NonNull Player player, @NonNull ScrollType scrollType, int currentPage, int maxPage);

}
