package ru.boomearo.menuinv.objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.MenuInv;
import ru.boomearo.menuinv.api.AbstractButtonHandler;
import ru.boomearo.menuinv.api.InvType;

import java.util.Arrays;

public class InventoryPage {

    private final String name;
    private final InvType type;
    private final String title;

    private final InventoryElements elements;

    private final Inventory inventory;

    private final Player player;

    public InventoryPage(String name, InvType type, String title, int height, InventoryElements elements, Player player) {
        this.name = name;
        this.type = type;
        this.title = title;
        this.elements = elements;
        this.player = player;

        if (this.type == InvType.Chest) {
            this.inventory = Bukkit.createInventory(new MenuInvHolder(this), height * this.type.getMaxWidth(), this.title);
        }
        else {
            this.inventory = Bukkit.createInventory(new MenuInvHolder(this), this.type.getInventoryType(), this.title);
        }

    }

    public String getName() {
        return this.name;
    }

    public InvType getType() {
        return this.type;
    }

    public String getTitle() {
        return this.title;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void handleInventoryClick(int slot, ClickType type) {
        ItemIcon ii = this.elements.getItemIcon(slot);
        if (ii != null) {

            ii.getHandler().handleClick(this, this.player, type);
            return;
        }

    }


    public void update() {
        //long start = System.nanoTime();
        update(false);
        //long end = System.nanoTime();

        //MenuInv.getInstance().getLogger().info("test " + (end - start));
    }

    public void update(boolean force) {
        update(false, force);
    }

    //TODO Я хз, нужно ли оптимизировать, так как вызывается обновление каждый тик.
    //TODO По замерам вроде вообще проблем нет
    public void update(boolean clean, boolean force) {

        ItemStack[] array;

        if (clean) {
            array = new ItemStack[this.inventory.getSize()];
            Arrays.fill(array, null);
        }
        else {
            //TODO А точно нужно копировать массив? Я его сейчас просто переиспользую а затем в конце заново устанавливаю..
            //TODO Если конечно баккит каждый раз не создает новый..
            array = this.inventory.getContents();
        }

        for (ItemIcon ii : this.elements.getAllItemIcon()) {
            AbstractButtonHandler handler = ii.getHandler();

            if (((System.currentTimeMillis() - ii.getUpdateHandlerCooldown()) > (handler.getUpdateTime() * 50)) || force) {
                ii.resetUpdateHandlerCooldown();
                array[ii.getPosition()] = ii.getHandler().update(this, this.player);
            }
        }

        this.inventory.setContents(array);

        this.player.updateInventory();
    }

    public void close() {
        Bukkit.getScheduler().runTask(MenuInv.getInstance(), this.player::closeInventory);
    }

}
