package ru.boomearo.menuinv.api;

public class TemplateListedIcons {

    private final String name;

    private final int x;
    private final int z;

    private final int width;
    private final int height;

    private final ListedIconsHandler handler;

    public TemplateListedIcons(String name, int x, int z, int width, int height, ListedIconsHandler handler) {
        this.name = name;
        this.x = x;
        this.z = z;
        this.width = width;
        this.height = height;
        this.handler = handler;
    }

    public String getName() {
        return this.name;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public ListedIconsHandler getHandler() {
        return this.handler;
    }
}
