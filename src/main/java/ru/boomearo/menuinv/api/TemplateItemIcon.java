package ru.boomearo.menuinv.api;

public class TemplateItemIcon extends SlotElement {

    private final ButtonHandlerFactory factory;

    public TemplateItemIcon(int slot, ButtonHandlerFactory factory) {
        super(slot);
        this.factory = factory;
    }

    public ButtonHandlerFactory getFactory() {
        return this.factory;
    }
}
