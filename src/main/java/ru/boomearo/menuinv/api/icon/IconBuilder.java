package ru.boomearo.menuinv.api.icon;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.DefaultUpdateDelay;
import ru.boomearo.menuinv.api.Delayable;
import ru.boomearo.menuinv.api.InventoryPage;

import java.time.Duration;

public class IconBuilder implements ElementBuilderUpdatable<IconBuilder> {

    private IconClick iconClick = (inventoryPage, player, clickType) -> {};
    private IconClickDelay iconClickDelay = new DefaultIconClickDelay();
    private IconUpdate iconUpdate = (inventoryPage, player) -> null;
    private Delayable<InventoryPage> updateDelay = new DefaultUpdateDelay();

    public IconBuilder setIconClick(IconClick iconClick) {
        Preconditions.checkArgument(iconClick != null, "iconClick is null!");
        this.iconClick = iconClick;
        return this;
    }

    public IconBuilder setIconUpdate(IconUpdate iconUpdate) {
        Preconditions.checkArgument(iconUpdate != null, "iconUpdate is null!");
        this.iconUpdate = iconUpdate;
        return this;
    }

    @Override
    public IconBuilder setUpdateDelay(Delayable<InventoryPage> updateDelay) {
        Preconditions.checkArgument(updateDelay != null, "updateDelay is null!");
        this.updateDelay = updateDelay;
        return this;
    }

    public IconBuilder setIconClickDelay(IconClickDelay iconClickDelay) {
        Preconditions.checkArgument(iconClickDelay != null, "iconClickDelay is null!");
        this.iconClickDelay = iconClickDelay;
        return this;
    }

    @Override
    public IconHandlerFactory build() {
        return () -> new IconHandler() {

            @Override
            public void onClick(InventoryPage page, Player player, ClickType click) {
                IconBuilder.this.iconClick.onClick(page, player, click);
            }

            @Override
            public long getClickTime(InventoryPage page, Player player, ClickType click) {
                return IconBuilder.this.iconClickDelay.getClickTime(page, player, click);
            }

            @Override
            public ItemStack onUpdate(InventoryPage consume, Player player) {
                return IconBuilder.this.iconUpdate.onUpdate(consume, player);
            }

            @Override
            public Duration onUpdateTime(InventoryPage page, boolean force) {
                return IconBuilder.this.updateDelay.onUpdateTime(page, force);
            }

        };
    }

}
