package ru.boomearo.menuinv.api;

import org.bukkit.entity.Player;
import ru.boomearo.menuinv.api.frames.inventory.PagedItems;
import ru.boomearo.menuinv.api.session.InventorySession;

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
     * Устанавливает флаг, обозначающий, что этот инвентарь изменен и его надо обновить.
     * Обновление произойдет обычным таймером на следующий тик.
     */
    public void setNeedUpdate();

    /**
     * Обновляет внутренние элементы инвентаря.
     * Не рекомендуется использовать во время нажатия на кнопки.
     */
    public default void update() {
        update(false);
    }

    /**
     * Обновляет внутренние элементы инвентаря.
     * Не рекомендуется использовать во время нажатия на кнопки.
     *
     * @param force Игнорировать ли задержку обновления всех элементов
     */
    public void update(boolean force);

    /**
     * Открывает новый экземпляр баккитовского Inventory.
     * На данный момент используется для обновления тайтла.
     */
    public default void reopen() {
        reopen(false);
    }

    /**
     * Открывает новый экземпляр баккитовского Inventory.
     * На данный момент используется для обновления тайтла.
     *
     * @param force Открыть ли новый инвентарь срочно. (прямо во время текущего тика, а не на следующем)
     */
    public void reopen(boolean force);

    /**
     * Закрывает этот инвентарь.
     */
    public default void close() {
        close(false);
    }

    /**
     * Закрывает этот инвентарь.
     *
     * @param force Закрыть ли инвентарь срочно. (прямо во время текущего тика, а не на следующем)
     */
    public void close(boolean force);

}
