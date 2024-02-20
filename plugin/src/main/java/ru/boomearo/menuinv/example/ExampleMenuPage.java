package ru.boomearo.menuinv.example;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.boomearo.menuinv.MenuInv;
import ru.boomearo.menuinv.api.PluginPage;

@RequiredArgsConstructor
@Getter
public enum ExampleMenuPage implements PluginPage {

    MAIN(MenuInv.getInstance(), "main"),
    OTHER(MenuInv.getInstance(), "other");

    private final MenuInv plugin;
    private final String page;

}
