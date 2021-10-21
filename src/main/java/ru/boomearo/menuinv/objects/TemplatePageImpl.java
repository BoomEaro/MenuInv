package ru.boomearo.menuinv.objects;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import ru.boomearo.menuinv.api.*;
import ru.boomearo.menuinv.api.frames.Frame;
import ru.boomearo.menuinv.api.frames.inventory.PagedItems;
import ru.boomearo.menuinv.api.frames.template.FramedIconsTemplate;
import ru.boomearo.menuinv.api.scrolls.ScrollHandler;
import ru.boomearo.menuinv.api.scrolls.ScrollHandlerFactory;
import ru.boomearo.menuinv.exceptions.MenuInvException;

import java.util.HashMap;
import java.util.Map;

public class TemplatePageImpl implements TemplatePage {

    private final String name;
    private final String title;
    private final InvType type;

    private final int height;

    private final Map<Integer, ItemIconTemplate> itemIcons = new HashMap<>();
    private final Map<String, FramedIconsTemplate> pagedItems = new HashMap<>();

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

    public InventoryPageImpl createNewInventoryPage(Player player) {
        Map<Integer, ItemIcon> itemIconsActive = new HashMap<>();
        for (ItemIconTemplate tii : this.itemIcons.values()) {
            itemIconsActive.put(tii.getSlot(), new ItemIcon(tii.getSlot(), tii.getFactory().create()));
        }
        Map<String, PagedItems> pagedIconsActive = new HashMap<>();
        for (FramedIconsTemplate tli : this.pagedItems.values()) {
            pagedIconsActive.put(tli.getName(), new PagedItems(tli.getName(), tli.getFirstX(), tli.getFirstZ(), tli.getWidth(), tli.getHeight(), tli.getFactory().create()));
        }

        return new InventoryPageImpl(this.name, this.type, this.title, this.height, itemIconsActive, pagedIconsActive, player);
    }

    @Override
    public void addButton(int slot, IconHandlerFactory factory) throws MenuInvException {
        ItemIconTemplate tmp = this.itemIcons.get(slot);
        if (tmp != null) {
            throw new MenuInvException("Кнопка на позиции '" + slot + "' уже добавлена!");
        }

        addButton(new ItemIconTemplate(slot, factory));
    }

    @Override
    public void addPagedItems(String name, int x, int z, int width, int height, FramedIconsHandlerFactory factory) throws MenuInvException {
        FramedIconsTemplate tmp = this.pagedItems.get(name);
        if (tmp != null) {
            throw new MenuInvException("Список кнопок '" + name + "' уже добавлена!");
        }

        FramedIconsTemplate tli = new FramedIconsTemplate(name, x, z, width, height, factory);

        checkBorder(tli);

        this.pagedItems.put(name, tli);
    }

    @Override
    public void addScrollButton(int slot, String pagedItems, PagedItems.ScrollType type, ScrollHandlerFactory factory) throws MenuInvException {
        ItemIconTemplate tmp = this.itemIcons.get(slot);
        if (tmp != null) {
            throw new MenuInvException("Кнопка на позиции '" + slot + "' уже добавлена!");
        }

        FramedIconsTemplate tli = this.pagedItems.get(pagedItems);
        if (tli == null) {
            throw new MenuInvException("Список кнопок '" + pagedItems + "' не найден!");
        }

        addButton(new ItemIconTemplate(slot, new ScrollIconHandlerFactory(pagedItems, type, factory)));
    }

    public void addButton(ItemIconTemplate icon) throws MenuInvException {
        int slot = icon.getSlot();

        if (slot < 0) {
            throw new MenuInvException("Кнопка '" + icon.getSlot() + "' находится на позиции меньше нуля! (slot: " + slot + ")");
        }
        int maxSlot = this.type.getMaxWidth() - 1;
        if (slot > maxSlot) {
            throw new MenuInvException("Кнопка '" + icon.getSlot() + "' находится на позиции больше допустимой! (slot: " + slot + "/" + maxSlot + ")");
        }

        this.itemIcons.put(icon.getSlot(), icon);
    }

    private void checkBorder(Frame frame) throws MenuInvException {

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

    private static class ScrollIconHandlerFactory implements IconHandlerFactory {

        private final String pagedItems;
        private final PagedItems.ScrollType type;
        private final ScrollHandlerFactory scrollHandlerFactory;

        public ScrollIconHandlerFactory(String pagedItems, PagedItems.ScrollType type, ScrollHandlerFactory scrollHandlerFactory) {
            this.pagedItems = pagedItems;
            this.type = type;
            this.scrollHandlerFactory = scrollHandlerFactory;
        }

        @Override
        public IconHandler create() {
            ScrollHandler handler = this.scrollHandlerFactory.create();

            return new IconHandler() {

                @Override
                public void onClick(InventoryPage page, Player player, ClickType clickType) {
                    boolean change = page.getListedIconsItems(ScrollIconHandlerFactory.this.pagedItems).scrollPage(ScrollIconHandlerFactory.this.type);
                    if (change) {
                        page.update(true);
                    }
                }

                @Override
                public ItemStack onUpdate(InventoryPage page, Player player) {
                    PagedItems lii = page.getListedIconsItems(ScrollIconHandlerFactory.this.pagedItems);

                    if (ScrollIconHandlerFactory.this.type == PagedItems.ScrollType.NEXT) {
                        if (lii.getCurrentPage() >= lii.getMaxPage()) {
                            return handler.onHide(lii.getCurrentPage(), lii.getMaxPage());
                        }
                        else {
                            return handler.onVisible(lii.getCurrentPage(), lii.getMaxPage());
                        }
                    }
                    else if (ScrollIconHandlerFactory.this.type == PagedItems.ScrollType.PREVIOUSLY) {
                        if (lii.getCurrentPage() <= 1) {
                            return handler.onHide(lii.getCurrentPage(), lii.getMaxPage());
                        }
                        else {
                            return handler.onVisible(lii.getCurrentPage(), lii.getMaxPage());
                        }
                    }
                    return null;
                }

            };
        }
    }

}
