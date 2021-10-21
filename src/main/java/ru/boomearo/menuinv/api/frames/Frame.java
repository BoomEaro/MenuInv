package ru.boomearo.menuinv.api.frames;

public abstract class Frame {

    private final String name;

    private final int firstX;
    private final int firstZ;
    private final int secondX;
    private final int secondZ;

    private final int width;
    private final int height;

    public Frame(String name, int x, int z, int width, int height) {
        this.name = name;
        this.firstX = x;
        this.secondX = x + width - 1;
        this.firstZ = z;
        this.secondZ = z + height - 1;

        this.width = width;
        this.height = height;
    }

    public String getName() {
        return this.name;
    }

    public int getFirstX() {
        return this.firstX;
    }

    public int getFirstZ() {
        return this.firstZ;
    }

    public int getSecondX() {
        return this.secondX;
    }

    public int getSecondZ() {
        return this.secondZ;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean isInsideFrame(int slot) {
        int y = slot / this.width;
        int x = slot - (y * this.width);
        return isInsideFrame(x, y);
    }

    public boolean isInsideFrame(int x, int y) {
        return x >= this.firstX && y >= this.firstZ && x <= this.secondX && y <= this.secondZ;
    }
}
