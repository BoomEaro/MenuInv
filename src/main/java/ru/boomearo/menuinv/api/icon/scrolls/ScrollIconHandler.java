package ru.boomearo.menuinv.api.icon.scrolls;

import lombok.Getter;
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
    public void onClick(InventoryPage page, ItemIcon icon, Player player, ClickType clickType) {
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

    @Override
    public ItemStack onUpdate(InventoryPage page, Player player) {
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

    @Override
    public Duration getClickTime(InventoryPage page, Player player, ClickType click) {
        return this.iconClickDelay.getClickTime(page, player, click);
    }

    @Override
    public Duration onUpdateTime(InventoryPage page, boolean force) {
        return this.updateDelay.onUpdateTime(page, force);
    }

}
