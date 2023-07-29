package ru.boomearo.menuinv.api.icon.scrolls;

@FunctionalInterface
public interface ScrollHandlerFactory {

    ScrollHandler create(ScrollType scrollType);

}
