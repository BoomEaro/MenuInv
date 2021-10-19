package ru.boomearo.menuinv.objects;

import ru.boomearo.menuinv.api.AbstractButtonHandler;

public class ItemIcon extends TemplateItemIcon {

    private long updateHandlerCooldown = System.currentTimeMillis();

    public ItemIcon(int position, AbstractButtonHandler handler) {
        super(position, handler);
    }

    public long getUpdateHandlerCooldown() {
        return this.updateHandlerCooldown;
    }

    public void resetUpdateHandlerCooldown() {
        this.updateHandlerCooldown = System.currentTimeMillis();
    }

}
