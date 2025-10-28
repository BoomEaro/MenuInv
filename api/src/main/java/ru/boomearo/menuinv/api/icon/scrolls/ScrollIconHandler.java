package ru.boomearo.menuinv.api.icon.scrolls;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.Delayable;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.frames.PagedIcons;
import ru.boomearo.menuinv.api.icon.IconClick;
import ru.boomearo.menuinv.api.icon.IconClickDelay;
import ru.boomearo.menuinv.api.icon.IconHandler;
import ru.boomearo.menuinv.api.icon.ItemIcon;

import javax.annotation.Nullable;
import java.time.Duration;

@RequiredArgsConstructor
@Getter
public class ScrollIconHandler extends IconHandler {

    private final String name;
    private final ScrollType scrollType;
    private final IconClick iconClick;
    private final IconClickDelay iconClickDelay;
    private final Delayable<InventoryPage> updateDelay;
    private final ScrollUpdate scrollHideUpdate;
    private final ScrollUpdate scrollVisibleUpdate;

    @Override
    public void onClick(@NonNull InventoryPage page, @NonNull ItemIcon icon, @NonNull Player player, @NonNull ClickType clickType) {
        PagedIcons pagedIcons = page.getListedIconsItems(this.name);
        if (pagedIcons == null) {
            return;
        }

        boolean change = pagedIcons.scrollPage(this.scrollType);
        if (change) {
            page.update(pagedIcons, true);
            page.updateScrolls(this.name, true);
            this.iconClick.onClick(page, icon, player, clickType);
        }
    }

    @Nullable
    @Override
    public ItemStack onUpdate(@NonNull InventoryPage page, @NonNull Player player) {
        PagedIcons pagedIcons = page.getListedIconsItems(this.name);
        if (pagedIcons == null) {
            return null;
        }

        if (this.scrollType == ScrollType.NEXT) {
            if (pagedIcons.getCurrentPage() >= pagedIcons.getMaxPage()) {
                return this.scrollHideUpdate.onUpdate(page, player, this.scrollType, pagedIcons.getCurrentPage(), pagedIcons.getMaxPage());
            } else {
                return this.scrollVisibleUpdate.onUpdate(page, player, this.scrollType, pagedIcons.getCurrentPage(), pagedIcons.getMaxPage());
            }
        } else if (this.scrollType == ScrollType.PREVIOUSLY) {
            if (pagedIcons.getCurrentPage() <= 1) {
                return this.scrollHideUpdate.onUpdate(page, player, this.scrollType, pagedIcons.getCurrentPage(), pagedIcons.getMaxPage());
            } else {
                return this.scrollVisibleUpdate.onUpdate(page, player, this.scrollType, pagedIcons.getCurrentPage(), pagedIcons.getMaxPage());
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Duration getClickTime(@NonNull InventoryPage page, @NonNull Player player, @NonNull ClickType click) {
        return this.iconClickDelay.getClickTime(page, player, click);
    }

    @Nullable
    @Override
    public Duration onUpdateTime(@NonNull InventoryPage page, boolean force) {
        return this.updateDelay.onUpdateTime(page, force);
    }

}
