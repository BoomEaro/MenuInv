package ru.boomearo.menuinv.api.icon;

import lombok.Getter;
import lombok.NonNull;
import ru.boomearo.menuinv.api.SlotElement;

@Getter
public class ItemIconTemplate extends SlotElement {

    protected final IconHandlerFactory factory;

    public ItemIconTemplate(int slot, @NonNull IconHandlerFactory factory) {
        super(slot);
        this.factory = factory;
    }

}
