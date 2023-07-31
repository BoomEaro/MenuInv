package ru.boomearo.menuinv.api;

import org.bukkit.entity.Player;
import ru.boomearo.menuinv.api.frames.PagedIcons;
import ru.boomearo.menuinv.api.session.InventorySession;

public interface InventoryPage {

    String getName();

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

    InventoryCloseHandler getInventoryCloseHandler();

}
