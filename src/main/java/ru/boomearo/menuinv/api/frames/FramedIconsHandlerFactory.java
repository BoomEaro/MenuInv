package ru.boomearo.menuinv.api.frames;

import ru.boomearo.menuinv.api.session.InventorySession;

@FunctionalInterface
public interface FramedIconsHandlerFactory<SESSION extends InventorySession> {

    FramedIconsHandler<SESSION> create();

}
