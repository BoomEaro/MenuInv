package ru.boomearo.menuinv.objects;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.MenuInv;
import ru.boomearo.menuinv.api.*;
import ru.boomearo.menuinv.api.frames.FramedIcons;
import ru.boomearo.menuinv.api.frames.ListedIconItems;
import ru.boomearo.menuinv.api.frames.TemplateListedIcons;
import ru.boomearo.menuinv.exceptions.MenuInvException;

import java.util.HashMap;
import java.util.Map;

public class TemplatePageImpl implements TemplatePage {

    private final String name;
    private final String title;
    private final InvType type;

    private final int height;

    private final Map<Integer, TemplateItemIcon> iconsPosition = new HashMap<>();
    private final Map<String, TemplateListedIcons> listedIcons = new HashMap<>();

    public TemplatePageImpl(String name, String title, InvType type, int height) {
        this.name = name;
        this.title = title;
        this.type = type;
        this.height = height;
    }

    public String getName() {
        return this.name;
    }

    public String getTitle() {
        return this.title;
    }

    public InvType getType() {
        return this.type;
    }

    public int getHeight() {
        return this.height;
    }

    public InventoryPage createNewInventoryPage(Player player) {
        Map<Integer, ItemIcon> itemIcons = new HashMap<>();
        for (TemplateItemIcon tii : this.iconsPosition.values()) {
            itemIcons.put(tii.getSlot(), new ItemIcon(tii.getSlot(), tii.getFactory().create()));
        }
        Map<String, ListedIconItems> listedIcons = new HashMap<>();
        for (TemplateListedIcons tli : this.listedIcons.values()) {
            listedIcons.put(tli.getName(), new ListedIconItems(tli.getName(), tli.getFirstX(), tli.getFirstZ(), tli.getWidth(), tli.getHeight(), tli.getHandler()));
        }

        return new InventoryPage(this.name, this.type, this.title, this.height, itemIcons, listedIcons, player);
    }

    @Override
    public void addButton(int slot, ButtonHandlerFactory factory) throws MenuInvException {
        TemplateItemIcon tmp = this.iconsPosition.get(slot);
        if (tmp != null) {
            throw new MenuInvException("Кнопка на позиции '" + slot + "' уже добавлена!");
        }

        addButton(new TemplateItemIcon(slot, factory));
    }

    @Override
    public void addListedButton(String name, int x, int z, int width, int height, ListedIconsHandler handler) throws MenuInvException {
        TemplateListedIcons tmp = this.listedIcons.get(name);
        if (tmp != null) {
            throw new MenuInvException("Список кнопок '" + name + "' уже добавлена!");
        }

        TemplateListedIcons tli = new TemplateListedIcons(name, x, z, width, height, handler);

        checkBorder(tli);

        this.listedIcons.put(name, tli);
    }

    @Override
    public void addScrollButton(int slot, String listedButton, ListedIconItems.ScrollType type, ScrollHandler handler) throws MenuInvException {
        TemplateItemIcon tmp = this.iconsPosition.get(slot);
        if (tmp != null) {
            throw new MenuInvException("Кнопка на позиции '" + slot + "' уже добавлена!");
        }

        TemplateListedIcons tli = this.listedIcons.get(listedButton);
        if (tli == null) {
            throw new MenuInvException("Список кнопок '" + listedButton + "' не найден!");
        }

        TemplateItemIcon icon = new TemplateItemIcon(slot, () -> new AbstractButtonHandler() {

            @Override
            public void onClick(InventoryPage page, Player player, ClickType clickType) {
                boolean change = page.getListedIconsItems(listedButton).scrollPage(type);
                if (change) {
                    page.update(true);
                }
            }

            @Override
            public ItemStack onUpdate(InventoryPage page, Player player) {
                ListedIconItems lii = page.getListedIconsItems(listedButton);

                if (type == ListedIconItems.ScrollType.NEXT) {
                    if (lii.getCurrentPage() >= lii.getMaxPage()) {
                        return handler.onHide(lii.getCurrentPage(), lii.getMaxPage());
                    }
                    else {
                        return handler.onVisible(lii.getCurrentPage(), lii.getMaxPage());
                    }
                }
                else if (type == ListedIconItems.ScrollType.PREVIOUSLY) {
                    if (lii.getCurrentPage() <= 1) {
                        return handler.onHide(lii.getCurrentPage(), lii.getMaxPage());
                    }
                    else {
                        return handler.onVisible(lii.getCurrentPage(), lii.getMaxPage());
                    }
                }
                return null;
            }

        });

        addButton(icon);
    }

    public void addButton(TemplateItemIcon icon) throws MenuInvException {
        int slot = icon.getSlot();

        if (slot < 0) {
            throw new MenuInvException("Кнопка '" + icon.getSlot() + "' находится на позиции меньше нуля! (slot: " + slot + ")");
        }
        int maxSlot = this.type.getMaxWidth() - 1;
        if (slot > maxSlot) {
            throw new MenuInvException("Кнопка '" + icon.getSlot() + "' находится на позиции больше допустимой! (slot: " + slot + "/" + maxSlot + ")");
        }

        this.iconsPosition.put(icon.getSlot(), icon);
    }

    private void checkBorder(FramedIcons frame) throws MenuInvException {

        //MenuInv.getInstance().getLogger().info("test " + (frame.getFirstX() + frame.getWidth()) + " > " + this.type.getMaxWidth() + " | " + (frame.getFirstZ() + frame.getHeight()) + " > " + this.height);

        if (frame.getFirstX() < 0 || frame.getFirstZ() < 0) {
            throw new MenuInvException("Список кнопок '" + frame.getName() + "' вышел за рамки области имея отрицательное значение координат. (x: " + frame.getFirstX() + " z: " + frame.getFirstZ());
        }

        if (frame.getFirstX() + frame.getWidth() > this.type.getMaxWidth()) {
            throw new MenuInvException("Список кнопок '" + frame.getName() + "' вышел за рамки максимального размера области (x: " + (frame.getFirstX() + frame.getWidth()) + " > width: " + this.type.getMaxWidth());
        }

        if (frame.getFirstZ() + frame.getHeight() > this.height) {
            throw new MenuInvException("Список кнопок '" + frame.getName() + "' вышел за рамки максимального размера области (z: " + (frame.getFirstZ() + frame.getHeight()) + " > height: " + this.height);
        }
    }


}
