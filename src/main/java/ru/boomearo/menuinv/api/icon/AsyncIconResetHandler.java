package ru.boomearo.menuinv.api.icon;

import ru.boomearo.menuinv.api.InventoryPage;

@FunctionalInterface
public interface AsyncIconResetHandler {

    boolean onIconReset(InventoryPage page, boolean force);

}
