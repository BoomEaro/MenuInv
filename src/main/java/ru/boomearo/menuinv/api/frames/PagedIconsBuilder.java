package ru.boomearo.menuinv.api.frames;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.frames.iteration.DefaultIterationHandler;
import ru.boomearo.menuinv.api.frames.iteration.FrameIterationHandler;
import ru.boomearo.menuinv.api.icon.IconHandler;
import ru.boomearo.menuinv.api.icon.IconUpdateDelay;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PagedIconsBuilder {

    private PagedIconsUpdate pagedIconsUpdate = (inventoryPage, player) -> new ArrayList<>();
    private IconUpdateDelay iconUpdateDelay = (inventoryPage) -> 250;
    private Predicate<InventoryPage> iconUpdateCondition = (inventoryPage) -> true;
    private FrameIterationHandler frameIterationHandler = new DefaultIterationHandler();
    private boolean permanent = false;

    public PagedIconsBuilder setPagedItemsUpdate(PagedIconsUpdate pagedIconsUpdate) {
        Preconditions.checkArgument(pagedIconsUpdate != null, "pagedItemsUpdate is null!");
        this.pagedIconsUpdate = pagedIconsUpdate;
        return this;
    }

    public PagedIconsBuilder setIconUpdateDelay(IconUpdateDelay iconUpdateDelay) {
        Preconditions.checkArgument(iconUpdateDelay != null, "iconUpdateDelay is null!");
        this.iconUpdateDelay = iconUpdateDelay;
        return this;
    }

    public PagedIconsBuilder setIconUpdateCondition(Predicate<InventoryPage> iconUpdateCondition) {
        Preconditions.checkArgument(iconUpdateCondition != null, "iconUpdateCondition is null!");
        this.iconUpdateCondition = iconUpdateCondition;
        return this;
    }

    public PagedIconsBuilder setFrameIterationHandler(FrameIterationHandler frameIterationHandler) {
        Preconditions.checkArgument(frameIterationHandler != null, "frameIterationHandler is null!");
        this.frameIterationHandler = frameIterationHandler;
        return this;
    }

    public PagedIconsBuilder setPermanent(boolean permanent) {
        this.permanent = permanent;
        return this;
    }

    public PagedIconsBuilder permanent() {
        this.permanent = true;
        return this;
    }

    public FrameIterationHandler getFrameIterationHandler() {
        return this.frameIterationHandler;
    }

    public boolean isPermanent() {
        return this.permanent;
    }

    public FramedIconsHandlerFactory build() {
        return new FramedIconsHandlerFactory() {

            @Override
            public FramedIconsHandler create() {
                return new FramedIconsHandler() {

                    @Override
                    public List<IconHandler> onUpdate(InventoryPage inventoryPage, Player player) {
                        return PagedIconsBuilder.this.pagedIconsUpdate.onUpdate(inventoryPage, player);
                    }

                    @Override
                    public long getUpdateTime(InventoryPage inventoryPage) {
                        return PagedIconsBuilder.this.iconUpdateDelay.getUpdateTime(inventoryPage);
                    }

                    @Override
                    public boolean shouldUpdate(InventoryPage inventoryPage) {
                        return PagedIconsBuilder.this.iconUpdateCondition.test(inventoryPage);
                    }

                };
            }
        };
    }

}
