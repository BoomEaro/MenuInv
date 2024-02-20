package ru.boomearo.menuinv.api.icon;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.InventoryPageImpl;
import ru.boomearo.menuinv.api.SlotElement;

@Getter
public class ItemIconImpl extends SlotElement implements ItemIcon {

    private long updateHandlerCooldown = System.currentTimeMillis();

    private boolean handlerChangeForceUpdate = true;
    private boolean forceUpdate = false;

    private IconHandler iconHandler;

    public ItemIconImpl(int position, IconHandler iconHandler) {
        super(position);
        this.iconHandler = iconHandler;
    }

    public void setIconHandler(IconHandler iconHandler) {
        Preconditions.checkArgument(iconHandler != null, "iconHandler is null!");

        this.iconHandler = iconHandler;
        this.handlerChangeForceUpdate = true;
    }

    public ItemStack getItemStack(InventoryPageImpl page, boolean force, boolean create, UpdateExceptionHandler updateExceptionHandler) {
        /*
         * We check at the very beginning whether the object is being retrieved for the first time
         * If so, get the updated item and return it.
         * In the future, whether there will be an update depends on the implementation of the IconHandler handler
         */
        if (this.handlerChangeForceUpdate) {
            this.handlerChangeForceUpdate = false;

            return getUpdatedItem(page, updateExceptionHandler);
        }

        boolean updateForce = this.forceUpdate || force;

        try {
            if (this.iconHandler.canUpdate(page, updateForce, this.updateHandlerCooldown) || create) {
                this.updateHandlerCooldown = System.currentTimeMillis();

                return getUpdatedItem(page, updateExceptionHandler);
            }
        }
        finally {
            this.forceUpdate = false;
        }

        return null;
    }

    private ItemStack getUpdatedItem(InventoryPageImpl page, UpdateExceptionHandler updateExceptionHandler) {
        try {
            ItemStack itemStack = this.iconHandler.onUpdate(page, page.getPlayer());
            if (itemStack != null) {
                return itemStack;
            }

            return new ItemStack(Material.AIR, 1);
        } catch (Exception e) {
            updateExceptionHandler.onException(page, page.getPlayer(), e);
            return null;
        }
    }

    @Override
    public void forceUpdate() {
        this.forceUpdate = true;
    }
}
