package ru.boomearo.menuinv.api;

import lombok.NonNull;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public interface Updatable<T, C> extends Delayable<C> {

    @Nullable
    T onUpdate(@NonNull C consume, @NonNull Player player) throws Exception;

}
