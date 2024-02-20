package ru.boomearo.menuinv.api;

import java.time.Duration;

public class DefaultUpdateDelay<T> implements Delayable<T> {

    @Override
    public Duration onUpdateTime(T inventoryPage, boolean force) {
        // If force then update immediately
        if (force) {
            return Duration.ZERO;
        }

        return Duration.ofMillis(250);
    }
}
