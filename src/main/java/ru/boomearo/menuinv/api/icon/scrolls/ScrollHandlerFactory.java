package ru.boomearo.menuinv.api.icon.scrolls;

/**
 * Фабрика создания обработчика прокрутки страницы
 */
public interface ScrollHandlerFactory {

    ScrollHandler create(ScrollType scrollType);

}
