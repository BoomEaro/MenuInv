package ru.boomearo.menuinv.objects;

import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.AbstractButtonHandler;
import ru.boomearo.menuinv.api.TemplateItemIcon;

public class ItemIcon extends TemplateItemIcon {

    private long updateHandlerCooldown = 0;
    private ItemStack cache = null;

    public ItemIcon(int position, AbstractButtonHandler handler) {
        super(position, handler);
    }

    public ItemStack getItemStack(InventoryPage page, boolean force) {
        AbstractButtonHandler handler = getHandler();

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
