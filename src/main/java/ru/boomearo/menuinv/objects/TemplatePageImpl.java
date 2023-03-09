package ru.boomearo.menuinv.objects;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import ru.boomearo.menuinv.api.FramedIconsHandlerFactory;
import ru.boomearo.menuinv.api.IconHandler;
import ru.boomearo.menuinv.api.IconHandlerFactory;
import ru.boomearo.menuinv.api.InvType;
import ru.boomearo.menuinv.api.InventoryCreationHandler;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.ItemIconTemplate;
import ru.boomearo.menuinv.api.TemplatePage;
import ru.boomearo.menuinv.api.frames.Frame;
import ru.boomearo.menuinv.api.frames.inventory.PagedItems;
import ru.boomearo.menuinv.api.frames.iteration.DefaultIterationHandler;
import ru.boomearo.menuinv.api.frames.iteration.FrameIterationHandler;
import ru.boomearo.menuinv.api.frames.template.FramedIconsTemplate;
import ru.boomearo.menuinv.api.scrolls.ScrollHandler;
import ru.boomearo.menuinv.api.scrolls.ScrollHandlerFactory;
import ru.boomearo.menuinv.api.session.InventorySession;
import ru.boomearo.menuinv.exceptions.MenuInvException;

import java.util.HashMap;
import java.util.Map;

/**
 * Представляет реализацию шаблона страницы
 */
public class TemplatePageImpl implements TemplatePage {

    private final String name;
    private final InvType type;
    private final InventoryCreationHandler creationHandler;

    private final PluginTemplatePagesImpl pluginTemplatePages;

    private final Map<Integer, ItemIconTemplate> itemIcons = new HashMap<>();
    private final Map<String, FramedIconsTemplate> pagedItems = new HashMap<>();
    private IconHandlerFactory background = null;

    public TemplatePageImpl(String name, InvType type, InventoryCreationHandler creationHandler, PluginTemplatePagesImpl pluginTemplatePages) {
        this.name = name;
        this.type = type;
        this.creationHandler = creationHandler;
        this.pluginTemplatePages = pluginTemplatePages;
    }

    public String getName() {
        return this.name;
    }

    public InvType getType() {
        return this.type;
    }

    public InventoryCreationHandler getInventoryCreationHandler() {
        return this.creationHandler;
    }

    public PluginTemplatePagesImpl getPluginTemplatePages() {
        return this.pluginTemplatePages;
    }

    public InventoryPageImpl createNewInventoryPage(Player player, InventorySession session) {
        Map<Integer, ItemIcon> itemIconsActive = new HashMap<>();
        for (ItemIconTemplate tii : this.itemIcons.values()) {
            itemIconsActive.put(tii.getSlot(), new ItemIcon(tii.getSlot(), tii.getFactory().create()));
        }
        Map<String, PagedItems> pagedIconsActive = new HashMap<>();
        for (FramedIconsTemplate tli : this.pagedItems.values()) {
            pagedIconsActive.put(tli.getName(), new PagedItems(tli.getName(), tli.getFirstX(), tli.getFirstZ(), tli.getWidth(), tli.getHeight(), tli.getIconsFactory().create(), tli.getIterationHandler(), tli.isPermanentCached()));
        }

        return new InventoryPageImpl(this.name, this.type, itemIconsActive, pagedIconsActive, this.creationHandler, this.background, player, session, this);
    }

    @Override
    public void addItem(int slot, IconHandlerFactory factory) throws MenuInvException {
        if (factory == null) {
            throw new MenuInvException("factory является нулевым!");
        }

        ItemIconTemplate tmp = this.itemIcons.get(slot);
        if (tmp != null) {
            throw new MenuInvException("Кнопка на позиции '" + slot + "' уже добавлена!");
        }

        addItem(new ItemIconTemplate(slot, factory));
    }

    @Override
    public void addPagedItems(String name, int x, int z, int width, int height, FramedIconsHandlerFactory iconFactory) throws MenuInvException {
        addPagedItems(name, x, z, width, height, iconFactory, false);
    }

