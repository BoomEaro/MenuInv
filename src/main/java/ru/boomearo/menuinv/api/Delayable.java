package ru.boomearo.menuinv.api;

import java.time.Duration;

@FunctionalInterface
public interface Delayable<T>{

    Duration onUpdateTime(T data, boolean force);

    default boolean canUpdate(T data, boolean force, long time) {
        Duration duration = onUpdateTime(data, force);
        if (duration == null) {
            duration = Duration.ZERO;
        }

        long miliseconds = duration.toMillis();

        if (miliseconds == Long.MAX_VALUE) {
            return false;
        }

        if (miliseconds <= 0) {
            return true;
        }

        return (System.currentTimeMillis() - time) > miliseconds;
    }

}
