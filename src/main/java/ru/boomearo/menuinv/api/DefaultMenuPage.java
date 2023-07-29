package ru.boomearo.menuinv.api;

import org.bukkit.plugin.Plugin;
import ru.boomearo.menuinv.MenuInv;

public enum DefaultMenuPage implements PluginPage {

    CONFIRM(MenuInv.getInstance(), "confirm");

    private final Plugin plugin;
    private final String page;

    DefaultMenuPage(Plugin plugin, String page) {
        this.plugin = plugin;
        this.page = page;
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public String getPage() {
        return this.page;
    }

}
