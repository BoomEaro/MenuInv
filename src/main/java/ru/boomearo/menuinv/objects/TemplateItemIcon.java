package ru.boomearo.menuinv.objects;

import ru.boomearo.menuinv.api.AbstractButtonHandler;

public class TemplateItemIcon {

    private final int position;
    private final AbstractButtonHandler handler;

    public TemplateItemIcon(int position, AbstractButtonHandler handler) {
        this.position = position;
        this.handler = handler;
    }

    public int getPosition() {
        return this.position;
    }

    public AbstractButtonHandler getHandler() {
        return this.handler;
    }
}
