package ru.boomearo.menuinv.api;

import org.bukkit.entity.Player;
import ru.boomearo.menuinv.api.frames.PagedItems;
import ru.boomearo.menuinv.api.session.InventorySession;

public interface InventoryPage {

    public String getName();

    public MenuType getMenuType();

    public Player getPlayer();

    public PagedItems getListedIconsItems(String name);

    public InventorySession getSession();

    public void setNeedUpdate();

    public default void update() {
        update(false);
    }

    public void update(boolean force);

    public default void reopen() {
        reopen(false);
    }

    public void reopen(boolean force);

    public default void close() {
        close(false);
    }

    public void close(boolean force);

}
