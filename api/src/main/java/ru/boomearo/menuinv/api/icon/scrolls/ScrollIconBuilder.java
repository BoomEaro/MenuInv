package ru.boomearo.menuinv.api.icon.scrolls;

import lombok.NonNull;
import ru.boomearo.menuinv.api.DefaultUpdateDelay;
import ru.boomearo.menuinv.api.Delayable;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.icon.*;

public class ScrollIconBuilder implements ElementBuilderUpdatable<ScrollIconBuilder> {

    private ScrollType scrollType = ScrollType.NEXT;
    private String name = "";

    private ScrollUpdate scrollVisibleUpdate = (inventoryPage, player, scrollType, currentPage, maxPage) -> null;
    private ScrollUpdate scrollHideUpdate = (inventoryPage, player, scrollType, currentPage, maxPage) -> null;

    private IconClick iconClick = (inventoryPage, icon, player, clickType) -> {
    };
    private IconClickDelay iconClickDelay = new DefaultIconClickDelay();
    private Delayable<InventoryPage> updateDelay = new DefaultUpdateDelay<>();

    @NonNull
    public ScrollIconBuilder setScrollType(@NonNull ScrollType scrollType) {
        this.scrollType = scrollType;
        return this;
    }

    @NonNull
    public ScrollIconBuilder setName(@NonNull String name) {
        this.name = name;
        return this;
    }

    @NonNull
    public ScrollIconBuilder setIconClick(@NonNull IconClick iconClick) {
        this.iconClick = iconClick;
        return this;
    }

    @NonNull
    public ScrollIconBuilder setScrollVisibleUpdate(@NonNull ScrollUpdate scrollUpdate) {
        this.scrollVisibleUpdate = scrollUpdate;
        return this;
    }

    @NonNull
    public ScrollIconBuilder setScrollHideUpdate(@NonNull ScrollUpdate scrollUpdate) {
        this.scrollHideUpdate = scrollUpdate;
        return this;
    }

    @NonNull
    @Override
    public ScrollIconBuilder setUpdateDelay(@NonNull Delayable<InventoryPage> updateDelay) {
        this.updateDelay = updateDelay;
        return this;
    }

    @NonNull
    public ScrollIconBuilder setIconClickDelay(@NonNull IconClickDelay iconClickDelay) {
        this.iconClickDelay = iconClickDelay;
        return this;
    }

    @NonNull
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
