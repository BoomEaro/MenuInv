package ru.boomearo.menuinv.api;

public class TemplateItemIcon {

    private final int slot;
    private final AbstractButtonHandler handler;

    public TemplateItemIcon(int slot, AbstractButtonHandler handler) {
        this.slot = slot;
        this.handler = handler;
    }

    public int getSlot() {
        return this.slot;
    }

    public AbstractButtonHandler getHandler() {
        return this.handler;
    }
}
