package ru.boomearo.menuinv.api;

import org.bukkit.entity.Player;
import ru.boomearo.menuinv.api.frames.inventory.PagedItems;

/**
 * Представляет страницу инвентаря, которая непосредственно используется для взаимодействия с обычным инвентарем баккита
 */
public interface InventoryPage {

    /**
     * @return Имя страницы
     */
    public String getName();

    /**
     * @return Тип инвентаря
     */
    public InvType getType();

    /**
     * @return Тайтл инвентаря
     */
    public String getTitle();

    /**
     * @return Игрок, открывший эту страницу
     */
    public Player getPlayer();

    /**
     * @return Рамку страниц
     */
    public PagedItems getListedIconsItems(String name);

    /**
     * @return Сессию инвентаря
     */
    public InventorySession getSession();

    /**
     * Обновляет внутренние элементы инвентаря.
     */
    public void update();

    /**
     * Обновляет внутренние элементы инвентаря.
     * @param force Игнорировать ли задержку обновления всех элементов
     */
    public void update(boolean force);

    /**
     * Закрывает этот инвентарь.
     */
    public void close();

    /**
     * Закрывает этот инвентарь.
     * @param force Закрыть ли инвентарь срочно (прямо во время текущего тика, а не на следующем)
     */
    public void close(boolean force);
}
