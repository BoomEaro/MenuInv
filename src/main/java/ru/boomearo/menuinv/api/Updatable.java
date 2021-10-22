package ru.boomearo.menuinv.api;

import org.bukkit.entity.Player;

/**
 * Представляет обновляемый элемент
 */
public interface Updatable<T, C> {

    /**
     * @return новый объект для которого применяется обновление
     */
    public T onUpdate(C consume, Player player);

    /**
     * @return время для обновления метода update. По умолчанию значение равно 20 тиков, то есть одна секунда. (1 тик = 50 мс)
     */
    public default long getUpdateTime() {
        return 20;
    }

    /**
     * @return действительно ли элемент будет обновлен
     */
    public default boolean shouldUpdate() {
        return true;
    }
}
