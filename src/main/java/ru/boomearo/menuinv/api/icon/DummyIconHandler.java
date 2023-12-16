package ru.boomearo.menuinv.api.icon;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.InventoryPage;

import java.time.Duration;

public class DummyIconHandler extends IconHandler {

    public static final DummyIconHandler INSTANCE = new DummyIconHandler();

    @Override
    public Duration onUpdateTime(InventoryPage data, boolean force) {
        if (force) {
            return Duration.ZERO;
        }
        return Duration.ofMillis(250);
    }

    @Override
    public ItemStack onUpdate(InventoryPage consume, Player player) {
        return null;
    }

    @Override
    public void onClick(InventoryPage page, Player player, ClickType click) {

    }
}
