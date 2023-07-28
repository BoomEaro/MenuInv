package ru.boomearo.menuinv.api;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import ru.boomearo.menuinv.api.frames.Frame;
import ru.boomearo.menuinv.api.frames.inventory.PagedItems;
import ru.boomearo.menuinv.api.scrolls.ScrollIconBuilder;
import ru.boomearo.menuinv.api.scrolls.ScrollType;
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
    private MenuType menuType = MenuType.CHEST_9X6;
    private InventoryTitleHandler inventoryTitleHandler = (inventoryPage) -> "Default page";
    private InventoryReopenHandler inventoryReopenHandler = (inventoryPage, force) -> false;
    private ClickExceptionHandler clickExceptionHandler = (inventoryPage, player, clickType, exception) -> exception.printStackTrace();
    private UpdateExceptionHandler updateExceptionHandler = (inventoryPage, player, exception) -> exception.printStackTrace();

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
    public MenuType getMenuType() {
        return this.menuType;
    }

    @Override
    public TemplatePage setMenuType(MenuType type) {
        Preconditions.checkArgument(type != null, "type is null!");

        this.menuType = type;
        return this;
    }

    @Override
    public InventoryTitleHandler getInventoryTitle() {
        return this.inventoryTitleHandler;
    }

    @Override
    public TemplatePage setInventoryTitle(InventoryTitleHandler inventoryTitleHandler) {
        Preconditions.checkArgument(inventoryTitleHandler != null, "inventoryCreationHandler is null!");

        this.inventoryTitleHandler = inventoryTitleHandler;
        return this;
    }

    @Override
    public InventoryReopenHandler getInventoryReopen() {
        return this.inventoryReopenHandler;
    }

    @Override
    public TemplatePage setInventoryReopen(InventoryReopenHandler inventoryReopenHandler) {
        Preconditions.checkArgument(inventoryReopenHandler != null, "inventoryReopenHandler is null!");

        this.inventoryReopenHandler = inventoryReopenHandler;
        return this;
    }

    @Override
    public UpdateExceptionHandler getUpdateExceptionHandler() {
        return this.updateExceptionHandler;
    }

    @Override
    public TemplatePage setUpdateExceptionHandler(UpdateExceptionHandler updateExceptionHandler) {
        Preconditions.checkArgument(updateExceptionHandler != null, "updateExceptionHandler is null!");
        this.updateExceptionHandler = updateExceptionHandler;

        return this;
    }

    @Override
    public ClickExceptionHandler getClickExceptionHandler() {
        return this.clickExceptionHandler;
    }

    @Override
    public TemplatePage setClickExceptionHandler(ClickExceptionHandler clickExceptionHandler) {
        Preconditions.checkArgument(clickExceptionHandler != null, "clickExceptionHandle is null!");

        this.clickExceptionHandler = clickExceptionHandler;
        return this;
    }

    @Override
    public TemplatePage setItem(int slot, IconBuilder iconBuilder) {
        Preconditions.checkArgument(iconBuilder != null, "iconBuilder is null!");

        ItemIconTemplate tmp = this.itemIcons.get(slot);
        if (tmp != null) {
            throw new IllegalStateException("Button on slot '" + slot + "' already added!");
        }

        addItem(new ItemIconTemplate(slot, iconBuilder.build()));
        return this;
    }


    @Override
    public TemplatePage setPagedItems(String name, int x, int z, int width, int height, PagedItemsBuilder pagedItemsBuilder) {
        Preconditions.checkArgument(name != null, "name is null!");
        Preconditions.checkArgument(pagedItemsBuilder != null, "pagedItemsBuilder is null!");

        FramedIconsTemplate tmp = this.pagedItems.get(name);
        if (tmp != null) {
            throw new IllegalStateException("Paged items with name '" + name + "' already added!");
        }

        FramedIconsTemplate tli = new FramedIconsTemplate(name, x, z, width, height, pagedItemsBuilder.build(), pagedItemsBuilder.getFrameIterationHandler(), pagedItemsBuilder.isPermanent());

        checkBorder(tli);

        this.pagedItems.put(name, tli);

        return this;
    }

    @Override
    public TemplatePage setScrollItem(int slot, String pagedItems, ScrollType type, ScrollIconBuilder scrollIconBuilder) {
        Preconditions.checkArgument(pagedItems != null, "pagedItems is null!");
        Preconditions.checkArgument(scrollIconBuilder != null, "scrollIconBuilder is null!");

        ItemIconTemplate tmp = this.itemIcons.get(slot);
        if (tmp != null) {
            throw new IllegalStateException("Button on position '" + slot + "' already added!");
        }

        FramedIconsTemplate tli = this.pagedItems.get(pagedItems);
        if (tli == null) {
            throw new IllegalStateException("Paged items with name '" + pagedItems + "' is not found!");
        }

        addItem(new ItemIconTemplate(slot, new ScrollIconHandlerFactory(pagedItems, type, scrollIconBuilder.build())));

        return this;
    }

    @Override
    public TemplatePage setBackground(IconBuilder iconBuilder) {
        Preconditions.checkArgument(iconBuilder != null, "iconBuilder is null!");

        this.background = iconBuilder.build();

        return this;
    }

    private void addItem(ItemIconTemplate icon) {
        int slot = icon.getSlot();

        if (slot < 0) {
            throw new IllegalStateException("Button on slot '" + icon.getSlot() + "' is outside 0! (slot: " + slot + ")");
        }
        int maxSlot = this.menuType.getSize() - 1;
        if (slot > maxSlot) {
            throw new IllegalStateException("Button on slot '" + icon.getSlot() + "' is more than possible! (slot: " + slot + "/" + maxSlot + ")");
        }

        this.itemIcons.put(icon.getSlot(), icon);
    }

    private void checkBorder(Frame frame) {
        if (frame.getFirstX() < 0 || frame.getFirstZ() < 0) {
            throw new IllegalStateException("Paged items with name '" + frame.getName() + "' went out of the area having a negative value of coordinates. (x: " + frame.getFirstX() + " z: " + frame.getFirstZ());
        }

        if (frame.getFirstX() + frame.getWidth() > this.menuType.getWidth()) {
            throw new IllegalStateException("Paged items with name '" + frame.getName() + "' went beyond the maximum area size (x: " + (frame.getFirstX() + frame.getWidth()) + " > width: " + this.menuType.getWidth());
        }

        if (frame.getFirstZ() + frame.getHeight() > this.menuType.getHeight()) {
            throw new IllegalStateException("Paged items with name '" + frame.getName() + "' went beyond the maximum area size (z: " + (frame.getFirstZ() + frame.getHeight()) + " > height: " + this.menuType.getHeight());
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
                this.menuType,
                itemIconsActive,
                pagedIconsActive,
                this.inventoryTitleHandler,
                this.inventoryReopenHandler,
                this.clickExceptionHandler,
                this.updateExceptionHandler,
                this.background,
                player,
                session,
                this);
    }

    private static class ScrollIconHandlerFactory implements IconHandlerFactory {

        private final String pagedItems;
        private final ScrollType type;
        private final ScrollHandlerFactory scrollHandlerFactory;

        public ScrollIconHandlerFactory(String pagedItems, ScrollType type, ScrollHandlerFactory scrollHandlerFactory) {
            this.pagedItems = pagedItems;
            this.type = type;
            this.scrollHandlerFactory = scrollHandlerFactory;
        }

        @Override
        public IconHandler create() {
            ScrollHandler handler = this.scrollHandlerFactory.create(ScrollIconHandlerFactory.this.type);

            return new IconHandler() {

                @Override
                public void onClick(InventoryPage page, Player player, ClickType clickType) {
                    boolean change = page.getListedIconsItems(ScrollIconHandlerFactory.this.pagedItems).scrollPage(ScrollIconHandlerFactory.this.type);
                    if (change) {
                        page.setNeedUpdate();
                        handler.onClick(page, player, clickType);
                    }
                }

                @Override
                public ItemStack onUpdate(InventoryPage page, Player player) {
                    PagedItems lii = page.getListedIconsItems(ScrollIconHandlerFactory.this.pagedItems);

                    if (ScrollIconHandlerFactory.this.type == ScrollType.NEXT) {
                        if (lii.getCurrentPage() >= lii.getMaxPage()) {
                            return handler.onHide(page, player, ScrollIconHandlerFactory.this.type, lii.getCurrentPage(), lii.getMaxPage());
                        } else {
                            return handler.onVisible(page, player, ScrollIconHandlerFactory.this.type, lii.getCurrentPage(), lii.getMaxPage());
                        }
                    } else if (ScrollIconHandlerFactory.this.type == ScrollType.PREVIOUSLY) {
                        if (lii.getCurrentPage() <= 1) {
                            return handler.onHide(page, player, ScrollIconHandlerFactory.this.type, lii.getCurrentPage(), lii.getMaxPage());
                        } else {
                            return handler.onVisible(page, player, ScrollIconHandlerFactory.this.type, lii.getCurrentPage(), lii.getMaxPage());
                        }
                    }
                    return null;
                }

            };
        }
    }
}
