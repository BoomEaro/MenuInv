package ru.boomearo.menuinv.api.icon;

import ru.boomearo.menuinv.api.InventoryPage;

import java.util.function.Predicate;

public interface ElementBuilderUpdatable<T> extends ElementBuilder {

    T setIconUpdateDelay(IconUpdateDelay iconUpdateDelay);

    T setIconUpdateCondition(Predicate<InventoryPage> predicate);

}