    @Override
    public void addPagedItems(String name, int x, int z, int width, int height, FramedIconsHandlerFactory iconFactory, boolean permanentCached) throws MenuInvException {
        addPagedItems(name, x, z, width, height, iconFactory, new DefaultIterationHandler(), permanentCached);
    }

    @Override
    public void addPagedItems(String name, int x, int z, int width, int height, FramedIconsHandlerFactory iconFactory, FrameIterationHandler iterationHandler) throws MenuInvException {
        addPagedItems(name, x, z, width, height, iconFactory, iterationHandler, false);
    }

    @Override
    public void addPagedItems(String name, int x, int z, int width, int height, FramedIconsHandlerFactory iconFactory, FrameIterationHandler iterationHandler, boolean permanentCached) throws MenuInvException {
        if (name == null || iconFactory == null || iterationHandler == null) {
            throw new MenuInvException("Указанные аргументы являются нулевыми!");
        }

        FramedIconsTemplate tmp = this.pagedItems.get(name);
        if (tmp != null) {
            throw new MenuInvException("Список кнопок '" + name + "' уже добавлен!");
        }

        FramedIconsTemplate tli = new FramedIconsTemplate(name, x, z, width, height, iconFactory, iterationHandler, permanentCached);

        checkBorder(tli);

        this.pagedItems.put(name, tli);
    }

    @Override
    public void addScrollItem(int slot, String pagedItems, PagedItems.ScrollType type, ScrollHandlerFactory factory) throws MenuInvException {
        if (pagedItems == null || type == null || factory == null) {
            throw new MenuInvException("Указанные аргументы являются нулевыми!");
        }

        ItemIconTemplate tmp = this.itemIcons.get(slot);
        if (tmp != null) {
            throw new MenuInvException("Кнопка на позиции '" + slot + "' уже добавлена!");
        }

        FramedIconsTemplate tli = this.pagedItems.get(pagedItems);
        if (tli == null) {
            throw new MenuInvException("Список кнопок '" + pagedItems + "' не найден!");
        }

        addItem(new ItemIconTemplate(slot, new ScrollIconHandlerFactory(pagedItems, type, factory)));
    }

    @Override
    public void setBackground(IconHandlerFactory factory) throws MenuInvException {
        if (factory == null) {
            throw new MenuInvException("factory является нулевым!");
        }

        if (this.background != null) {
            throw new MenuInvException("Фон уже установлен!");
        }

        this.background = factory;
    }

    private void addItem(ItemIconTemplate icon) throws MenuInvException {
        int slot = icon.getSlot();

        if (slot < 0) {
            throw new MenuInvException("Кнопка '" + icon.getSlot() + "' находится на позиции меньше нуля! (slot: " + slot + ")");
        }
        int maxSlot = this.type.getSize() - 1;
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

        if (frame.getFirstX() + frame.getWidth() > this.type.getWidth()) {
            throw new MenuInvException("Список кнопок '" + frame.getName() + "' вышел за рамки максимального размера области (x: " + (frame.getFirstX() + frame.getWidth()) + " > width: " + this.type.getWidth());
        }

        if (frame.getFirstZ() + frame.getHeight() > this.type.getHeight()) {
            throw new MenuInvException("Список кнопок '" + frame.getName() + "' вышел за рамки максимального размера области (z: " + (frame.getFirstZ() + frame.getHeight()) + " > height: " + this.type.getHeight());
        }
    }

    private static class ScrollIconHandlerFactory implements IconHandlerFactory {

        private final String pagedItems;
        private final PagedItems.ScrollType type;
        private final ScrollHandlerFactory scrollHandlerFactory;

        private static final Sound DISPENSE_SOUND = findCorrectSound();

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
                        page.setNeedUpdate();
                        player.playSound(player.getLocation(), DISPENSE_SOUND, 1, 1);
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

        private static Sound findCorrectSound() {
            Sound sound;
            try {
                sound = Sound.valueOf("BLOCK_DISPENSER_FAIL");
            }
            catch (Exception e) {
                sound = Sound.CLICK;
            }
            return sound;
        }
    }

}
