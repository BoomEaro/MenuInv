package ru.boomearo.menuinv.api;

public class TemplateItemIcon extends SlotElement {

    private final IconHandlerFactory factory;

    public TemplateItemIcon(int slot, IconHandlerFactory factory) {
        super(slot);
        this.factory = factory;
    }

    public IconHandlerFactory getFactory() {
        return this.factory;
    }
}
