package ru.boomearo.menuinv.api;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class PageData {

    private final JavaPlugin plugin;
    private final String page;

    public PageData(JavaPlugin plugin, String page) {
        this.plugin = Objects.requireNonNull(plugin);
        this.page = Objects.requireNonNull(page);
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public String getPage() {
        return this.page;
    }
}
