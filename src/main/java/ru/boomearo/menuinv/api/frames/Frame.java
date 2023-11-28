package ru.boomearo.menuinv.api.frames;

import lombok.Getter;
import ru.boomearo.menuinv.api.InventoryLocation;

@Getter
public abstract class Frame {
    protected final String name;

    protected final InventoryLocation first;
    protected final InventoryLocation second;

    protected final int width;
    protected final int height;

    public Frame(String name, InventoryLocation first, InventoryLocation second) {
        this.name = name;

        int xMin = (Math.min(first.getX(), second.getX()));
        int zMin = (Math.min(first.getZ(), second.getZ()));
        this.first = InventoryLocation.of(xMin, zMin);

        int xMax = (Math.max(first.getX(), second.getX()));
        int zMax = (Math.max(first.getZ(), second.getZ()));
        this.second = InventoryLocation.of(xMax, zMax);

        this.width = Math.abs(second.getX() - first.getX());
        this.height = Math.abs(second.getZ() - first.getZ());
    }

    public Frame(String name, InventoryLocation loc, int width, int height) {
        this(
                name,
                InventoryLocation.of(loc.getX(), loc.getZ()),
                InventoryLocation.of(loc.getX() + width, loc.getZ() + height)
        );
    }

    public boolean isInsideFrame(int slot) {
        int y = slot / this.width;
        int x = slot - (y * this.width);
        return isInsideFrame(x, y);
    }

    public boolean isInsideFrame(int x, int z) {
        return x >= this.first.getX() && z >= this.first.getZ() && x <= this.second.getX() && z <= this.second.getZ();
    }
}
