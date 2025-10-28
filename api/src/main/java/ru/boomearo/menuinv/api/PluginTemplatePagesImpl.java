package ru.boomearo.menuinv.api;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class PluginTemplatePagesImpl implements PluginTemplatePages {

    private final Plugin menuPlugin;
    private final Plugin plugin;
    private final Map<String, TemplatePageImpl> pages = new HashMap<>();

    @Nullable
    public TemplatePageImpl getTemplatePage(String name) {
        return this.pages.get(name);
    }

    @NonNull
    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @NonNull
    @Override
    public TemplatePage createTemplatePage(@NonNull PluginPage pluginPage) {
        String name = pluginPage.getPage();

        TemplatePageImpl newPage = new TemplatePageImpl(this.menuPlugin, name, this);

        this.pages.put(name, newPage);

        return newPage;
    }

}
