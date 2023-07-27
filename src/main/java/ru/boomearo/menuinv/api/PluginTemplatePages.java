package ru.boomearo.menuinv.api;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Представляет шаблон всех страниц плагина
 */
public interface PluginTemplatePages {

    /**
     * @return Плагин, который зарегистрировал эти страницы
     */
    JavaPlugin getPlugin();

    /**
     * Создает новый шаблон страниц. По умолчанию тип инвентаря InvType.CHEST_9X6
     *
     * @param pluginPage   Тип страницы
     * @param titleHandler Обработчик тайтла страницы
     * @return Шаблон страницы
     */
    TemplatePage createTemplatePage(PluginPage pluginPage);

    /**
     * Создает новый шаблон страниц.
     *
     * @param pluginPage   Тип страницы
     * @param titleHandler Обработчик тайтла страницы
     * @param type         Тип инвентаря
     * @return Шаблон страницы
     */
    TemplatePage createTemplatePage(PluginPage pluginPage, InvType type);
}
