package ru.boomearo.menuinv.api;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface Updatable<T, C> {

    public T onUpdate(C consume, Player player);

    public default long getUpdateTime(C consume) {
        return 1000;
    }

    public default boolean shouldUpdate(C consume) {
        return true;
    }
}
