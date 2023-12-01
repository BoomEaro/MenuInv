package ru.boomearo.menuinv.api;

import org.bukkit.plugin.Plugin;
import ru.boomearo.menuinv.api.session.InventorySession;

public interface PluginTemplatePages {

    Plugin getPlugin();

    <SESSION extends InventorySession> TemplatePage<SESSION> createTemplatePage(PluginPage<SESSION> pluginPage);
}
