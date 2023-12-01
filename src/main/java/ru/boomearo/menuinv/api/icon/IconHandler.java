package ru.boomearo.menuinv.api.icon;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.Updatable;
import ru.boomearo.menuinv.api.session.InventorySession;

import java.util.HashMap;
import java.util.Map;

public abstract class IconHandler<SESSION extends InventorySession> implements Updatable<ItemStack, InventoryPage<SESSION>>, Comparable<IconHandler<SESSION>> {

    private static final Map<String, Long> CLICK_COOLDOWN = new HashMap<>();

    public abstract void onClick(InventoryPage<SESSION> page, Player player, ClickType click);

    public long getClickTime(InventoryPage<SESSION> page, Player player, ClickType click) {
        return 250;
    }

    public void handleClick(InventoryPage<SESSION> page, Player player, ClickType click) {
        if (hasClicked(player.getName(), getClickTime(page, player, click))) {
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
