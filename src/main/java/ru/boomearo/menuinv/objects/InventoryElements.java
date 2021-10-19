package ru.boomearo.menuinv.objects;

import java.util.Collection;
import java.util.Map;

public class InventoryElements {

    private final Map<Integer, ItemIcon> iconsPosition;

    public InventoryElements(Map<Integer, ItemIcon> iconsPosition) {
        this.iconsPosition = iconsPosition;
    }

    public ItemIcon getItemIcon(int slot) {
        return this.iconsPosition.get(slot);
    }

    public Collection<ItemIcon> getAllItemIcon() {
        return this.iconsPosition.values();
    }

}
