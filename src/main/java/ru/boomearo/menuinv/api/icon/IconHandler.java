package ru.boomearo.menuinv.api.icon;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.Updatable;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public abstract class IconHandler implements Updatable<ItemStack, InventoryPage>, Comparable<IconHandler> {

    private static final Map<String, Long> CLICK_COOLDOWN = new HashMap<>();

    public abstract void onClick(InventoryPage page, Player player, ClickType click);

    public Duration getClickTime(InventoryPage page, Player player, ClickType click) {
        return Duration.ofMillis(250);
    }

    public void handleClick(InventoryPage page, Player player, ClickType click) {
        Duration duration = getClickTime(page, player, click);
        if (duration == null) {
            duration = Duration.ZERO;
        }

        if (hasClicked(player.getName(), duration.toMillis())) {
            CLICK_COOLDOWN.put(player.getName(), System.currentTimeMillis());

            onClick(page, player, click);
        }
    }

    private static boolean hasClicked(String name, long cd) {
        Long value = CLICK_COOLDOWN.get(name);
        if (value == null) {
            return true;
        }

        if ((System.currentTimeMillis() - value) > cd) {
            CLICK_COOLDOWN.remove(name);
            return true;
        }

        return false;

    }

    @Override
    public int compareTo(IconHandler other) {
        return 1;
    }

}
