package ru.boomearo.menuinv.api;

public class ItemIconTemplate extends SlotElement {

    private final IconHandlerFactory factory;

    public ItemIconTemplate(int slot, IconHandlerFactory factory) {
        super(slot);
        this.factory = factory;
    }

    public IconHandlerFactory getFactory() {
        return this.factory;
    }
}
