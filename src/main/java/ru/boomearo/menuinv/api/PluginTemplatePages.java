package ru.boomearo.menuinv.api;

import org.bukkit.plugin.java.JavaPlugin;
import ru.boomearo.menuinv.exceptions.MenuInvException;

public interface PluginTemplatePages {

    public JavaPlugin getPlugin();

    public TemplatePage createTemplatePage(String name, String title) throws MenuInvException;

    public TemplatePage createTemplatePage(String name, String title, InvType type) throws MenuInvException;

}
