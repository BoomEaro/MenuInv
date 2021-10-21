package ru.boomearo.menuinv.api;

import org.bukkit.entity.Player;
import ru.boomearo.menuinv.api.frames.inventory.PagedItems;

public interface InventoryPage {

    public String getName();

    public InvType getType();

    public String getTitle();

    public Player getPlayer();

    public PagedItems getListedIconsItems(String name);

    public InventorySession getSession();

    public void update();

    public void update(boolean force);

    public void close();

    public void close(boolean force);
}
