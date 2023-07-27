package ru.boomearo.menuinv.api;

import com.google.common.base.Preconditions;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import ru.boomearo.menuinv.api.frames.Frame;
import ru.boomearo.menuinv.api.frames.inventory.PagedItems;
import ru.boomearo.menuinv.api.frames.iteration.DefaultIterationHandler;
import ru.boomearo.menuinv.api.frames.iteration.FrameIterationHandler;
import ru.boomearo.menuinv.api.frames.template.FramedIconsTemplate;
import ru.boomearo.menuinv.api.scrolls.ScrollHandler;
import ru.boomearo.menuinv.api.scrolls.ScrollHandlerFactory;
import ru.boomearo.menuinv.api.session.InventorySession;

import java.util.HashMap;
import java.util.Map;

/**
 * Представляет реализацию шаблона страницы
 */
public class TemplatePageImpl implements TemplatePage {

    private final String name;
    private InvType type = InvType.CHEST_9X6;
    private InventoryCreationHandler inventoryCreationHandler = (inventoryPage) -> "Default page";
    private InventoryReopenHandler inventoryReopenHandler = (inventoryPage, force) -> false;

    private final PluginTemplatePagesImpl pluginTemplatePages;

    private final Map<Integer, ItemIconTemplate> itemIcons = new HashMap<>();
    private final Map<String, FramedIconsTemplate> pagedItems = new HashMap<>();
    private IconHandlerFactory background = null;

