package ru.boomearo.menuinv.api.session;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.boomearo.menuinv.api.InventoryPage;

import java.util.Collections;

/**
 * Представляет информацию для подтверждения каких-то действий. Используется для меню подтверждения.
 */
public interface ConfirmData {

    public void executeConfirm(InventoryPage page);

    public void executeCancel(InventoryPage page);

    public ItemStack getConfirmItem(InventoryPage page);

    public default ItemStack getCancelItem(InventoryPage page) {
        ItemStack item = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§c§l✖ Отмена");

        meta.setLore(Collections.singletonList(
                "§f► Кликните чтобы вернуться обратно."
        ));

        item.setItemMeta(meta);

        return item;
    }

    public String getInventoryName(InventorySession session);

}
