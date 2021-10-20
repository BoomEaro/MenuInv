package ru.boomearo.menuinv.api;

import ru.boomearo.menuinv.exceptions.MenuInvException;
import ru.boomearo.menuinv.api.frames.ListedIconItems;

public interface TemplatePage {

    public void addButton(int slot, ButtonHandlerFactory factory) throws MenuInvException;

    public void addListedButton(String name, int x, int z, int width, int height, ListedIconsHandler handler) throws MenuInvException;

    public void addScrollButton(int slot, String listedButton, ListedIconItems.ScrollType type, ScrollHandler handler) throws MenuInvException;
}
