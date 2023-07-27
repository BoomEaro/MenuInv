package ru.boomearo.menuinv.api.session;

import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.InventoryPage;

/**
 * Представляет информацию для подтверждения каких-то действий. Используется для меню подтверждения.
 */
public interface ConfirmData {

    void executeConfirm(InventoryPage page);

    void executeCancel(InventoryPage page);

    ItemStack getConfirmItem(InventoryPage page);

    ItemStack getCancelItem(InventoryPage page);

    String getInventoryName(InventorySession session);

}
