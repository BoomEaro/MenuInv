package ru.boomearo.menuinv.api.icon;

import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.InventoryPageImpl;
import ru.boomearo.menuinv.api.SlotElement;

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

    public ItemStack getItemStack(InventoryPageImpl page, boolean force, UpdateExceptionHandler updateExceptionHandler) {
        /*
         * Проверяем в самом начале, первый ли раз вызывается извлечение предмета
         * Если это так, получаем обновленный предмет и возвращаем его.
         * В дальнейшем, будет ли обновление, зависит от реализации обработчика IconHandler
         */
        if (this.firstUpdate) {
            ItemStack newItem = getUpdatedItem(page, updateExceptionHandler);

            this.cache = newItem;

            this.firstUpdate = false;

            return newItem;
        }

        if (this.handler.shouldUpdate(page) && (((System.currentTimeMillis() - this.updateHandlerCooldown) > this.handler.getUpdateTime(page)) || force)) {
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
        }
        catch (Exception e) {
            updateExceptionHandler.onException(page, page.getPlayer(), e);
            return null;
        }
    }
}
