package ru.boomearo.menuinv.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import ru.boomearo.menuinv.api.frames.PagedIcons;
import ru.boomearo.menuinv.api.icon.BottomInventoryClickHandler;
import ru.boomearo.menuinv.api.session.InventorySession;

public interface InventoryPage<SESSION extends InventorySession> {

    String getName();

    Inventory getInventory();

    MenuType getMenuType();

    Player getPlayer();

    PagedIcons<SESSION> getListedIconsItems(String name);

    SESSION getSession();

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

    InventoryCloseHandler<SESSION> getInventoryCloseHandler();

    BottomInventoryClickHandler<SESSION> getBottomInventoryClickHandler();

}
