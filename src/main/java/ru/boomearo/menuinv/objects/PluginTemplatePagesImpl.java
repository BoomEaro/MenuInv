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
        return createTemplatePage(name, title, InvType.Chest);
    }

    @Override
    public TemplatePage createTemplatePage(String name, String title, int height) throws MenuInvException {
        return createTemplatePage(name, title, InvType.Chest, height);
    }

    @Override
    public TemplatePage createTemplatePage(String name, String title, InvType type) throws MenuInvException {
        return createTemplatePage(name, title, type, type.getMaxHeight());
    }

    @Override
    public TemplatePage createTemplatePage(String name, String title, InvType type, int height) throws MenuInvException {
        TemplatePage tmp = this.pages.get(name);
        if (tmp != null) {
            throw new MenuInvException("Страница '" + name + "' уже создана!");
        }

        int newHeight = height;
        if (type != InvType.Chest) {
            newHeight = type.getMaxHeight();
        }

        if (newHeight <= 0) {
            newHeight = 1;
        }

        if (newHeight > type.getMaxHeight()) {
            newHeight = type.getMaxHeight();
        }

        TemplatePageImpl newPage = new TemplatePageImpl(name, title, type, newHeight, this);

        this.pages.put(name, newPage);

        return newPage;
    }
}
