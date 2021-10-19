package ru.boomearo.menuinv.api;

import ru.boomearo.menuinv.exceptions.MenuInvException;

public interface IPluginTemplatePages {

    public ITemplatePage createTemplatePage(String name, String title) throws MenuInvException;

    public ITemplatePage createTemplatePage(String name, String title, int height) throws MenuInvException;

    public ITemplatePage createTemplatePage(String name, String title, InvType type) throws MenuInvException;

    public ITemplatePage createTemplatePage(String name, String title, InvType type, int height) throws MenuInvException;

}
