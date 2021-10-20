package ru.boomearo.menuinv.api.frames;

import ru.boomearo.menuinv.api.ListedIconsHandler;

public class TemplateListedIcons extends FramedIcons {

    private final ListedIconsHandler handler;

    public TemplateListedIcons(String name, int x, int z, int width, int height, ListedIconsHandler handler) {
        super(name, x, z, width, height);
        this.handler = handler;
    }

    public ListedIconsHandler getHandler() {
        return this.handler;
    }
}
