package ru.boomearo.menuinv.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@RequiredArgsConstructor
@Getter
public enum MenuType {

    ANVIL(InventoryType.ANVIL, 3, 1),
    BEACON(InventoryType.BEACON, 1, 1),
    BLAST_FURNACE(findInventoryType("BLAST_FURNACE"), 3, 1),
    FURNACE(InventoryType.FURNACE, 3, 1),
    BREWING(InventoryType.BREWING, 5, 1),
    CARTOGRAPHY(findInventoryType("CARTOGRAPHY"), 3, 1),
    ENCHANTING(InventoryType.ENCHANTING, 5, 1),
    LOOM(findInventoryType("LOOM"), 4, 1),
    SMITHING(findInventoryType("SMITHING"), 3, 1),
    SMOKER(findInventoryType("SMOKER"), 3, 1),
    STONECUTTER(findInventoryType("STONECUTTER"), 2, 1),
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

    private static InventoryType findInventoryType(String name) {
        InventoryType type;
        try {
            type = InventoryType.valueOf(name.toUpperCase());
        }
        catch (Exception e) {
            type = InventoryType.CHEST;
        }

        return type;
    }
}
