package ru.boomearo.menuinv.api;

import lombok.NonNull;
import org.bukkit.plugin.Plugin;

public interface PluginPage {

    @NonNull
    Plugin getPlugin();

    @NonNull
    String getPage();
}
