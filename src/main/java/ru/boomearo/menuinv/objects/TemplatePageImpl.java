package ru.boomearo.menuinv.objects;

import org.bukkit.entity.Player;
import ru.boomearo.menuinv.api.*;
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
            itemIcons.put(tii.getPosition(), new ItemIcon(tii.getPosition(), tii.getHandler()));
        }
        Map<String, ListedIconItems> listedIcons = new HashMap<>();
        for (TemplateListedIcons tli : this.listedIcons.values()) {
            listedIcons.put(tli.getName(), new ListedIconItems(tli.getName(), tli.getX(), tli.getZ(), tli.getWidth(), tli.getHeight(), tli.getHandler()));
        }

        return new InventoryPage(this.name, this.type, this.title, this.height, itemIcons, listedIcons, player);
    }

    //TODO Добавить куча проверок на пересечение и прочее
    @Override
    public void addButton(int slot, AbstractButtonHandler handler) throws MenuInvException {
        TemplateItemIcon tmp = this.iconsPosition.get(slot);
        if (tmp != null) {
            throw new MenuInvException("Кнопка на позиции '" + slot + "' уже добавлена!");
        }

        this.iconsPosition.put(slot, new TemplateItemIcon(slot, handler));
    }

    //TODO Добавить куча проверок на пересечение и прочее
    @Override
    public void addListedButton(String name, int x, int z, int width, int height, ListedIconsHandler handler) throws MenuInvException {
        TemplateListedIcons tmp = this.listedIcons.get(name);
        if (tmp != null) {
            throw new MenuInvException("Список кнопок '" + name + "' уже добавлена!");
        }

        this.listedIcons.put(name, new TemplateListedIcons(name, x, z, width, height, handler));
    }
}
