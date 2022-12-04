package ru.boomearo.menuinv.api;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Енум, представляющий все возможные инвентари с которым можно взаимодействовать
 */
public enum InvType {

    ANVIL(InventoryType.ANVIL, 3, 1),
    BEACON(InventoryType.BEACON, 1, 1),
    FURNACE(InventoryType.FURNACE, 3, 1),
    BREWING(InventoryType.BREWING, 5, 1),
    ENCHANTING(InventoryType.ENCHANTING, 5, 1),
    //Причина, по которой именно 10 в том, что иначе не получится засунуть предмет в результат..
    //Жертвуя высотой, хоть как то можно решить эту проблему.
    WORKBENCH(InventoryType.WORKBENCH, 10, 1),
    CHEST_9X1(InventoryType.CHEST, 9, 1),
    CHEST_9X2(InventoryType.CHEST, 9, 2),
    CHEST_9X3(InventoryType.CHEST, 9, 3),
    CHEST_9X4(InventoryType.CHEST, 9, 4),
    CHEST_9X5(InventoryType.CHEST, 9, 5),
    CHEST_9X6(InventoryType.CHEST, 9, 6),
    HOPPER(InventoryType.HOPPER, 5, 1),
    DROPPER(InventoryType.DROPPER, 3, 3);

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

    public int getSize() {
        return this.width * this.height;
    }

    public Inventory createInventory(InventoryHolder holder, String title) {
        if (title == null) {
            title = " ";
        }
        if (this.type == InventoryType.CHEST) {
            return Bukkit.createInventory(holder, getSize(), title);
        }

        return Bukkit.createInventory(holder, this.type, title);
    }
}
