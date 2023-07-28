package ru.boomearo.menuinv.api;

import org.bukkit.plugin.java.JavaPlugin;

public interface PluginTemplatePages {

    JavaPlugin getPlugin();

    TemplatePage createTemplatePage(PluginPage pluginPage);
}
