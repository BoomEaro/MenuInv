package ru.boomearo.menuinv.api.session;

import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.InventoryPage;

public interface ConfirmData {

    void executeConfirm(InventoryPage<?> page);

    void executeCancel(InventoryPage<?> page);

    ItemStack getConfirmItem(InventoryPage<?> page);

    ItemStack getCancelItem(InventoryPage<?> page);

    String getInventoryName(InventorySession session);

}
