package ru.boomearo.menuinv.api;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import ru.boomearo.menuinv.api.frames.PagedIcons;
import ru.boomearo.menuinv.api.icon.BottomInventoryClickHandler;
import ru.boomearo.menuinv.api.icon.IconHandler;
import ru.boomearo.menuinv.api.icon.ItemIcon;
import ru.boomearo.menuinv.api.session.InventorySession;

import javax.annotation.Nullable;

public interface InventoryPage {

    @NonNull
    String getName();

    @NonNull
    Inventory getInventory();

    @Deprecated
    @NonNull
    MenuType getMenuType();

    @NonNull
    InventoryFactory getInventoryFactory();

    @NonNull
    Player getPlayer();

    @Nullable
    PagedIcons getListedIconsItems(@NonNull String name);

    @NonNull
    InventorySession getSession();

    void setNeedUpdate();

    default boolean updateScrolls(@NonNull String name) {
        return updateScrolls(name, false);
    }

    boolean updateScrolls(@NonNull String name, boolean force);

    default boolean update(@NonNull PagedIcons pagedIcons) {
        return update(pagedIcons, false);
    }

    boolean update(@NonNull PagedIcons pagedIcons, boolean force);

    default boolean update(@NonNull ItemIcon itemIcon) {
        return update(itemIcon, false);
    }

    boolean update(@NonNull ItemIcon itemIcon, boolean force);

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

    @NonNull
    TemplatePage getTemplatePage();

    @NonNull
    InventoryCloseHandler getInventoryCloseHandler();

    @NonNull
    BottomInventoryClickHandler getBottomInventoryClickHandler();

    boolean isHandlerExists(@NonNull IconHandler iconHandler);

    @NonNull
    ItemIcon getItemIconBySlot(int slot);

}
