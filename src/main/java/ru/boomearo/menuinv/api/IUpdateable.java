package ru.boomearo.menuinv.api;

import org.bukkit.entity.Player;

/**
 * Представляет обновляемый элемент
 */
public interface IUpdateable<T, C> {

    /**
     * @return новый объект для которого применяется обновление
     */
    public T update(C consume, Player player);

    /**
     * @return время для обновления метода update. По умолчанию значение равно 20 тиков, то есть одна секунда. (1 тик = 50 мс)
     */
    public default long getUpdateTime() {
        return 20;
    }
}
