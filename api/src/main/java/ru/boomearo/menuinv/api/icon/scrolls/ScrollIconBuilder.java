package ru.boomearo.menuinv.api.icon.scrolls;

import com.google.common.base.Preconditions;
import ru.boomearo.menuinv.api.DefaultUpdateDelay;
import ru.boomearo.menuinv.api.Delayable;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.icon.*;

public class ScrollIconBuilder implements ElementBuilderUpdatable<ScrollIconBuilder> {

    private ScrollType scrollType = ScrollType.NEXT;
    private String name = "";

    private ScrollUpdate scrollVisibleUpdate = (inventoryPage, player, scrollType, currentPage, maxPage) -> null;
    private ScrollUpdate scrollHideUpdate = (inventoryPage, player, scrollType, currentPage, maxPage) -> null;

    private IconClick iconClick = (inventoryPage, icon, player, clickType) -> {};
    private IconClickDelay iconClickDelay = new DefaultIconClickDelay();
    private Delayable<InventoryPage> updateDelay = new DefaultUpdateDelay<>();

    public ScrollIconBuilder setScrollType(ScrollType scrollType) {
        Preconditions.checkArgument(scrollType != null, "scrollType is null!");
        this.scrollType = scrollType;
        return this;
    }

    public ScrollIconBuilder setName(String name) {
        Preconditions.checkArgument(name != null, "name is null!");
        this.name = name;
        return this;
    }

    public ScrollIconBuilder setIconClick(IconClick iconClick) {
        Preconditions.checkArgument(iconClick != null, "iconClick is null!");
        this.iconClick = iconClick;
        return this;
    }

    public ScrollIconBuilder setScrollVisibleUpdate(ScrollUpdate scrollUpdate) {
        Preconditions.checkArgument(scrollUpdate != null, "scrollUpdate is null!");
        this.scrollVisibleUpdate = scrollUpdate;
        return this;
    }

    public ScrollIconBuilder setScrollHideUpdate(ScrollUpdate scrollUpdate) {
        Preconditions.checkArgument(scrollUpdate != null, "scrollUpdate is null!");
        this.scrollHideUpdate = scrollUpdate;
        return this;
    }

    @Override
    public ScrollIconBuilder setUpdateDelay(Delayable<InventoryPage> updateDelay) {
        Preconditions.checkArgument(updateDelay != null, "updateDelay is null!");
        this.updateDelay = updateDelay;
        return this;
    }

    public ScrollIconBuilder setIconClickDelay(IconClickDelay iconClickDelay) {
        Preconditions.checkArgument(iconClickDelay != null, "iconClickDelay is null!");
        this.iconClickDelay = iconClickDelay;
        return this;
    }

    @Override
    public IconHandlerFactory build() {
        return () -> new ScrollIconHandler(
                this.name,
                this.scrollType,
                this.iconClick,
                this.iconClickDelay,
                this.updateDelay,
                this.scrollHideUpdate,
                this.scrollVisibleUpdate
        );
    }
}
