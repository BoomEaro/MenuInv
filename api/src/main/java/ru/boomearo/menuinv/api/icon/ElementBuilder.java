package ru.boomearo.menuinv.api.icon;

import lombok.NonNull;

public interface ElementBuilder {

    @NonNull
    IconHandlerFactory build();

}
