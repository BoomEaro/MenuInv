package ru.boomearo.menuinv.api;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.boomearo.menuinv.api.frames.PagedIcons;
import ru.boomearo.menuinv.api.frames.PagedIconsImpl;
import ru.boomearo.menuinv.api.icon.*;
import ru.boomearo.menuinv.api.icon.scrolls.ScrollIconHandler;
import ru.boomearo.menuinv.api.session.InventorySession;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Getter
public class InventoryPageImpl implements InventoryPage {

    private final Plugin plugin;
    private final String name;
    private final MenuType menuType;
    private final InventoryTitleHandler inventoryTitleHandler;
    private final InventoryReopenHandler inventoryReopenHandler;
    private final ClickExceptionHandler clickExceptionHandler;
    private final UpdateExceptionHandler updateExceptionHandler;
    private final InventoryCloseHandler inventoryCloseHandler;
    private final Delayable<InventoryPage> globalUpdateDelay;
    private final BottomInventoryClickHandler bottomInventoryClickHandler;

    private final Map<String, PagedIconsImpl> listedIcons;

    private final ItemIconImpl[] activeIcons;

    private Inventory inventory;

    private final Player player;

    private final InventorySession session;

    private final TemplatePageImpl templatePage;

    private long cooldown = 0;

    private boolean needUpdate = false;

    @Setter
    private boolean closed = false;

    public InventoryPageImpl(Plugin plugin,
                             String name,
                             MenuType menuType,
                             Map<Integer, ItemIconImpl> iconsPosition,
                             Map<String, PagedIconsImpl> listedIcons,
                             InventoryTitleHandler inventoryTitleHandler,
                             InventoryReopenHandler inventoryReopenHandler,
                             ClickExceptionHandler clickExceptionHandler,
                             UpdateExceptionHandler updateExceptionHandler,
                             InventoryCloseHandler inventoryCloseHandler,
                             Delayable<InventoryPage> globalUpdateDelay,
                             BottomInventoryClickHandler bottomInventoryClickHandler,
                             IconHandlerFactory background,
                             Player player,
                             InventorySession session,
                             TemplatePageImpl templatePage) {
        this.plugin = plugin;
        this.name = name;
        this.menuType = menuType;
        this.listedIcons = listedIcons;
        this.inventoryTitleHandler = inventoryTitleHandler;
        this.inventoryReopenHandler = inventoryReopenHandler;
        this.clickExceptionHandler = clickExceptionHandler;
        this.updateExceptionHandler = updateExceptionHandler;
        this.inventoryCloseHandler = inventoryCloseHandler;
        this.globalUpdateDelay = globalUpdateDelay;
        this.bottomInventoryClickHandler = bottomInventoryClickHandler;
        this.player = player;
        this.session = session;
        this.templatePage = templatePage;

        // Create a new bukkit inventory and add our own holder to it to identify the inventory
        this.inventory = this.menuType.createInventory(new MenuInventoryHolder(this), this.inventoryTitleHandler.createTitle(this));

        // Create an array of active size items in the current inventory
        this.activeIcons = new ItemIconImpl[this.menuType.getSize()];
        // Filling the array with nulls
        Arrays.fill(this.activeIcons, null);

        // First, we fill the array of active objects with the background.
        if (background != null) {
            for (int i = 0; i < this.menuType.getSize(); i++) {
                this.activeIcons[i] = new ItemIconImpl(i, background.create());
            }
        }

        // Then we fill the array of active objects with independent objects.
        for (ItemIconImpl ii : iconsPosition.values()) {
            this.activeIcons[ii.getSlot()] = ii;
        }
    }

    @Override
    public void setNeedUpdate() {
        this.needUpdate = true;
    }

    @Override
    public PagedIcons getListedIconsItems(String name) {
        return this.listedIcons.get(name);
    }

    public void handleInventoryClick(int slot, ClickType type) {
        ItemIconImpl ii = this.activeIcons[slot];
        if (ii != null) {

            try {
                ii.getIconHandler().handleClick(this, ii, this.player, type);
            } catch (Exception e) {
                this.clickExceptionHandler.onException(this, this.player, type, e);
            }
        }
    }

    @Override
    public void update(boolean force) {
        performUpdate(force, true, false);
    }

    public void updateOnCreate() {
        performUpdate(true, false, true);
    }

    private void performUpdate() {
        performUpdate(false, false, false);
    }

