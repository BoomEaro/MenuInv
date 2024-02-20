package ru.boomearo.menuinv.api;

import org.bukkit.plugin.Plugin;

public interface PluginTemplatePages {

    Plugin getPlugin();

    TemplatePage createTemplatePage(PluginPage pluginPage);
}
