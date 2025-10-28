package ru.boomearo.menuinv.api.frames;

import lombok.Getter;
import lombok.NonNull;
import ru.boomearo.menuinv.api.InventoryLocation;

@Getter
public abstract class Frame {
    protected final String name;

    protected final InventoryLocation first;
    protected final InventoryLocation second;

    protected final int width;
    protected final int height;

    public Frame(@NonNull String name, @NonNull InventoryLocation first, @NonNull InventoryLocation second) {
        this.name = name;

        int xMin = (Math.min(first.x(), second.x()));
        int zMin = (Math.min(first.z(), second.z()));
        this.first = InventoryLocation.of(xMin, zMin);

        int xMax = (Math.max(first.x(), second.x()));
        int zMax = (Math.max(first.z(), second.z()));
        this.second = InventoryLocation.of(xMax, zMax);

        this.width = Math.abs(second.x() - first.x());
        this.height = Math.abs(second.z() - first.z());
    }

    public Frame(@NonNull String name, @NonNull InventoryLocation loc, int width, int height) {
        this(
                name,
                InventoryLocation.of(loc.x(), loc.z()),
                InventoryLocation.of(loc.x() + width, loc.z() + height)
        );
    }

    public boolean isInsideFrame(int slot) {
        int y = slot / this.width;
        int x = slot - (y * this.width);
        return isInsideFrame(x, y);
    }

    public boolean isInsideFrame(int x, int z) {
        return x >= this.first.x() && z >= this.first.z() && x <= this.second.x() && z <= this.second.z();
    }
}
