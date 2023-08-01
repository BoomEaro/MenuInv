package ru.boomearo.menuinv.api.icon;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.InventoryPageImpl;
import ru.boomearo.menuinv.api.SlotElement;

public class ItemIcon extends SlotElement {

    private long updateHandlerCooldown = System.currentTimeMillis();

    private boolean forceUpdate = true;

    private IconHandler handler;

    public ItemIcon(int position, IconHandler handler) {
        super(position);
        this.handler = handler;
    }

    public IconHandler getHandler() {
        return this.handler;
    }

    public void setHandler(IconHandler handler) {
        this.handler = handler;
        this.forceUpdate = true;
    }

    public ItemStack getItemStack(InventoryPageImpl page, boolean force, boolean create, UpdateExceptionHandler updateExceptionHandler) {
        /*
         * We check at the very beginning whether the object is being retrieved for the first time
         * If so, get the upgraded item and return it.
         * In the future, whether there will be an update depends on the implementation of the IconHandler handler
         */
        if (this.forceUpdate) {
            this.forceUpdate = false;

            return getUpdatedItem(page, updateExceptionHandler);
        }

        if (this.handler.canUpdate(page, force, this.updateHandlerCooldown) || create) {
            this.updateHandlerCooldown = System.currentTimeMillis();

            return getUpdatedItem(page, updateExceptionHandler);
        }

        return null;
    }

    private ItemStack getUpdatedItem(InventoryPageImpl page, UpdateExceptionHandler updateExceptionHandler) {
        try {
            ItemStack itemStack = this.handler.onUpdate(page, page.getPlayer());
            if (itemStack != null) {
                return itemStack;
            }

            return new ItemStack(Material.AIR, 1);
        } catch (Exception e) {
            updateExceptionHandler.onException(page, page.getPlayer(), e);
            return null;
        }
    }
}
