package ru.boomearo.menuinv.objects;

import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.IconHandler;
import ru.boomearo.menuinv.api.SlotElement;

public class ItemIcon extends SlotElement {

    private long updateHandlerCooldown = 0;
    private ItemStack cache = null;

    private final IconHandler handler;

    public ItemIcon(int position, IconHandler handler) {
        super(position);
        this.handler = handler;
    }

    public IconHandler getHandler() {
        return this.handler;
    }

    public ItemStack getItemStack(InventoryPageImpl page, boolean force) {
        IconHandler handler = this.handler;

        if (((System.currentTimeMillis() - this.updateHandlerCooldown) > (handler.getUpdateTime() * 50)) || force) {
            this.updateHandlerCooldown = System.currentTimeMillis();

            ItemStack item = null;
            try {
                item = handler.onUpdate(page, page.getPlayer());
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            this.cache = item;

            return item;
        }

        return cache;
    }

}
