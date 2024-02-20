package ru.boomearo.menuinv.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import ru.boomearo.menuinv.api.frames.PagedIcons;
import ru.boomearo.menuinv.api.icon.BottomInventoryClickHandler;
import ru.boomearo.menuinv.api.icon.IconHandler;
import ru.boomearo.menuinv.api.icon.ItemIcon;
import ru.boomearo.menuinv.api.session.InventorySession;

public interface InventoryPage {

    String getName();

    Inventory getInventory();

    MenuType getMenuType();

    Player getPlayer();

    PagedIcons getListedIconsItems(String name);

    InventorySession getSession();

    void setNeedUpdate();

    default boolean updateScrolls(String name) {
        return updateScrolls(name, false);
    }

    boolean updateScrolls(String name, boolean force);

    default boolean update(PagedIcons pagedIcons) {
        return update(pagedIcons, false);
    }

    boolean update(PagedIcons pagedIcons, boolean force);

    default boolean update(ItemIcon itemIcon) {
        return update(itemIcon, false);
    }

    boolean update(ItemIcon itemIcon, boolean force);

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

    TemplatePage getTemplatePage();

    InventoryCloseHandler getInventoryCloseHandler();

    BottomInventoryClickHandler getBottomInventoryClickHandler();

    boolean isHandlerExists(IconHandler iconHandler);

    ItemIcon getItemIconBySlot(int slot);

}
