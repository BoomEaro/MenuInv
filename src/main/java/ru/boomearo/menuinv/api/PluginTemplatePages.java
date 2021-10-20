package ru.boomearo.menuinv.api;

import ru.boomearo.menuinv.exceptions.MenuInvException;

public interface PluginTemplatePages {

    public TemplatePage createTemplatePage(String name, String title) throws MenuInvException;

    public TemplatePage createTemplatePage(String name, String title, int height) throws MenuInvException;

    public TemplatePage createTemplatePage(String name, String title, InvType type) throws MenuInvException;

    public TemplatePage createTemplatePage(String name, String title, InvType type, int height) throws MenuInvException;

}
