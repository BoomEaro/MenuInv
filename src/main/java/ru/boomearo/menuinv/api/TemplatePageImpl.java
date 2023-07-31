package ru.boomearo.menuinv.api;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;

import ru.boomearo.menuinv.api.frames.Frame;
import ru.boomearo.menuinv.api.frames.PagedIcons;
import ru.boomearo.menuinv.api.frames.PagedIconsBuilder;
import ru.boomearo.menuinv.api.icon.*;
import ru.boomearo.menuinv.api.frames.FramedIconsTemplate;
import ru.boomearo.menuinv.api.session.InventorySession;

import java.util.HashMap;
import java.util.Map;

public class TemplatePageImpl implements TemplatePage {

    private final String name;
    private MenuType menuType = MenuType.CHEST_9X6;
    private InventoryTitleHandler inventoryTitleHandler = (inventoryPage) -> "Default page";
    private InventoryReopenHandler inventoryReopenHandler = (inventoryPage, force) -> false;
    private ClickExceptionHandler clickExceptionHandler = (inventoryPage, player, clickType, exception) -> {
        inventoryPage.close(true);
        exception.printStackTrace();
    };
    private UpdateExceptionHandler updateExceptionHandler = (inventoryPage, player, exception) -> {
        inventoryPage.close(true);
        exception.printStackTrace();
    };

    private InventoryCloseHandler inventoryCloseHandler = (inventoryPage, player) -> {};

    private StructureHolder[] structure = null;

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
    public InventoryCloseHandler getInventoryCloseHandler() {
        return this.inventoryCloseHandler;
    }

