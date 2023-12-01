package ru.boomearo.menuinv.api.icon;

import lombok.Getter;
import ru.boomearo.menuinv.api.SlotElement;
import ru.boomearo.menuinv.api.session.InventorySession;

@Getter
public class ItemIconTemplate<SESSION extends InventorySession> extends SlotElement {

    protected final IconHandlerFactory<SESSION> factory;

    public ItemIconTemplate(int slot, IconHandlerFactory<SESSION> factory) {
        super(slot);
        this.factory = factory;
    }

}
