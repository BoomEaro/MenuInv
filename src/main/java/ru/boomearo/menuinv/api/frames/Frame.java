package ru.boomearo.menuinv.api.frames;

/**
 * Представляет абстрактную рамку
 */
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

    /**
     * @return Имя рамки
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return Первые координаты x
     */
    public int getFirstX() {
        return this.firstX;
    }

    /**
     * @return Первые координаты z
     */
    public int getFirstZ() {
        return this.firstZ;
    }

    /**
     * @return Вторые координаты x
     */
    public int getSecondX() {
        return this.secondX;
    }

    /**
     * @return Вторые координаты z
     */
    public int getSecondZ() {
        return this.secondZ;
    }

    /**
     * @return Ширина рамки
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * @return Высота рамки
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Проверяет, находится ли позиция в рамке
     * @param slot Проверяемая позиция
     * @return true если указанная позиция внутри рамки
     */
    public boolean isInsideFrame(int slot) {
        int y = slot / this.width;
        int x = slot - (y * this.width);
        return isInsideFrame(x, y);
    }

    /**
     * Проверяет, находится ли координаты в рамке
     * @param x Первая точка позиции
     * @param z Вторая точка позиции
     * @return true если указанные координаты внутри рамки
     */
    public boolean isInsideFrame(int x, int z) {
        return x >= this.firstX && z >= this.firstZ && x <= this.secondX && z <= this.secondZ;
    }
}
