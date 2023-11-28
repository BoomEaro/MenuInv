package ru.boomearo.menuinv.api.icon;

import lombok.Getter;
import ru.boomearo.menuinv.api.SlotElement;

@Getter
public class ItemIconTemplate extends SlotElement {

    protected final IconHandlerFactory factory;

    public ItemIconTemplate(int slot, IconHandlerFactory factory) {
        super(slot);
        this.factory = factory;
    }

}
