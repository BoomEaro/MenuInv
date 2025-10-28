package ru.boomearo.menuinv.api;

import com.google.common.base.Preconditions;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.boomearo.menuinv.api.frames.*;
import ru.boomearo.menuinv.api.icon.*;
import ru.boomearo.menuinv.api.session.InventorySession;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@RequiredArgsConstructor
@Getter
public class TemplatePageImpl implements TemplatePage {

    private final Plugin plugin;
    private final String name;
    private final PluginTemplatePagesImpl pluginTemplatePages;

    private MenuType menuType = MenuType.CHEST_9X6;

    private ComponentInventoryTitleHandler componentInventoryTitleHandler = (inventoryPage) -> Component.text("Default page");
    private InventoryTitleHandler inventoryTitleHandler = (inventoryPage) -> "Default page";

    private InventoryReopenHandler inventoryReopenHandler = (inventoryPage, force) -> false;
    private ClickExceptionHandler clickExceptionHandler = (inventoryPage, player, clickType, exception) -> {
        inventoryPage.close(true);
        inventoryPage.getTemplatePage().getPluginTemplatePages().getPlugin().getLogger().log(Level.SEVERE,
                "Exception on icon click. Player: " + player.getName() + ". ClickType: " + clickType + ". Closing menu now.", exception);
    };
    private UpdateExceptionHandler updateExceptionHandler = (inventoryPage, player, exception) -> {
        inventoryPage.close(true);
        inventoryPage.getTemplatePage().getPluginTemplatePages().getPlugin().getLogger().log(Level.SEVERE,
                "Exception on icon update. Player: " + player.getName() + ". Closing menu now.", exception);
    };

    private InventoryCloseHandler inventoryCloseHandler = (inventoryPage, player) -> {
    };
    private Delayable<InventoryPage> globalUpdateDelay = new DefaultUpdateDelay<>();

    private BottomInventoryClickHandler bottomInventoryClickHandler = (inventoryPage, player, slot, clickType) -> true;

    private StructureHolder[] structure = null;

    private final Map<Integer, ItemIconTemplate> itemIcons = new HashMap<>();
    private final Map<String, FramedIconsTemplate> pagedItems = new HashMap<>();
    private IconHandlerFactory background = null;

    @NonNull
    @Override
    public MenuType getMenuType() {
        return this.menuType;
    }

    @NonNull
    @Override
    public TemplatePage setMenuType(@NonNull MenuType type) {
        this.menuType = type;
        return this;
    }

    @Nullable
    @Override
    public InventoryTitleHandler getInventoryTitle() {
        return this.inventoryTitleHandler;
    }

    @NonNull
    @Override
    public TemplatePage setInventoryTitle(@NonNull InventoryTitleHandler inventoryTitleHandler) {
        this.inventoryTitleHandler = inventoryTitleHandler;
        this.componentInventoryTitleHandler = null;
        return this;
    }

    @Nullable
    @Override
    public ComponentInventoryTitleHandler getComponentInventoryTitle() {
        return this.componentInventoryTitleHandler;
    }

    @NonNull
    @Override
    public TemplatePage setComponentInventoryTitle(@NonNull ComponentInventoryTitleHandler componentInventoryTitleHandler) {
        this.componentInventoryTitleHandler = componentInventoryTitleHandler;
        this.inventoryTitleHandler = null;
        return this;
    }

    @NonNull
    @Override
    public InventoryReopenHandler getInventoryReopen() {
        return this.inventoryReopenHandler;
    }

    @NonNull
    @Override
    public TemplatePage setInventoryReopen(@NonNull InventoryReopenHandler inventoryReopenHandler) {
        this.inventoryReopenHandler = inventoryReopenHandler;
        return this;
    }

    @NonNull
    @Override
    public UpdateExceptionHandler getUpdateExceptionHandler() {
        return this.updateExceptionHandler;
    }

    @Override
    public TemplatePage setUpdateExceptionHandler(@NonNull UpdateExceptionHandler updateExceptionHandler) {
        this.updateExceptionHandler = updateExceptionHandler;
        return this;
    }

    @NonNull
    @Override
    public InventoryCloseHandler getInventoryCloseHandler() {
        return this.inventoryCloseHandler;
    }

    @NonNull
    @Override
    public TemplatePage setInventoryCloseHandler(@NonNull InventoryCloseHandler inventoryCloseHandler) {
        this.inventoryCloseHandler = inventoryCloseHandler;
        return this;
    }

    @NonNull
    @Override
    public Delayable<InventoryPage> getGlobalUpdateDelay() {
        return this.globalUpdateDelay;
    }

