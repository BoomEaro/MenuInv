package ru.boomearo.menuinv.api;

import lombok.NonNull;
import org.bukkit.plugin.Plugin;

public interface PluginTemplatePages {

    @NonNull
    Plugin getPlugin();

    @NonNull
    TemplatePage createTemplatePage(@NonNull PluginPage pluginPage);
}
