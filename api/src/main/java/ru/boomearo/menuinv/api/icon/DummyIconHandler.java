package ru.boomearo.menuinv.api.icon;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.Delayable;
import ru.boomearo.menuinv.api.InfinityUpdateDelay;
import ru.boomearo.menuinv.api.InventoryPage;

import javax.annotation.Nullable;
import java.time.Duration;

public class DummyIconHandler extends IconHandler {

    public static final DummyIconHandler INSTANCE = new DummyIconHandler();

    private final Delayable<InventoryPage> delayable = new InfinityUpdateDelay<>();

    @Nullable
    @Override
    public Duration onUpdateTime(@NonNull InventoryPage data, boolean force) {
        return this.delayable.onUpdateTime(data, force);
    }

    @Nullable
    @Override
    public ItemStack onUpdate(@NonNull InventoryPage consume, @NonNull Player player) {
        return null;
    }

    @Override
    public void onClick(@NonNull InventoryPage page, @NonNull ItemIcon icon, @NonNull Player player, @NonNull ClickType click) {

    }
}
