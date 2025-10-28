package ru.boomearo.menuinv.api;

import lombok.NonNull;
import net.kyori.adventure.text.Component;

import javax.annotation.Nullable;

@FunctionalInterface
public interface ComponentInventoryTitleHandler {

    @Nullable
    Component createTitle(@NonNull InventoryPage page);

}
