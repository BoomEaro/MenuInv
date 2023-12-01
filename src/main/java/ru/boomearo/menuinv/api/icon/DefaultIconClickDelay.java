package ru.boomearo.menuinv.api.icon;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.session.InventorySession;

public class DefaultIconClickDelay<SESSION extends InventorySession> implements IconClickDelay<SESSION> {

    @Override
    public long getClickTime(InventoryPage<SESSION> page, Player player, ClickType click) {
        return 250;
    }

}
