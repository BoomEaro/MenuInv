package ru.boomearo.menuinv.api;

@FunctionalInterface
public interface AsyncResetHandler {

    boolean onIconReset(InventoryPage page, boolean force);

}
