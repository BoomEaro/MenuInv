package ru.boomearo.menuinv.api;

import org.bukkit.plugin.java.JavaPlugin;
import ru.boomearo.menuinv.exceptions.MenuInvException;

/**
 * Представляет шаблон всех страниц плагина
 */
public interface PluginTemplatePages {

    /**
     * @return Плагин, который зарегистрировал эти страницы
     */
    public JavaPlugin getPlugin();

    /**
     * Создает новый шаблон страниц. По умолчанию тип инвентаря InvType.CHEST_9X6
     * @param name Название страницы
     * @param title Тайтл страницы, который отображается в названии инвентаря
     * @return Шаблон страницы
     */
    public TemplatePage createTemplatePage(String name, String title) throws MenuInvException;

    /**
     * Создает новый шаблон страниц.
     * @param name Название страницы
     * @param title Тайтл страницы, который отображается в названии инвентаря
     * @param type Тип инвентаря
     * @return Шаблон страницы
     */
    public TemplatePage createTemplatePage(String name, String title, InvType type) throws MenuInvException;

}
