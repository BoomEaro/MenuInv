package ru.boomearo.menuinv.api;

import lombok.NonNull;

import javax.annotation.Nullable;
import java.time.Duration;

@FunctionalInterface
public interface Delayable<T> {

    @Nullable
    Duration onUpdateTime(@NonNull T data, boolean force);

    default boolean canUpdate(@NonNull T data, boolean force, long time) {
        Duration duration = onUpdateTime(data, force);
        if (duration == null) {
            duration = Duration.ZERO;
        }

        long milliseconds = duration.toMillis();

        if (milliseconds == Long.MAX_VALUE) {
            return false;
        }

        if (milliseconds <= 0) {
            return true;
        }

        return (System.currentTimeMillis() - time) > milliseconds;
    }

}
