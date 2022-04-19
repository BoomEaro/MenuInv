package ru.boomearo.menuinv.api.frames.iteration;

/**
 * Представляет шаблон порядка итерации элементов в рамке
 */
public interface FrameIterationHandler {

    /**
     * Начальная позиция в итерации для оси X
     *
     * @param maxX Максимальная позиция оси X
     */
    public int startPositionX(int maxX);

    /**
     * Начальная позиция в итерации для оси Z
     *
     * @param maxZ Максимальная позиция оси Z
     */
    public int startPositionZ(int maxZ);

    /**
     * Условие срабатывания итерации для оси X
     *
     * @param x    Текущая позиция
     * @param maxX Максимальная позиция оси X
     */
    public boolean hasNextX(int x, int maxX);

    /**
     * Условие срабатывания итерации для оси Z
     *
     * @param z    Текущая позиция
     * @param maxZ Максимальная позиция оси Z
     */
    public boolean hasNextZ(int z, int maxZ);

    /**
     * Метод который должен изменять значение. Например, инкрементировать или декрементировать число.
     *
     * @param x Текущая позиция
     */
    public int manipulateX(int x);

    /**
     * Метод который должен изменять значение. Например, инкрементировать или декрементировать число.
     *
     * @param z Текущая позиция
     */
    public int manipulateZ(int z);

    /**
     * Должна ли сначала итерироваться ось X вместо оси z?
     *
     * @return Итерировать ли ось X вместо оси Z.
     */
    public boolean isReverse();
}
