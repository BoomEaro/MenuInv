package ru.boomearo.menuinv.api.icon;

import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.InventoryPageImpl;
import ru.boomearo.menuinv.api.SlotElement;

public class ItemIcon extends SlotElement {

    private long updateHandlerCooldown = System.currentTimeMillis();

    private ItemStack cache = null;
    private boolean firstUpdate = true;

    private final IconHandler handler;

    public ItemIcon(int position, IconHandler handler) {
        super(position);
        this.handler = handler;
    }

    public IconHandler getHandler() {
        return this.handler;
    }

    public ItemStack getItemStack(InventoryPageImpl page, boolean force, boolean create, UpdateExceptionHandler updateExceptionHandler) {
        /*
         * We check at the very beginning whether the object is being retrieved for the first time
         * If so, get the upgraded item and return it.
         * In the future, whether there will be an update depends on the implementation of the IconHandler handler
         */
        if (this.firstUpdate) {
            ItemStack newItem = getUpdatedItem(page, updateExceptionHandler);

            this.cache = newItem;

            this.firstUpdate = false;

            return newItem;
        }

        if (this.handler.canUpdate(page, force, this.updateHandlerCooldown) || create) {
            this.updateHandlerCooldown = System.currentTimeMillis();

            ItemStack newItem = getUpdatedItem(page, updateExceptionHandler);

            this.cache = newItem;

            return newItem;
        }

        return this.cache;
    }

    private ItemStack getUpdatedItem(InventoryPageImpl page, UpdateExceptionHandler updateExceptionHandler) {
        try {
            return this.handler.onUpdate(page, page.getPlayer());
        } catch (Exception e) {
            updateExceptionHandler.onException(page, page.getPlayer(), e);
            return null;
        }
    }
}
