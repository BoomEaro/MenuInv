package ru.boomearo.menuinv.objects;

import org.bukkit.plugin.java.JavaPlugin;

import ru.boomearo.menuinv.api.IPluginTemplatePages;
import ru.boomearo.menuinv.api.ITemplatePage;
import ru.boomearo.menuinv.api.InvType;
import ru.boomearo.menuinv.exceptions.MenuInvException;

import java.util.HashMap;
import java.util.Map;

public class PluginTemplatePages implements IPluginTemplatePages {

    private final JavaPlugin plugin;
    private final Map<String, TemplatePage> pages = new HashMap<>();

    public PluginTemplatePages(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public TemplatePage getTemplatePage(String name) {
        return this.pages.get(name);
    }

    @Override
    public ITemplatePage createTemplatePage(String name, String title) throws MenuInvException {
        return createTemplatePage(name, title, InvType.Chest);
    }

    @Override
    public ITemplatePage createTemplatePage(String name, String title, int height) throws MenuInvException {
        return createTemplatePage(name, title, InvType.Chest, height);
    }

    @Override
    public ITemplatePage createTemplatePage(String name, String title, InvType type) throws MenuInvException {
        return createTemplatePage(name, title, type, type.getMaxHeight());
    }

    @Override
    public ITemplatePage createTemplatePage(String name, String title, InvType type, int height) throws MenuInvException {
        ITemplatePage tmp = this.pages.get(name);
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

        TemplatePage newPage = new TemplatePage(name, title, type, newHeight);

        this.pages.put(name, newPage);

        return newPage;
    }
}
