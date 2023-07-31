package ru.boomearo.menuinv.api.icon;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import ru.boomearo.menuinv.api.InventoryPage;

public class DefaultIconClickDelay implements IconClickDelay {

    @Override
    public long getClickTime(InventoryPage page, Player player, ClickType click) {
        return 250;
    }

}
