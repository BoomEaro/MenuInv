package ru.boomearo.menuinv.api.icon;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.session.InventorySession;

public class DummyIconHandler<SESSION extends InventorySession> extends IconHandler<SESSION> {

    @Override
    public long onUpdateTime(InventoryPage<SESSION> data, boolean force) {
        if (force) {
            return 0;
        }
        return 250;
    }

    @Override
    public ItemStack onUpdate(InventoryPage<SESSION> consume, Player player) {
        return null;
    }

    @Override
    public void onClick(InventoryPage<SESSION> page, Player player, ClickType click) {

    }
}
