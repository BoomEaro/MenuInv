package ru.boomearo.menuinv.objects;

import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.IconHandler;
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

    public ItemStack getItemStack(InventoryPageImpl page, boolean force) {
        /*
         * Проверяем в самом начале, первый ли раз вызывается извлечение предмета
         * Если это так, получаем обновленный предмет и возвращаем его.
         * В дальнейшем, будет ли обновление, зависит от реализации обработчика IconHandler
         */
        if (this.firstUpdate) {
            ItemStack newItem = getUpdatedItem(page);

            this.cache = newItem;

            this.firstUpdate = false;

            return newItem;
        }

        if (this.handler.shouldUpdate() && (((System.currentTimeMillis() - this.updateHandlerCooldown) > (this.handler.getUpdateTime() * 50)) || force)) {
            this.updateHandlerCooldown = System.currentTimeMillis();

            ItemStack newItem = getUpdatedItem(page);

            this.cache = newItem;

            return newItem;
        }

        return this.cache;
    }

    private ItemStack getUpdatedItem(InventoryPageImpl page) {

        ItemStack item = null;
        try {
            item = this.handler.onUpdate(page, page.getPlayer());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return item;
    }
}
