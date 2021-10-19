package ru.boomearo.menuinv.api;

import ru.boomearo.menuinv.exceptions.MenuInvException;

public interface ITemplatePage {

    public void addButton(int position, AbstractButtonHandler handler) throws MenuInvException;

    public void addListedButton(String name, int x, int z, int width, int height, IListedButtonHandler listedButton) throws MenuInvException;

}
