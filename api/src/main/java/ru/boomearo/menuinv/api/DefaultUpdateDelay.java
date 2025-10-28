package ru.boomearo.menuinv.api;

import lombok.NonNull;

import javax.annotation.Nullable;
import java.time.Duration;

public class DefaultUpdateDelay<T> implements Delayable<T> {

    @Nullable
    @Override
    public Duration onUpdateTime(@NonNull T inventoryPage, boolean force) {
        // If force then update immediately
        if (force) {
            return Duration.ZERO;
        }

        return Duration.ofMillis(250);
    }
}
