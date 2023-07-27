package ru.boomearo.menuinv.api;

import com.google.common.base.Preconditions;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Представляет реализацию шаблона страниц плагина
 */
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
    public TemplatePage createTemplatePage(PluginPage pluginPage) {
        return createTemplatePage(pluginPage, InvType.CHEST_9X6);
    }

    @Override
    public TemplatePage createTemplatePage(PluginPage pluginPage, InvType type) {
        Preconditions.checkArgument(pluginPage != null, "pluginPage is null!");
        Preconditions.checkArgument(type != null, "type is null!");

        String name = pluginPage.getPage();

        TemplatePage tmp = this.pages.get(name);
        if (tmp != null) {
            throw new IllegalStateException("Page with name '" + name + "' alreadt created!");
        }

        TemplatePageImpl newPage = new TemplatePageImpl(name, this);

        this.pages.put(name, newPage);

        return newPage;
    }

}
