package ru.boomearo.menuinv.api.icon;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import ru.boomearo.menuinv.api.InventoryPage;

@FunctionalInterface
public interface IconClick {

    void onClick(@NonNull InventoryPage page, @NonNull ItemIcon icon, @NonNull Player player, @NonNull ClickType click);

}
