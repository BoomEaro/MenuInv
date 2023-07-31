package ru.boomearo.menuinv.api;

import org.bukkit.entity.Player;

public interface Updatable<T, C> extends Delayable<C> {

    T onUpdate(C consume, Player player);

}
