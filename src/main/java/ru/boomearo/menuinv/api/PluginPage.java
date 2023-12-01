package ru.boomearo.menuinv.api;

import org.bukkit.plugin.Plugin;
import ru.boomearo.menuinv.api.session.InventorySession;

public interface PluginPage<SESSION extends InventorySession> {

    Plugin getPlugin();

    String getPage();

}
