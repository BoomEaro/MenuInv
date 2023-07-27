package ru.boomearo.menuinv.api;

import org.bukkit.plugin.java.JavaPlugin;
import ru.boomearo.menuinv.MenuInv;

public enum DefaultMenuPage implements PluginPage {

    CONFIRM(MenuInv.getInstance(), "confirm");

    private final JavaPlugin plugin;
    private final String page;

    DefaultMenuPage(JavaPlugin plugin, String page) {
        this.plugin = plugin;
        this.page = page;
    }

    @Override
    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    @Override
    public String getPage() {
        return this.page;
    }

}