    @Override
    public TemplatePage setInventoryCloseHandler(InventoryCloseHandler inventoryCloseHandler) {
        Preconditions.checkArgument(inventoryCloseHandler != null, "inventoryCloseHandler is null!");
        this.inventoryCloseHandler = inventoryCloseHandler;

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
    public TemplatePage setIcon(int slot, ElementBuilder elementBuilder) {
        Preconditions.checkArgument(elementBuilder != null, "elementBuilder is null!");

        ItemIconTemplate tmp = this.itemIcons.get(slot);
        if (tmp != null) {
            throw new IllegalStateException("Button on slot '" + slot + "' already added!");
        }

        addItem(new ItemIconTemplate(slot, elementBuilder.build()));
        return this;
    }

    @Override
    public TemplatePage setImmutableIcon(int slot, ElementBuilder elementBuilder) {
        if (elementBuilder instanceof ElementBuilderUpdatable) {
            ElementBuilderUpdatable elementBuilderUpdatable = (ElementBuilderUpdatable) elementBuilder;
            elementBuilderUpdatable.setIconUpdateCondition((inventoryPage) -> false);
            elementBuilderUpdatable.setIconUpdateDelay((inventoryPage) -> Long.MAX_VALUE);
        }
        setIcon(slot, elementBuilder);
        return this;
    }


    @Override
    public TemplatePage setPagedIcons(String name, int x, int z, int width, int height, PagedIconsBuilder pagedIconsBuilder) {
        Preconditions.checkArgument(name != null, "name is null!");
        Preconditions.checkArgument(pagedIconsBuilder != null, "pagedItemsBuilder is null!");

        FramedIconsTemplate tli = new FramedIconsTemplate(name, x, z, width, height, pagedIconsBuilder.build(), pagedIconsBuilder.getFrameIterationHandler(), pagedIconsBuilder.isPermanent());

        checkPagedItemsBorder(tli);

        this.pagedItems.put(name, tli);

        return this;
    }

    private void checkPagedItemsBorder(Frame frame) {
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

    @Override
    public TemplatePage setImmutablePagedIcons(String name, int x, int z, int width, int height, PagedIconsBuilder pagedIconsBuilder) {
        pagedIconsBuilder.setIconUpdateCondition((inventoryPage) -> false);
        pagedIconsBuilder.setIconUpdateDelay((inventoryPage) -> Long.MAX_VALUE);
        pagedIconsBuilder.setPermanent(true);

        setPagedIcons(name, x, z, width, height, pagedIconsBuilder);
        return this;
    }

    @Override
    public TemplatePage setBackground(ElementBuilder elementBuilder) {
        Preconditions.checkArgument(elementBuilder != null, "elementBuilder is null!");

        this.background = elementBuilder.build();

        return this;
    }

    @Override
    public TemplatePage setImmutableBackground(ElementBuilder elementBuilder) {
        if (elementBuilder instanceof ElementBuilderUpdatable) {
            ElementBuilderUpdatable elementBuilderUpdatable = (ElementBuilderUpdatable) elementBuilder;
            elementBuilderUpdatable.setIconUpdateCondition((inventoryPage) -> false);
            elementBuilderUpdatable.setIconUpdateDelay((inventoryPage) -> Long.MAX_VALUE);
        }

        setBackground(elementBuilder);
        return this;
    }

    @Override
    public TemplatePage setStructure(String... value) {
        value = removeEmptyChars(value);

        int height = value.length;

        if (height > this.menuType.getHeight()) {
            throw new IllegalStateException("Structure height is more than " + this.menuType.getHeight());
        }

        int width = 0;
        StringBuilder sb = new StringBuilder();
        for (String data : value) {
            if (data.length() > this.menuType.getWidth()) {
                throw new IllegalStateException("Structure width is more than " + this.menuType.getWidth());
            }

            width = data.length();

            sb.append(data);
        }

        String readyValue = sb.toString();

        this.structure = new StructureHolder[width * height];
        for (int i = 0; i < this.structure.length; i++) {
            char c = readyValue.charAt(i);

            this.structure[i] = new StructureHolder(i, c);
        }

        return this;
    }

    @Override
    public TemplatePage setIngredient(char value, ElementBuilder elementBuilder) {
        for (StructureHolder holder : this.structure) {
            if (holder.getValue() == value) {
                holder.setElementBuilder(elementBuilder);
            }
        }
        return this;
    }

    @Override
    public TemplatePage setImmutableIngredient(char value, ElementBuilder elementBuilder) {
        if (elementBuilder instanceof ElementBuilderUpdatable) {
            ElementBuilderUpdatable elementBuilderUpdatable = (ElementBuilderUpdatable) elementBuilder;
            elementBuilderUpdatable.setIconUpdateCondition((inventoryPage) -> false);
            elementBuilderUpdatable.setIconUpdateDelay((inventoryPage) -> Long.MAX_VALUE);
        }

        setIngredient(value, elementBuilder);
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

    public PluginTemplatePagesImpl getPluginTemplatePages() {
        return this.pluginTemplatePages;
    }

    public InventoryPageImpl createNewInventoryPage(Player player, InventorySession session) {
        Map<Integer, ItemIcon> itemIconsActive = new HashMap<>();

        if (this.structure != null) {
            for (StructureHolder holder : this.structure) {
                ElementBuilder elementBuilder = holder.getElementBuilder();
                if (elementBuilder != null) {
                    itemIconsActive.put(holder.getSlot(), new ItemIcon(holder.getSlot(), elementBuilder.build().create()));
                }
            }
        }

        for (ItemIconTemplate tii : this.itemIcons.values()) {
            itemIconsActive.put(tii.getSlot(), new ItemIcon(tii.getSlot(), tii.getFactory().create()));
        }

        Map<String, PagedIcons> pagedIconsActive = new HashMap<>();
        for (FramedIconsTemplate tli : this.pagedItems.values()) {
            pagedIconsActive.put(tli.getName(), new PagedIcons(tli.getName(), tli.getFirstX(), tli.getFirstZ(), tli.getWidth(), tli.getHeight(), tli.getIconsFactory().create(), tli.getIterationHandler(), tli.isPermanentCached()));
        }

        return new InventoryPageImpl(this.name,
                this.menuType,
                itemIconsActive,
                pagedIconsActive,
                this.inventoryTitleHandler,
                this.inventoryReopenHandler,
                this.clickExceptionHandler,
                this.updateExceptionHandler,
                this.inventoryCloseHandler,
                this.background,
                player,
                session,
                this);
    }

    private static String[] removeEmptyChars(String[] value) {
        String[] newValue = new String[value.length];
        for (int i = 0; i < newValue.length; i++) {
            newValue[i] = value[i].replace(" ", "");
        }
        return newValue;
    }

    private static class StructureHolder {
        private final int slot;
        private final char value;
        private ElementBuilder elementBuilder;

        public StructureHolder(int slot, char value) {
            this.slot = slot;
            this.value = value;
        }

        public int getSlot() {
            return this.slot;
        }

        public char getValue() {
            return this.value;
        }

        public ElementBuilder getElementBuilder() {
            return this.elementBuilder;
        }

        public void setElementBuilder(ElementBuilder elementBuilder) {
            this.elementBuilder = elementBuilder;
        }
    }
}
