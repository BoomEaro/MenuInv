package ru.boomearo.menuinv.api.icon;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.DefaultUpdateDelay;
import ru.boomearo.menuinv.api.Delayable;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.session.InventorySession;

public class IconBuilder<SESSION extends InventorySession> implements ElementBuilderUpdatable<IconBuilder<SESSION>, SESSION> {

    private IconClick<SESSION> iconClick = (inventoryPage, player, clickType) -> {};
    private IconClickDelay<SESSION> iconClickDelay = new DefaultIconClickDelay<>();
    private IconUpdate<SESSION> iconUpdate = (inventoryPage, player) -> null;
    private Delayable<InventoryPage<SESSION>> updateDelay = new DefaultUpdateDelay<>();

    public IconBuilder<SESSION> setIconClick(IconClick<SESSION> iconClick) {
        Preconditions.checkArgument(iconClick != null, "iconClick is null!");
        this.iconClick = iconClick;
        return this;
    }

    public IconBuilder<SESSION> setIconUpdate(IconUpdate<SESSION> iconUpdate) {
        Preconditions.checkArgument(iconUpdate != null, "iconUpdate is null!");
        this.iconUpdate = iconUpdate;
        return this;
    }

    @Override
    public IconBuilder<SESSION> setUpdateDelay(Delayable<InventoryPage<SESSION>> updateDelay) {
        Preconditions.checkArgument(updateDelay != null, "updateDelay is null!");
        this.updateDelay = updateDelay;
        return this;
    }

    public IconBuilder<SESSION> setIconClickDelay(IconClickDelay<SESSION> iconClickDelay) {
        Preconditions.checkArgument(iconClickDelay != null, "iconClickDelay is null!");
        this.iconClickDelay = iconClickDelay;
        return this;
    }

    @Override
    public IconHandlerFactory<SESSION> build() {
        return () -> new IconHandler<SESSION>() {

            @Override
            public void onClick(InventoryPage<SESSION> page, Player player, ClickType click) {
                IconBuilder.this.iconClick.onClick(page, player, click);
            }

            @Override
            public long getClickTime(InventoryPage<SESSION> page, Player player, ClickType click) {
                return IconBuilder.this.iconClickDelay.getClickTime(page, player, click);
            }

            @Override
            public ItemStack onUpdate(InventoryPage<SESSION> consume, Player player) {
                return IconBuilder.this.iconUpdate.onUpdate(consume, player);
            }

            @Override
            public long onUpdateTime(InventoryPage<SESSION> page, boolean force) {
                return IconBuilder.this.updateDelay.onUpdateTime(page, force);
            }

        };
    }

}
