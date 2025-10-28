package ru.boomearo.menuinv.api;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@RequiredArgsConstructor
public class InfinityUpdateDelay<T> implements Delayable<T> {

    private final boolean ignoreForce;

    public InfinityUpdateDelay() {
        this.ignoreForce = false;
    }

    @NonNull
    @Override
    public Duration onUpdateTime(@NonNull T data, boolean force) {
        if (this.ignoreForce) {
            return Duration.ofMillis(Long.MAX_VALUE);
        }

        if (force) {
            return Duration.ZERO;
        }
        return Duration.ofMillis(Long.MAX_VALUE);
    }
}
