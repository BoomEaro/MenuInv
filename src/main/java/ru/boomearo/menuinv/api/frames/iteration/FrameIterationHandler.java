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
    int startPositionX(int maxX);

    /**
     * Начальная позиция в итерации для оси Z
     *
     * @param maxZ Максимальная позиция оси Z
     */
    int startPositionZ(int maxZ);

    /**
     * Условие срабатывания итерации для оси X
     *
     * @param x    Текущая позиция
     * @param maxX Максимальная позиция оси X
     */
    boolean hasNextX(int x, int maxX);

    /**
     * Условие срабатывания итерации для оси Z
     *
     * @param z    Текущая позиция
     * @param maxZ Максимальная позиция оси Z
     */
    boolean hasNextZ(int z, int maxZ);

    /**
     * Метод который должен изменять значение. Например, инкрементировать или декрементировать число.
     *
     * @param x Текущая позиция
     */
    int manipulateX(int x);

    /**
     * Метод который должен изменять значение. Например, инкрементировать или декрементировать число.
     *
     * @param z Текущая позиция
     */
    int manipulateZ(int z);

    /**
     * Должна ли сначала итерироваться ось X вместо оси z?
     *
     * @return Итерировать ли ось X вместо оси Z.
     */
    boolean isReverse();
}
