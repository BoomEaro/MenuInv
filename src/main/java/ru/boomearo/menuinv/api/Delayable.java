package ru.boomearo.menuinv.api;

@FunctionalInterface
public interface Delayable<T>{

    long onUpdateTime(T data, boolean force);

    default boolean canUpdate(T data, boolean force, long time) {
        long result = onUpdateTime(data, force);

        if (result == Long.MAX_VALUE) {
            return false;
        }

        if (result <= 0) {
            return true;
        }

        return (System.currentTimeMillis() - time) > result;
    }

}
