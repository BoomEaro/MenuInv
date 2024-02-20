package ru.boomearo.menuinv;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import ru.boomearo.menuinv.api.*;
import ru.boomearo.menuinv.example.ExampleMenuManager;

public final class MenuInv extends JavaPlugin {

    @Getter
    private static MenuInv instance = null;

    @Override
    public void onEnable() {
        instance = this;

        Menu.initMenu(this);

        ExampleMenuManager.setup(this);

        this.getLogger().info("Plugin successfully enabled.");
    }

    @Override
    public void onDisable() {
        Menu.unloadMenu(this);

        this.getLogger().info("Plugin successfully disabled.");
    }
}
