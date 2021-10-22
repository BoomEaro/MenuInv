package ru.boomearo.menuinv.api.scrolls;

/**
 * Фабрика создания обработчика прокрутки страницы
 */
public interface ScrollHandlerFactory {

    public ScrollHandler create();

}
