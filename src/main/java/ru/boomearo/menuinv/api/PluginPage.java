package ru.boomearo.menuinv.api;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Интерфейс, представляющий страницу меню плагина.
 * Используется для идентификации и открытия нужных страниц для нужного плагина.
 */
public interface PluginPage {

    JavaPlugin getPlugin();

    String getPage();
}
