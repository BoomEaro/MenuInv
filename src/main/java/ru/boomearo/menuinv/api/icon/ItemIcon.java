package ru.boomearo.menuinv.api.icon;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.InventoryPageImpl;
import ru.boomearo.menuinv.api.SlotElement;
import ru.boomearo.menuinv.api.session.InventorySession;

@Getter
public class ItemIcon<SESSION extends InventorySession> extends SlotElement {

    private long updateHandlerCooldown = System.currentTimeMillis();

    private boolean forceUpdate = true;

    private IconHandler<SESSION> handler;

    public ItemIcon(int position, IconHandler<SESSION> handler) {
        super(position);
        this.handler = handler;
    }

    public void setHandler(IconHandler<SESSION> handler) {
        this.handler = handler;
        this.forceUpdate = true;
    }

    public ItemStack getItemStack(InventoryPageImpl<SESSION> page, boolean force, boolean create, UpdateExceptionHandler<SESSION> updateExceptionHandler) {
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

    private ItemStack getUpdatedItem(InventoryPageImpl<SESSION> page, UpdateExceptionHandler<SESSION> updateExceptionHandler) {
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
