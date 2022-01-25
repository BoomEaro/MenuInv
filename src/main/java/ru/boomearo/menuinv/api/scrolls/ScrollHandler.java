package ru.boomearo.menuinv.api.scrolls;

import org.bukkit.inventory.ItemStack;

/**
 * Представляет обработчик прокрутки страницы
 */
public interface ScrollHandler {

    /**
     * Обрабатывается в ситуации, когда предмет должен быть отображен
     *
     * @param currentPage Текущая страница в рамки предметов
     * @param maxPage     Максимальная страница в рамки предметов
     */
    public ItemStack onVisible(int currentPage, int maxPage);

    /**
     * Обрабатывается в ситуации, когда предмет должен быть скрыт
     *
     * @param currentPage Текущая страница в рамки предметов
     * @param maxPage     Максимальная страница в рамки предметов
     */
    public ItemStack onHide(int currentPage, int maxPage);

}
