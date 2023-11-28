package ru.boomearo.menuinv.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;
import ru.boomearo.menuinv.MenuInv;

@RequiredArgsConstructor
@Getter
public enum DefaultMenuPage implements PluginPage {

    CONFIRM(MenuInv.getInstance(), "confirm");

    private final Plugin plugin;
    private final String page;

}
