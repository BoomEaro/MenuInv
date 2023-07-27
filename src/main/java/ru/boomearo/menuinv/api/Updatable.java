package ru.boomearo.menuinv.api;

import org.bukkit.entity.Player;

/**
 * Представляет обновляемый элемент
 */
public interface Updatable<T, C> {

    /**
     * @return Новый объект для которого применяется обновление
     */
    public T onUpdate(C consume, Player player);

    /**
     * @return Время для обновления метода update. По умолчанию значение равно 1000 миллисекунд или одной секундой.
     */
    public default long getUpdateTime(C consume) {
        return 1000;
    }

    /**
     * @return Действительно ли элемент будет обновлен
     */
    public default boolean shouldUpdate(C consume) {
        return true;
    }
}
