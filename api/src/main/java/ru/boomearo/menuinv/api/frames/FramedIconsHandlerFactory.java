package ru.boomearo.menuinv.api.frames;

import lombok.NonNull;

@FunctionalInterface
public interface FramedIconsHandlerFactory {

    @NonNull
    FramedIconsHandler create();

}
