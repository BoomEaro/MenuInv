package ru.boomearo.menuinv.objects;

import org.bukkit.entity.Player;
import ru.boomearo.menuinv.api.AbstractButtonHandler;
import ru.boomearo.menuinv.api.ListedButtonHandler;
import ru.boomearo.menuinv.api.TemplatePage;
import ru.boomearo.menuinv.api.InvType;
import ru.boomearo.menuinv.exceptions.MenuInvException;

import java.util.HashMap;
import java.util.Map;

public class TemplatePageImpl implements TemplatePage {

    private final String name;
    private final String title;
    private final InvType type;

    private final int height;

    private final Map<Integer, TemplateItemIcon> iconsPosition = new HashMap<>();

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
        InventoryElements newElements = new InventoryElements(itemIcons);

        return new InventoryPage(this.name, this.type, this.title, this.height, newElements, player);
    }

    @Override
    public void addButton(int position, AbstractButtonHandler handler) throws MenuInvException {
        TemplateItemIcon iconPosition = this.iconsPosition.get(position);
        if (iconPosition != null) {
            throw new MenuInvException("Кнопка на позиции '" + position + "' уже добавлена!");
        }

        this.iconsPosition.put(position, new TemplateItemIcon(position, handler));
    }

    @Override
    public void addListedButton(String name, int x, int z, int width, int height, ListedButtonHandler listedButton) {

    }
}
