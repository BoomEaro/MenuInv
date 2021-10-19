package ru.boomearo.menuinv.api;

import org.bukkit.event.inventory.InventoryType;

//TODO добавить больше типов инвентарей и сделать масштабирование
public enum InvType {

    Chest(InventoryType.CHEST, 9, 6),
    Hopper(InventoryType.HOPPER, 5, 1),
    Dropper(InventoryType.DROPPER, 3, 3);

    private final InventoryType type;
    private final int maxWidth;
    private final int maxHeight;

    InvType(InventoryType type, int maxWidth, int maxHeight) {
        this.type = type;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    public InventoryType getInventoryType() {
        return this.type;
    }

    public int getMaxWidth() {
        return this.maxWidth;
    }

    public int getMaxHeight() {
        return this.maxHeight;
    }

    public int getMaxSize() {
        return this.maxWidth * this.maxHeight;
    }
}
