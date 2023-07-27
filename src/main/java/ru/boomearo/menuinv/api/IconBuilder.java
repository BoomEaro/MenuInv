package ru.boomearo.menuinv.api;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class IconBuilder {

    private IconClick iconClick = (inventoryPage, player, clickType) -> {};
    private IconClickDelay iconClickDelay = (inventoryPage, player, clickType) -> 250;
    private IconUpdate iconUpdate = (inventoryPage, player) -> null;
    private IconUpdateDelay iconUpdateDelay = (inventoryPage) -> 250;
    private IconUpdateCondition iconUpdateCondition = (inventoryPage) -> true;

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

    public IconBuilder setIconUpdateDelay(IconUpdateDelay iconUpdateDelay) {
        Preconditions.checkArgument(iconUpdateDelay != null, "iconUpdateDelay is null!");
        this.iconUpdateDelay = iconUpdateDelay;
        return this;
    }

    public IconBuilder setIconUpdateCondition(IconUpdateCondition iconUpdateCondition) {
        Preconditions.checkArgument(iconUpdateCondition != null, "iconUpdateCondition is null!");
        this.iconUpdateCondition = iconUpdateCondition;
        return this;
    }

    public IconBuilder setIconClickDelay(IconClickDelay iconClickDelay) {
        Preconditions.checkArgument(iconClickDelay != null, "iconClickDelay is null!");
        this.iconClickDelay = iconClickDelay;
        return this;
    }

    public IconHandlerFactory build() {
        return () -> {
            return new IconHandler() {

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
                public long getUpdateTime(InventoryPage page) {
                    return IconBuilder.this.iconUpdateDelay.getUpdateTime(page);
                }

                @Override
                public boolean shouldUpdate(InventoryPage page) {
                    return IconBuilder.this.iconUpdateCondition.shouldUpdate(page);
                }
            };
        };
    }

}
