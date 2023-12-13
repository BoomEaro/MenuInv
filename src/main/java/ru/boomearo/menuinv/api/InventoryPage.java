package ru.boomearo.menuinv.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import ru.boomearo.menuinv.api.frames.PagedIcons;
import ru.boomearo.menuinv.api.icon.BottomInventoryClickHandler;
import ru.boomearo.menuinv.api.session.InventorySession;

public interface InventoryPage {

    String getName();

    Inventory getInventory();

    MenuType getMenuType();

    Player getPlayer();

    PagedIcons getListedIconsItems(String name);

    InventorySession getSession();

    void setNeedUpdate();

    default void update() {
        update(false);
    }

    void update(boolean force);

    default void reopen() {
        reopen(false);
    }

    void reopen(boolean force);

    default void close() {
        close(false);
    }

    void close(boolean force);

    boolean isClosed();

    InventoryCloseHandler getInventoryCloseHandler();

    BottomInventoryClickHandler getBottomInventoryClickHandler();

}
