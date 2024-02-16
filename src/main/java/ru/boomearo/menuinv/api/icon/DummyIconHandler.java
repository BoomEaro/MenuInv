package ru.boomearo.menuinv.api.icon;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.Delayable;
import ru.boomearo.menuinv.api.InfinityUpdateDelay;
import ru.boomearo.menuinv.api.InventoryPage;

import java.time.Duration;

public class DummyIconHandler extends IconHandler {

    public static final DummyIconHandler INSTANCE = new DummyIconHandler();

    private final Delayable<InventoryPage> delayable = new InfinityUpdateDelay<>();

    @Override
    public Duration onUpdateTime(InventoryPage data, boolean force) {
        return this.delayable.onUpdateTime(data, force);
    }

    @Override
    public ItemStack onUpdate(InventoryPage consume, Player player) {
        return null;
    }

    @Override
    public void onClick(InventoryPage page, ItemIcon icon, Player player, ClickType click) {

    }
}
