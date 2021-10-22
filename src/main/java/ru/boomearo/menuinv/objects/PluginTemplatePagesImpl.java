package ru.boomearo.menuinv.objects;

import org.bukkit.plugin.java.JavaPlugin;

import ru.boomearo.menuinv.api.PluginTemplatePages;
import ru.boomearo.menuinv.api.TemplatePage;
import ru.boomearo.menuinv.api.InvType;
import ru.boomearo.menuinv.exceptions.MenuInvException;

import java.util.HashMap;
import java.util.Map;

public class PluginTemplatePagesImpl implements PluginTemplatePages {

    private final JavaPlugin plugin;
    private final Map<String, TemplatePageImpl> pages = new HashMap<>();

    public PluginTemplatePagesImpl(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public TemplatePageImpl getTemplatePage(String name) {
        return this.pages.get(name);
    }

    @Override
    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    @Override
    public TemplatePage createTemplatePage(String name, String title) throws MenuInvException {
        return createTemplatePage(name, title, InvType.CHEST_9X6);
    }

    @Override
    public TemplatePage createTemplatePage(String name, String title, InvType type) throws MenuInvException {
        if (name == null || title == null || type == null) {
            throw new MenuInvException("Указанные аргументы являются нулевыми!");
        }

        TemplatePage tmp = this.pages.get(name);
        if (tmp != null) {
            throw new MenuInvException("Страница '" + name + "' уже создана!");
        }

        TemplatePageImpl newPage = new TemplatePageImpl(name, title, type,this);

        this.pages.put(name, newPage);

        return newPage;
    }
}
