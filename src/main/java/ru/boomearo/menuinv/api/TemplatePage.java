package ru.boomearo.menuinv.api;

import ru.boomearo.menuinv.api.frames.inventory.PagedItems;
import ru.boomearo.menuinv.exceptions.MenuInvException;

public interface TemplatePage {

    public void addButton(int slot, IconHandlerFactory factory) throws MenuInvException;

    public void addPagedItems(String name, int x, int z, int width, int height, FramedIconsHandlerFactory factory) throws MenuInvException;

    public void addScrollButton(int slot, String pagedItems, PagedItems.ScrollType type, ScrollHandlerFactory factory) throws MenuInvException;
}
