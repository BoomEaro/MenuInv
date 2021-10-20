package ru.boomearo.menuinv.api;

import ru.boomearo.menuinv.exceptions.MenuInvException;

public interface TemplatePage {

    public void addButton(int slot, AbstractButtonHandler handler) throws MenuInvException;

    public void addListedButton(String name, int x, int z, int width, int height, ListedIconsHandler handler) throws MenuInvException;

}