    @NonNull
    @Override
    public TemplatePage setGlobalUpdateDelay(@NonNull Delayable<InventoryPage> globalUpdateDelay) {
        this.globalUpdateDelay = globalUpdateDelay;
        return this;
    }

    @NonNull
    @Override
    public ClickExceptionHandler getClickExceptionHandler() {
        return this.clickExceptionHandler;
    }

    @NonNull
    @Override
    public TemplatePage setClickExceptionHandler(@NonNull ClickExceptionHandler clickExceptionHandler) {
        this.clickExceptionHandler = clickExceptionHandler;
        return this;
    }

    @NonNull
    @Override
    public TemplatePage setBottomInventoryClickHandler(@NonNull BottomInventoryClickHandler bottomInventoryClickHandler) {
        this.bottomInventoryClickHandler = bottomInventoryClickHandler;
        return this;
    }

    @NonNull
    @Override
    public TemplatePage setIcon(int slot, @NonNull ElementBuilder elementBuilder) {
        ItemIconTemplate tmp = this.itemIcons.get(slot);
        if (tmp != null) {
            throw new IllegalStateException("Button on slot '" + slot + "' already added!");
        }

        addItem(new ItemIconTemplate(slot, elementBuilder.build()));
        return this;
    }

    @NonNull
    @Override
    public TemplatePage setImmutableIcon(int slot, @NonNull ElementBuilder elementBuilder) {
        if (elementBuilder instanceof ElementBuilderUpdatable<?> elementBuilderUpdatable) {
            elementBuilderUpdatable.setUpdateDelay(new InfinityUpdateDelay<>(true));
        }
        return setIcon(slot, elementBuilder);
    }

    @NonNull
    @Override
    public TemplatePage setPagedIcons(@NonNull String name, @NonNull InventoryLocation first, int width, int height, @NonNull PagedElementBuilder pagedIconsBuilder) {
        FramedIconsTemplate tli = new FramedIconsTemplate(name, first, width, height, pagedIconsBuilder.build(), pagedIconsBuilder.getFrameIterationHandler(), pagedIconsBuilder.getCacheHandler());

        checkPagedItemsBorder(tli);

        this.pagedItems.put(name, tli);

        return this;
    }

    @NonNull
    @Override
    public TemplatePage setPagedIcons(@NonNull String name, @NonNull InventoryLocation first, @NonNull InventoryLocation second, @NonNull PagedElementBuilder pagedIconsBuilder) {
        FramedIconsTemplate tli = new FramedIconsTemplate(name, first, second, pagedIconsBuilder.build(), pagedIconsBuilder.getFrameIterationHandler(), pagedIconsBuilder.getCacheHandler());

        checkPagedItemsBorder(tli);

        this.pagedItems.put(name, tli);

        return this;
    }

