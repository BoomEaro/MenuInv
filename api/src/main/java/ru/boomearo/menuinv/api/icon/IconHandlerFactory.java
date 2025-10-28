package ru.boomearo.menuinv.api.icon;

import lombok.NonNull;

@FunctionalInterface
public interface IconHandlerFactory {

    @NonNull
    IconHandler create();

}
