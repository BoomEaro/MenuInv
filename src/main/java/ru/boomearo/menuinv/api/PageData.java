package ru.boomearo.menuinv.api;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * Реализация страницы плагина. Устаревшее, так как используется интерфейс PluginPage, через которого можно сделать более удобные енумы для передачи страниц
 */
@Deprecated
public class PageData implements PluginPage {

    private final JavaPlugin plugin;
    private final String page;

    public PageData(JavaPlugin plugin, String page) {
        this.plugin = Objects.requireNonNull(plugin);
        this.page = Objects.requireNonNull(page);
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