    private void performUpdate(boolean force, boolean reopenIfNeed, boolean create) {
        boolean forceUpdate = this.needUpdate || force;

        try {
            if (this.globalUpdateDelay.canUpdate(this, forceUpdate, this.cooldown) || create) {
                this.cooldown = System.currentTimeMillis();

                if (reopenIfNeed) {
                    try {
                        if (this.inventoryReopenHandler.reopenCondition(this, forceUpdate)) {
                            reopen(true);
                            return;
                        }
                    } catch (Exception e) {
                        this.updateExceptionHandler.onException(this, this.player, e);
                    }
                }

                // Update the current array of active items using frame items.
                for (PagedIconsImpl lii : this.listedIcons.values()) {
                    lii.updateActiveIcons(this, this.activeIcons, forceUpdate, create, this.updateExceptionHandler);
                }

                // Using an array of active items, we fill the array with Bukkit items
                boolean shouldUpdateInventory = false;
                for (ItemIconImpl ii : this.activeIcons) {
                    if (ii == null) {
                        continue;
                    }

                    ItemStack itemStack = ii.getItemStack(this, forceUpdate, create, this.updateExceptionHandler);
                    if (itemStack == null) {
                        continue;
                    }

                    this.inventory.setItem(ii.getSlot(), itemStack);
                    shouldUpdateInventory = true;
                }

                if (shouldUpdateInventory) {
                    this.player.updateInventory();
                }
            }
        }
        catch (Exception e) {
            this.updateExceptionHandler.onException(this, this.player, e);
        }
        finally {
            this.needUpdate = false;
        }
    }

    @Override
    public boolean update(PagedIcons pagedIcons, boolean force) {
        if (!(pagedIcons instanceof PagedIconsImpl)) {
            return false;
        }
        PagedIconsImpl pagedIconsImpl = (PagedIconsImpl) pagedIcons;

        List<ItemIcon> updatedIcons = pagedIconsImpl.updateActiveIcons(this, this.activeIcons, force, false, this.updateExceptionHandler);
        if (updatedIcons == null) {
            return false;
        }

        for (ItemIcon itemIcon : updatedIcons) {
            update(itemIcon, force);
        }

        return true;
    }

    @Override
    public boolean update(ItemIcon itemIcon, boolean force) {
        if (!(itemIcon instanceof ItemIconImpl)) {
            return false;
        }
        ItemIconImpl itemIconImpl = (ItemIconImpl) itemIcon;

        ItemStack itemStack = itemIconImpl.getItemStack(this, force, false, this.updateExceptionHandler);
        if (itemStack == null) {
            return false;
        }

        this.inventory.setItem(itemIconImpl.getSlot(), itemStack);

        return true;
    }

    @Override
    public boolean updateScrolls(String name, boolean force) {
        boolean updated = false;
        for (ItemIconImpl icon : this.activeIcons) {
            if (icon == null) {
                continue;
            }

            IconHandler iconHandler = icon.getIconHandler();

            if (!(iconHandler instanceof ScrollIconHandler)) {
                continue;
            }
            ScrollIconHandler scrollIconHandler = (ScrollIconHandler) iconHandler;

            if (!scrollIconHandler.getName().equals(name)) {
                continue;
            }

            if (update(icon, force)) {
                updated = true;
            }
        }

        return updated;
    }

    @Override
    public void reopen(boolean force) {
        if (force) {
            performReopen();
            return;
        }

        Bukkit.getScheduler().runTask(this.plugin, this::performReopen);
    }

    // Recreate the page
    // TODO Works weird. Namely, if during the opening of the inventory the player closes it, then the player will open a phantom inventory.
    private void performReopen() {
        // First, create a new instance of bukkit's inventory
        this.inventory = this.menuType.createInventory(new MenuInventoryHolder(this), this.inventoryTitleHandler.createTitle(this));
        // Clear page scroll changes
        for (PagedIconsImpl pi : this.listedIcons.values()) {
            pi.resetChanges();
        }
        // Filling inventory
        performUpdate();
        // Open this inventory to that player
        this.player.openInventory(this.inventory);
    }

    @Override
    public void close(boolean force) {
        if (force) {
            this.player.closeInventory();
            return;
        }

        Bukkit.getScheduler().runTask(this.plugin, this.player::closeInventory);
    }

    @Override
    public boolean isClosed() {
        return this.closed;
    }

    @Override
    public boolean isHandlerExists(IconHandler iconHandler) {
        for (ItemIconImpl icon : this.activeIcons) {
            if (icon == null) {
                continue;
            }

            if (icon.getIconHandler() == iconHandler) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemIcon getItemIconBySlot(int slot) {
        return this.activeIcons[slot];
    }

}
