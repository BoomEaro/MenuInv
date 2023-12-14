package ru.boomearo.menuinv.api;

import com.google.common.base.Preconditions;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import ru.boomearo.menuinv.api.frames.Frame;
import ru.boomearo.menuinv.api.frames.PagedIcons;
import ru.boomearo.menuinv.api.frames.PagedIconsBuilder;
import ru.boomearo.menuinv.api.icon.*;
import ru.boomearo.menuinv.api.frames.FramedIconsTemplate;
import ru.boomearo.menuinv.api.session.InventorySession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class TemplatePageImpl implements TemplatePage {

    private final String name;
    private final PluginTemplatePagesImpl pluginTemplatePages;

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
    private Delayable<InventoryPage> globalUpdateDelay = new DefaultUpdateDelay();

    private BottomInventoryClickHandler bottomInventoryClickHandler = (inventoryPage, player, slot, clickType) -> true;

    private StructureHolder[] structure = null;

    private final Map<Integer, ItemIconTemplate> itemIcons = new HashMap<>();
    private final Map<String, FramedIconsTemplate> pagedItems = new HashMap<>();
    private IconHandlerFactory background = null;

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
    public Delayable<InventoryPage> getGlobalUpdateDelay() {
        return this.globalUpdateDelay;
    }

    @Override
    public TemplatePage setGlobalUpdateDelay(Delayable<InventoryPage> globalUpdateDelay) {
        Preconditions.checkArgument(globalUpdateDelay != null, "globalUpdateDelay is null!");
        this.globalUpdateDelay = globalUpdateDelay;

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
    public TemplatePage setBottomInventoryClickHandler(BottomInventoryClickHandler bottomInventoryClickHandler) {
        Preconditions.checkArgument(bottomInventoryClickHandler != null, "bottomInventoryClickHandler is null!");

        this.bottomInventoryClickHandler = bottomInventoryClickHandler;
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
            ElementBuilderUpdatable<?> elementBuilderUpdatable = (ElementBuilderUpdatable<?>) elementBuilder;
            elementBuilderUpdatable.setUpdateDelay((inventoryPage, force) -> Long.MAX_VALUE);
        }
        return setIcon(slot, elementBuilder);
    }

    @Override
    public TemplatePage setPagedIcons(String name, InventoryLocation first, int width, int height, PagedIconsBuilder pagedIconsBuilder) {
        Preconditions.checkArgument(name != null, "name is null!");
        Preconditions.checkArgument(first != null, "first is null!");
        Preconditions.checkArgument(pagedIconsBuilder != null, "pagedItemsBuilder is null!");

        FramedIconsTemplate tli = new FramedIconsTemplate(name, first, width, height, pagedIconsBuilder.build(), pagedIconsBuilder.getFrameIterationHandler(), pagedIconsBuilder.getCacheHandler());

        checkPagedItemsBorder(tli);

        this.pagedItems.put(name, tli);

        return this;
    }

    @Override
    public TemplatePage setPagedIcons(String name, InventoryLocation first, InventoryLocation second, PagedIconsBuilder pagedIconsBuilder) {
        Preconditions.checkArgument(name != null, "name is null!");
        Preconditions.checkArgument(first != null, "first is null!");
        Preconditions.checkArgument(second != null, "second is null!");
        Preconditions.checkArgument(pagedIconsBuilder != null, "pagedItemsBuilder is null!");

        FramedIconsTemplate tli = new FramedIconsTemplate(name, first, second, pagedIconsBuilder.build(), pagedIconsBuilder.getFrameIterationHandler(), pagedIconsBuilder.getCacheHandler());

        checkPagedItemsBorder(tli);

        this.pagedItems.put(name, tli);

        return this;
    }

    @Override
    public TemplatePage setPagedIconsIngredients(String name, char first, char second, PagedIconsBuilder pagedIconsBuilder) {
        Preconditions.checkArgument(this.structure != null, "structure is not set!");

        InventoryLocation firstLocation = InventoryLocation.of(0, 0);
        InventoryLocation secondLocation = InventoryLocation.of(0, 0);
        for (StructureHolder holder : this.structure) {
            if (holder.getValue() == first) {
                firstLocation = InventoryLocation.of(holder.getX(), holder.getZ());
            }
            if (holder.getValue() == second) {
                secondLocation = InventoryLocation.of(holder.getX() + 1, holder.getZ() + 1);
            }
        }

        return setPagedIcons(name, firstLocation, secondLocation, pagedIconsBuilder);
    }

    @Override
    public TemplatePage setImmutablePagedIcons(String name, InventoryLocation first, int width, int height, PagedIconsBuilder pagedIconsBuilder) {
        pagedIconsBuilder.setUpdateDelay((inventoryPage, force) -> Long.MAX_VALUE);
        pagedIconsBuilder.setCacheHandler(page -> true);

        return setPagedIcons(name, first, width, height, pagedIconsBuilder);
    }

    @Override
    public TemplatePage setImmutablePagedIcons(String name, InventoryLocation first, InventoryLocation second, PagedIconsBuilder pagedIconsBuilder) {
        pagedIconsBuilder.setUpdateDelay((inventoryPage, force) -> Long.MAX_VALUE);
        pagedIconsBuilder.setCacheHandler(page -> true);

        return setPagedIcons(name, first, second, pagedIconsBuilder);
    }

    @Override
    public TemplatePage setImmutablePagedIconsIngredients(String name, char first, char second, PagedIconsBuilder pagedIconsBuilder) {
        pagedIconsBuilder.setUpdateDelay((inventoryPage, force) -> Long.MAX_VALUE);
        pagedIconsBuilder.setCacheHandler(page -> true);

        return setPagedIconsIngredients(name, first, second, pagedIconsBuilder);
    }

    private void checkPagedItemsBorder(Frame frame) {
        if (frame.getFirst().getX() < 0 || frame.getFirst().getZ() < 0) {
            throw new IllegalStateException("Paged items with name '" + frame.getName() + "' went out of the area having a negative value of coordinates. (x: " + frame.getFirst().getX() + " z: " + frame.getFirst().getZ() + ")");
        }

        if (frame.getFirst().getX() + frame.getWidth() > this.menuType.getWidth()) {
            throw new IllegalStateException("Paged items with name '" + frame.getName() + "' went beyond the maximum area size (x: " + (frame.getFirst().getX() + frame.getWidth()) + " > width: " + this.menuType.getWidth() + ")");
        }

        if (frame.getFirst().getZ() + frame.getHeight() > this.menuType.getHeight()) {
            throw new IllegalStateException("Paged items with name '" + frame.getName() + "' went beyond the maximum area size (z: " + (frame.getFirst().getZ() + frame.getHeight()) + " > height: " + this.menuType.getHeight() + ")");
        }
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
            ElementBuilderUpdatable<?> elementBuilderUpdatable = (ElementBuilderUpdatable<?>) elementBuilder;
            elementBuilderUpdatable.setUpdateDelay((inventoryPage, force) -> Long.MAX_VALUE);
        }

        return setBackground(elementBuilder);
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

            int x = i % width;
            int z = i / width;

            this.structure[i] = new StructureHolder(i, x, z, c);
        }

        return this;
    }

    @Override
    public TemplatePage setStructure(List<String> value) {
        return setStructure(value.toArray(new String[0]));
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
            ElementBuilderUpdatable<?> elementBuilderUpdatable = (ElementBuilderUpdatable<?>) elementBuilder;
            elementBuilderUpdatable.setUpdateDelay((inventoryPage, force) -> Long.MAX_VALUE);
        }

        return setIngredient(value, elementBuilder);
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
            pagedIconsActive.put(tli.getName(), new PagedIcons(tli.getName(), tli.getFirst(), tli.getSecond(), tli.getIconsFactory().create(), tli.getIterationHandler(), tli.getCacheHandler()));
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
                this.globalUpdateDelay,
                this.bottomInventoryClickHandler,
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

    @Data
    @RequiredArgsConstructor
    private static class StructureHolder {
        private final int slot;
        private final int x;
        private final int z;
        private final char value;

        private ElementBuilder elementBuilder;

    }
}
