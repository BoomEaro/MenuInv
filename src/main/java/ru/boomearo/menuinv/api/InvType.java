package ru.boomearo.menuinv.api;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public enum InvType {

    Chest_9X1(InventoryType.CHEST, 9, 1),
    Chest_9X2(InventoryType.CHEST, 9, 2),
    Chest_9X3(InventoryType.CHEST, 9, 3),
    Chest_9X4(InventoryType.CHEST, 9, 4),
    Chest_9X5(InventoryType.CHEST, 9, 5),
    Chest_9X6(InventoryType.CHEST, 9, 6),

    Hopper(InventoryType.HOPPER, 5, 1),
    Dropper(InventoryType.DROPPER, 3, 3);

    private final InventoryType type;
    private final int width;
    private final int height;

    InvType(InventoryType type, int width, int height) {
        this.type = type;
        this.width = width;
        this.height = height;
    }

    public InventoryType getInventoryType() {
        return this.type;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getMaxSize() {
        return this.width * this.height;
    }

    public Inventory createInventory(InventoryHolder holder, String title) {
        if (this.type == InventoryType.CHEST) {
            return Bukkit.createInventory(holder,getHeight() * getWidth(), title);
        }

        return Bukkit.createInventory(holder, this.type, title);
    }
}