    public TemplatePageImpl(String name, PluginTemplatePagesImpl pluginTemplatePages) {
        this.name = name;
        this.pluginTemplatePages = pluginTemplatePages;
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
    public TemplatePage setType(InvType type) {
        Preconditions.checkArgument(type != null, "type is null!");

        this.type = type;
        return this;
    }

    @Override
    public InventoryCreationHandler getInventoryCreationHandler() {
        return this.inventoryCreationHandler;
    }

    @Override
    public TemplatePage setInventoryCreationHandler(InventoryCreationHandler inventoryCreationHandler) {
        Preconditions.checkArgument(inventoryCreationHandler != null, "inventoryCreationHandler is null!");

        this.inventoryCreationHandler = inventoryCreationHandler;
        return this;
    }

    @Override
    public InventoryReopenHandler getInventoryReopenHandler() {
        return this.inventoryReopenHandler;
    }

    @Override
    public TemplatePage setInventoryReopenHandler(InventoryReopenHandler inventoryReopenHandler) {
        Preconditions.checkArgument(inventoryReopenHandler != null, "inventoryReopenHandler is null!");

        this.inventoryReopenHandler = inventoryReopenHandler;
        return this;
    }

    @Override
    public TemplatePage setItem(int slot, IconHandlerFactory factory) {
        Preconditions.checkArgument(factory != null, "factory is null!");

        ItemIconTemplate tmp = this.itemIcons.get(slot);
        if (tmp != null) {
            throw new IllegalStateException("Button on slot '" + slot + "' already added!");
        }

        addItem(new ItemIconTemplate(slot, factory));
        return this;
    }

    @Override
    public TemplatePage setPagedItems(String name, int x, int z, int width, int height, FramedIconsHandlerFactory iconFactory) {
        return setPagedItems(name, x, z, width, height, iconFactory, false);
    }

    @Override
    public TemplatePage setPagedItems(String name, int x, int z, int width, int height, FramedIconsHandlerFactory iconFactory, boolean permanentCached) {
        return setPagedItems(name, x, z, width, height, iconFactory, new DefaultIterationHandler(), permanentCached);
    }

    @Override
    public TemplatePage setPagedItems(String name, int x, int z, int width, int height, FramedIconsHandlerFactory iconFactory, FrameIterationHandler iterationHandler) {
        return setPagedItems(name, x, z, width, height, iconFactory, iterationHandler, false);
    }

    @Override
    public TemplatePage setPagedItems(String name, int x, int z, int width, int height, FramedIconsHandlerFactory iconFactory, FrameIterationHandler iterationHandler, boolean permanentCached) {
        Preconditions.checkArgument(name != null, "name is null!");
        Preconditions.checkArgument(iconFactory != null, "iconFactory is null!");
        Preconditions.checkArgument(iterationHandler != null, "iterationHandler is null!");

        FramedIconsTemplate tmp = this.pagedItems.get(name);
        if (tmp != null) {
            throw new IllegalStateException("Paged items with name '" + name + "' already added!");
        }

        FramedIconsTemplate tli = new FramedIconsTemplate(name, x, z, width, height, iconFactory, iterationHandler, permanentCached);

        checkBorder(tli);

        this.pagedItems.put(name, tli);

        return this;
    }

    @Override
    public TemplatePage setScrollItem(int slot, String pagedItems, PagedItems.ScrollType type, ScrollHandlerFactory factory) {
        Preconditions.checkArgument(pagedItems != null, "pagedItems is null!");
        Preconditions.checkArgument(type != null, "type is null!");
        Preconditions.checkArgument(factory != null, "factory is null!");

        ItemIconTemplate tmp = this.itemIcons.get(slot);
        if (tmp != null) {
            throw new IllegalStateException("Button on position '" + slot + "' already added!");
        }

        FramedIconsTemplate tli = this.pagedItems.get(pagedItems);
        if (tli == null) {
            throw new IllegalStateException("Paged items with name '" + pagedItems + "' is not found!");
        }

        addItem(new ItemIconTemplate(slot, new ScrollIconHandlerFactory(pagedItems, type, factory)));

        return this;
    }

    @Override
    public TemplatePage setBackground(IconHandlerFactory factory) {
        Preconditions.checkArgument(factory != null, "factory is null!");

        this.background = factory;

        return this;
    }

    private void addItem(ItemIconTemplate icon) {
        int slot = icon.getSlot();

        if (slot < 0) {
            throw new IllegalStateException("Button on slot '" + icon.getSlot() + "' is outside 0! (slot: " + slot + ")");
        }
        int maxSlot = this.type.getSize() - 1;
        if (slot > maxSlot) {
            throw new IllegalStateException("Button on slot '" + icon.getSlot() + "' is more than possible! (slot: " + slot + "/" + maxSlot + ")");
        }

        this.itemIcons.put(icon.getSlot(), icon);
    }

    private void checkBorder(Frame frame) {
        if (frame.getFirstX() < 0 || frame.getFirstZ() < 0) {
            throw new IllegalStateException("Paged items with name '" + frame.getName() + "' went out of the area having a negative value of coordinates. (x: " + frame.getFirstX() + " z: " + frame.getFirstZ());
        }

        if (frame.getFirstX() + frame.getWidth() > this.type.getWidth()) {
            throw new IllegalStateException("Paged items with name '" + frame.getName() + "' went beyond the maximum area size (x: " + (frame.getFirstX() + frame.getWidth()) + " > width: " + this.type.getWidth());
        }

        if (frame.getFirstZ() + frame.getHeight() > this.type.getHeight()) {
            throw new IllegalStateException("Paged items with name '" + frame.getName() + "' went beyond the maximum area size (z: " + (frame.getFirstZ() + frame.getHeight()) + " > height: " + this.type.getHeight());
        }
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

        return new InventoryPageImpl(this.name,
                this.type,
                itemIconsActive,
                pagedIconsActive,
                this.inventoryCreationHandler,
                this.inventoryReopenHandler,
                this.background,
                player,
                session,
                this);
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
                        } else {
                            return handler.onVisible(lii.getCurrentPage(), lii.getMaxPage());
                        }
                    } else if (ScrollIconHandlerFactory.this.type == PagedItems.ScrollType.PREVIOUSLY) {
                        if (lii.getCurrentPage() <= 1) {
                            return handler.onHide(lii.getCurrentPage(), lii.getMaxPage());
                        } else {
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
            } catch (Exception e) {
                sound = Sound.CLICK;
            }
            return sound;
        }
    }

}
