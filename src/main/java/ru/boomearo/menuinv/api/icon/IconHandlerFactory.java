package ru.boomearo.menuinv.api.icon;

import ru.boomearo.menuinv.api.session.InventorySession;

@FunctionalInterface
public interface IconHandlerFactory<SESSION extends InventorySession> {

    IconHandler<SESSION> create();

}
