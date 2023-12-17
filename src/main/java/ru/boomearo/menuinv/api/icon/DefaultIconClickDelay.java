package ru.boomearo.menuinv.api.icon;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import ru.boomearo.menuinv.api.InventoryPage;

import java.time.Duration;

public class DefaultIconClickDelay implements IconClickDelay {

    @Override
    public Duration getClickTime(InventoryPage page, Player player, ClickType click) {
        return Duration.ofMillis(250);
    }

}
