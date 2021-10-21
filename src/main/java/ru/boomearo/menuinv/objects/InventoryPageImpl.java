package ru.boomearo.menuinv.objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

import org.bukkit.inventory.ItemStack;

import ru.boomearo.menuinv.MenuInv;
import ru.boomearo.menuinv.api.InvType;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.InventorySession;
import ru.boomearo.menuinv.api.frames.inventory.PagedItems;

import java.util.*;

public class InventoryPageImpl implements InventoryPage {

    private final String name;
    private final InvType type;
    private final String title;

    private final Map<String, PagedItems> listedIcons;

    private final ItemIcon[] activeIcons;

    private final Inventory inventory;

    private final Player player;

    private final InventorySession session;

    //Используется только для того, чтобы узнать от кого был создан этого экземпляр и чтобы можно было узнать плагин создавший этот чертеж.
    private final TemplatePageImpl templatePage;

    public InventoryPageImpl(String name, InvType type, String title, int height, Map<Integer, ItemIcon> iconsPosition, Map<String, PagedItems> listedIcons,
                             Player player, InventorySession session, TemplatePageImpl templatePage) {
        this.name = name;
        this.type = type;
        this.title = title;
        this.listedIcons = listedIcons;
        this.player = player;
        this.session = session;
        this.templatePage = templatePage;

        if (this.type == InvType.Chest) {
            this.inventory = Bukkit.createInventory(new MenuInvHolder(this), height * this.type.getMaxWidth(), this.title);
        }
        else {
            this.inventory = Bukkit.createInventory(new MenuInvHolder(this), this.type.getInventoryType(), this.title);
        }

        this.activeIcons = new ItemIcon[this.inventory.getSize()];
        Arrays.fill(this.activeIcons, null);

        for (ItemIcon ii : iconsPosition.values()) {
            this.activeIcons[ii.getSlot()] = ii;
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public InvType getType() {
        return this.type;
    }

    @Override
    public String getTitle() {
        return this.title;
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
    public PagedItems getListedIconsItems(String name) {
        return this.listedIcons.get(name);
    }

    public ItemIcon[] getUnsafeActiveIcons() {
        return this.activeIcons;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void handleInventoryClick(int slot, ClickType type) {
        ItemIcon ii = this.activeIcons[slot];
        if (ii != null) {

            ii.getHandler().handleClick(this, this.player, type);
        }
    }

    public TemplatePageImpl getTemplatePage() {
        return this.templatePage;
    }

    @Override
    public void update() {
        //long start = System.nanoTime();
        update(false);
        //long end = System.nanoTime();

        //MenuInv.getInstance().getLogger().info("test " + (end - start));
    }
    //TODO Я хз, нужно ли оптимизировать, так как вызывается обновление каждый тик.
    //TODO По замерам вроде вообще проблем нет
    @Override
    public void update(boolean force) {

        ItemStack[] array = new ItemStack[this.inventory.getSize()];
        Arrays.fill(array, null);

        for (PagedItems lii : this.listedIcons.values()) {
            lii.updateActiveIcons(this, force);
        }

        //MenuInv.getInstance().getLogger().info("test " + this.activeIcons.toString());

        for (ItemIcon ii : this.activeIcons) {
            if (ii == null) {
                continue;
            }

            array[ii.getSlot()] = ii.getItemStack(this, force);
        }

        this.inventory.setContents(array);

        this.player.updateInventory();
    }

    @Override
    public void close() {
        close(false);
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
