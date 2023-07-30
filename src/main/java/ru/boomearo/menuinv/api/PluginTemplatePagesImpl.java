package ru.boomearo.menuinv.api;

import com.google.common.base.Preconditions;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class PluginTemplatePagesImpl implements PluginTemplatePages {

    private final Plugin plugin;
    private final Map<String, TemplatePageImpl> pages = new HashMap<>();

    public PluginTemplatePagesImpl(Plugin plugin) {
        this.plugin = plugin;
    }

    public TemplatePageImpl getTemplatePage(String name) {
        return this.pages.get(name);
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public TemplatePage createTemplatePage(PluginPage pluginPage) {
        Preconditions.checkArgument(pluginPage != null, "pluginPage is null!");

        String name = pluginPage.getPage();

        TemplatePageImpl newPage = new TemplatePageImpl(name, this);

        this.pages.put(name, newPage);

        return newPage;
    }

}
