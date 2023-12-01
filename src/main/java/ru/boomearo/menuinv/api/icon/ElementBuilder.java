package ru.boomearo.menuinv.api.icon;

import ru.boomearo.menuinv.api.session.InventorySession;

public interface ElementBuilder<SESSION extends InventorySession> {

    IconHandlerFactory<SESSION> build();

}
