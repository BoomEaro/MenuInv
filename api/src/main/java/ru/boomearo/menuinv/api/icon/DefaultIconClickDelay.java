package ru.boomearo.menuinv.api.icon;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import ru.boomearo.menuinv.api.InventoryPage;

import javax.annotation.Nullable;
import java.time.Duration;

public class DefaultIconClickDelay implements IconClickDelay {

    @Nullable
    @Override
    public Duration getClickTime(@NonNull InventoryPage page, @NonNull Player player, @NonNull ClickType click) {
        return Duration.ofMillis(250);
    }

}
