package ru.boomearo.menuinv.api;

import org.bukkit.inventory.ItemStack;

/**
 * Представляет активный предмет с позицией, активно используемый для обработки инвентаря
 */
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

        if (this.handler.shouldUpdate(page) && (((System.currentTimeMillis() - this.updateHandlerCooldown) > this.handler.getUpdateTime(page)) || force)) {
            this.updateHandlerCooldown = System.currentTimeMillis();

            ItemStack newItem = getUpdatedItem(page);

            this.cache = newItem;

            return newItem;
        }

        return this.cache;
    }

    private ItemStack getUpdatedItem(InventoryPageImpl page) {
        return this.handler.onUpdate(page, page.getPlayer());
    }
}
