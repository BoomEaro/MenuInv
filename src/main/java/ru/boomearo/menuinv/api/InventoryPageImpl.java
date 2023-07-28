package ru.boomearo.menuinv.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ru.boomearo.menuinv.MenuInv;
import ru.boomearo.menuinv.api.session.InventorySession;
import ru.boomearo.menuinv.api.frames.inventory.PagedItems;

import java.util.*;

/**
 * Реализация страницы инвентаря
 */
public class InventoryPageImpl implements InventoryPage {

    private final String name;
    private final MenuType menuType;
    private final InventoryTitleHandler inventoryTitleHandler;
    private final InventoryReopenHandler inventoryReopenHandler;
    private final ClickExceptionHandler clickExceptionHandler;
    private final UpdateExceptionHandler updateExceptionHandler;

    private final Map<String, PagedItems> listedIcons;

    private final ItemIcon[] activeIcons;

    private Inventory inventory;

    private final Player player;

    private final InventorySession session;

    //Используется только для того, чтобы узнать от кого был создан этот экземпляр и чтобы можно было узнать плагин создавший этот шаблон.
    private final TemplatePageImpl templatePage;

    private boolean changes = false;

    public InventoryPageImpl(String name,
                             MenuType menuType,
                             Map<Integer, ItemIcon> iconsPosition,
                             Map<String, PagedItems> listedIcons,
                             InventoryTitleHandler inventoryTitleHandler,
                             InventoryReopenHandler inventoryReopenHandler,
                             ClickExceptionHandler clickExceptionHandler,
                             UpdateExceptionHandler updateExceptionHandler,
                             IconHandlerFactory background,
                             Player player,
                             InventorySession session,
                             TemplatePageImpl templatePage) {
        this.name = name;
        this.menuType = menuType;
        this.listedIcons = listedIcons;
        this.inventoryTitleHandler = inventoryTitleHandler;
        this.inventoryReopenHandler = inventoryReopenHandler;
        this.clickExceptionHandler = clickExceptionHandler;
        this.updateExceptionHandler = updateExceptionHandler;
        this.player = player;
        this.session = session;
        this.templatePage = templatePage;

        //Создаем новый инвентарь баккита и добавляет в него свой холдер для идентификации инвентари
        this.inventory = this.menuType.createInventory(new MenuInvHolder(this), this.inventoryTitleHandler.createTitle(this));

        //Создаем массив активных предметов размеров в текущий инвентарь
        this.activeIcons = new ItemIcon[this.menuType.getSize()];
        //Заполняем массив нулями
        Arrays.fill(this.activeIcons, null);

        //Сначала заполняем массив активных предметов задним фоном.
        if (background != null) {
            for (int i = 0; i < this.menuType.getSize(); i++) {
                this.activeIcons[i] = new ItemIcon(i, background.create());
            }
        }

        //Затем заполняем массив активных предметов самостоятельными предметами.
        for (ItemIcon ii : iconsPosition.values()) {
            this.activeIcons[ii.getSlot()] = ii;
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public MenuType getMenuType() {
        return this.menuType;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public InventorySession getSession() {
        return this.session;
    }

    @Override
    public void setNeedUpdate() {
        this.changes = true;
    }

    @Override
    public PagedItems getListedIconsItems(String name) {
        return this.listedIcons.get(name);
    }

    public ItemIcon[] getUnsafeActiveIcons() {
        return this.activeIcons;
    }

    public org.bukkit.inventory.Inventory getInventory() {
        return this.inventory;
    }

    public void handleInventoryClick(int slot, ClickType type) {
        ItemIcon ii = this.activeIcons[slot];
        if (ii != null) {

            try {
                ii.getHandler().handleClick(this, this.player, type);
            }
            catch (Exception e) {
                this.clickExceptionHandler.onException(this, this.player, type, e);
            }
        }
    }

    public TemplatePageImpl getTemplatePage() {
        return this.templatePage;
    }

    @Override
    public void update(boolean force) {
        performUpdate(force, true);
    }

    private void performUpdate(boolean force, boolean reopenIfNeed) {
        try {
            boolean forceUpdate = this.changes || force;

            if (reopenIfNeed) {
                if (this.inventoryReopenHandler.reopenCondition(this, forceUpdate)) {
                    reopen(true);
                    return;
                }
            }

            ItemStack[] array = new ItemStack[this.menuType.getSize()];
            Arrays.fill(array, null);

            //Обновляем текущий массив активных предметов, используя рамочные предметы.
            for (PagedItems lii : this.listedIcons.values()) {
                lii.updateActiveIcons(this, forceUpdate);
            }

            //Используя массив активных предметов, заполняем массив баккитовских предметов
            for (ItemIcon ii : this.activeIcons) {
                if (ii == null) {
                    continue;
                }

                array[ii.getSlot()] = ii.getItemStack(this, forceUpdate);
            }

            this.inventory.setContents(array);

            this.changes = false;
        }
        catch (Exception e) {
            this.updateExceptionHandler.onException(this, this.player, e);
        }
    }

    @Override
    public void reopen(boolean force) {
        if (force) {
            performReopen();
            return;
        }

        Bukkit.getScheduler().runTask(MenuInv.getInstance(), this::performReopen);
    }

    //Пересоздание страницы
    //TODO Работает странно. А именно, если во время открытия инвентаря игрок его закроет, то у игрока откроется фантомный инвентарь.
    private void performReopen() {
        //Сначала создаем новый экземпляр баккитовского инвентаря
        this.inventory = this.menuType.createInventory(new MenuInvHolder(this), this.inventoryTitleHandler.createTitle(this));
        //Очищаем изменения скроллов страницы
        for (PagedItems pi : this.listedIcons.values()) {
            pi.resetChanges();
        }
        //Заполняем инвентарь
        performUpdate(false, false);
        //Открываем этот инвентарь тому игроку
        this.player.openInventory(this.inventory);
    }

    @Override
    public void close(boolean force) {
        if (force) {
            this.player.closeInventory();
            return;
        }

        Bukkit.getScheduler().runTask(MenuInv.getInstance(), this.player::closeInventory);
    }

}
