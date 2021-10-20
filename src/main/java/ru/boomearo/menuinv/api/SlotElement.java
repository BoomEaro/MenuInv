package ru.boomearo.menuinv.api;

public abstract class SlotElement {

    private final int slot;

    public SlotElement(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return this.slot;
    }
}
