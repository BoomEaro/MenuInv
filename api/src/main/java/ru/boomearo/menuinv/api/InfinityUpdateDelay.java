package ru.boomearo.menuinv.api;

import lombok.RequiredArgsConstructor;

import java.time.Duration;

@RequiredArgsConstructor
public class InfinityUpdateDelay<T> implements Delayable<T> {

    private final boolean ignoreForce;

    public InfinityUpdateDelay() {
        this.ignoreForce = false;
    }

    @Override
    public Duration onUpdateTime(T data, boolean force) {
        if (this.ignoreForce) {
            return Duration.ofMillis(Long.MAX_VALUE);
        }

        if (force) {
            return Duration.ZERO;
        }
        return Duration.ofMillis(Long.MAX_VALUE);
    }
}