    @NonNull
    @Override
    public TemplatePage setPagedIconsIngredients(@NonNull String name, char first, char second, @NonNull PagedElementBuilder pagedIconsBuilder) {
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

    @NonNull
    @Override
    public TemplatePage setImmutablePagedIcons(@NonNull String name, @NonNull InventoryLocation first, int width, int height, @NonNull PagedElementBuilder pagedIconsBuilder) {
        if (pagedIconsBuilder instanceof PagedElementBuilderUpdatable<?> elementBuilderUpdatable) {
            elementBuilderUpdatable.setUpdateDelay(new InfinityUpdateDelay<>(true));
        }

        pagedIconsBuilder.setCacheHandler(new InfinityUpdateDelay<>(true));

        return setPagedIcons(name, first, width, height, pagedIconsBuilder);
    }

    @NonNull
    @Override
    public TemplatePage setImmutablePagedIcons(@NonNull String name, @NonNull InventoryLocation first, @NonNull InventoryLocation second, @NonNull PagedElementBuilder pagedIconsBuilder) {
        if (pagedIconsBuilder instanceof PagedElementBuilderUpdatable<?> elementBuilderUpdatable) {
            elementBuilderUpdatable.setUpdateDelay(new InfinityUpdateDelay<>(true));
        }

        pagedIconsBuilder.setCacheHandler(new InfinityUpdateDelay<>(true));

        return setPagedIcons(name, first, second, pagedIconsBuilder);
    }

    @NonNull
    @Override
    public TemplatePage setImmutablePagedIconsIngredients(@NonNull String name, char first, char second, @NonNull PagedElementBuilder pagedIconsBuilder) {
        if (pagedIconsBuilder instanceof PagedElementBuilderUpdatable<?> elementBuilderUpdatable) {
            elementBuilderUpdatable.setUpdateDelay(new InfinityUpdateDelay<>(true));
        }

        pagedIconsBuilder.setCacheHandler(new InfinityUpdateDelay<>(true));

        return setPagedIconsIngredients(name, first, second, pagedIconsBuilder);
    }

    private void checkPagedItemsBorder(Frame frame) {
        if (frame.getFirst().x() < 0 || frame.getFirst().z() < 0) {
            throw new IllegalStateException("Paged items with name '" + frame.getName() + "' went out of the area having a negative value of coordinates. (x: " + frame.getFirst().x() + " z: " + frame.getFirst().z() + ")");
        }

        if (frame.getFirst().x() + frame.getWidth() > this.menuType.getWidth()) {
            throw new IllegalStateException("Paged items with name '" + frame.getName() + "' went beyond the maximum area size (x: " + (frame.getFirst().x() + frame.getWidth()) + " > width: " + this.menuType.getWidth() + ")");
        }

        if (frame.getFirst().z() + frame.getHeight() > this.menuType.getHeight()) {
            throw new IllegalStateException("Paged items with name '" + frame.getName() + "' went beyond the maximum area size (z: " + (frame.getFirst().z() + frame.getHeight()) + " > height: " + this.menuType.getHeight() + ")");
        }
    }

    @NonNull
    @Override
    public TemplatePage setBackground(@NonNull ElementBuilder elementBuilder) {
        this.background = elementBuilder.build();

        return this;
    }

    @NonNull
    @Override
    public TemplatePage setImmutableBackground(@NonNull ElementBuilder elementBuilder) {
        if (elementBuilder instanceof ElementBuilderUpdatable<?> elementBuilderUpdatable) {
            elementBuilderUpdatable.setUpdateDelay(new InfinityUpdateDelay<>(true));
        }

        return setBackground(elementBuilder);
    }

    @NonNull
    @Override
    public TemplatePage setStructure(@NonNull String... value) {
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

    @NonNull
    @Override
    public TemplatePage setStructure(@NonNull List<String> value) {
        return setStructure(value.toArray(new String[0]));
    }

    @NonNull
    @Override
    public TemplatePage setIngredient(char value, @NonNull ElementBuilder elementBuilder) {
        for (StructureHolder holder : this.structure) {
            if (holder.getValue() == value) {
                holder.setElementBuilder(elementBuilder);
            }
        }
        return this;
    }

    @NonNull
    @Override
    public TemplatePage setImmutableIngredient(char value, @NonNull ElementBuilder elementBuilder) {
        if (elementBuilder instanceof ElementBuilderUpdatable<?> elementBuilderUpdatable) {
            elementBuilderUpdatable.setUpdateDelay(new InfinityUpdateDelay<>(true));
        }

        return setIngredient(value, elementBuilder);
    }

    private void addItem(@NonNull ItemIconTemplate icon) {
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

    @NonNull
    public InventoryPageImpl createNewInventoryPage(@NonNull Player player, @NonNull InventorySession session) {
        Map<Integer, ItemIconImpl> itemIconsActive = new HashMap<>();

        if (this.structure != null) {
            for (StructureHolder holder : this.structure) {
                ElementBuilder elementBuilder = holder.getElementBuilder();
                if (elementBuilder != null) {
                    itemIconsActive.put(holder.getSlot(), new ItemIconImpl(holder.getSlot(), elementBuilder.build().create()));
                }
            }
        }

        for (ItemIconTemplate tii : this.itemIcons.values()) {
            itemIconsActive.put(tii.getSlot(), new ItemIconImpl(tii.getSlot(), tii.getFactory().create()));
        }

        Map<String, PagedIconsImpl> pagedIconsActive = new HashMap<>();
        for (FramedIconsTemplate tli : this.pagedItems.values()) {
            pagedIconsActive.put(tli.getName(), new PagedIconsImpl(tli.getName(), tli.getFirst(), tli.getSecond(), tli.getIconsFactory().create(), tli.getIterationHandler(), tli.getCacheHandler()));
        }

        return new InventoryPageImpl(
                this.plugin,
                this.name,
                this.menuType,
                itemIconsActive,
                pagedIconsActive,
                this.inventoryTitleHandler,
                this.componentInventoryTitleHandler,
                this.inventoryReopenHandler,
                this.clickExceptionHandler,
                this.updateExceptionHandler,
                this.inventoryCloseHandler,
                this.globalUpdateDelay,
                this.bottomInventoryClickHandler,
                this.background,
                player,
                session,
                this
        );
    }

    @NonNull
    private static String[] removeEmptyChars(@NonNull String[] value) {
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
