package ru.boomearo.menuinv.api;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface Updatable<T, C> {

    T onUpdate(C consume, Player player);

    default long getUpdateTime(C consume) {
        return 1000;
    }

    default boolean shouldUpdate(C consume) {
        return true;
    }
}
