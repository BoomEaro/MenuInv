package ru.boomearo.menuinv.api.icon;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.DefaultUpdateDelay;
import ru.boomearo.menuinv.api.Delayable;
import ru.boomearo.menuinv.api.InventoryPage;

import java.time.Duration;

public class IconBuilder implements ElementBuilderUpdatable<IconBuilder> {

    private IconClick iconClick = (inventoryPage, icon, player, clickType) -> {
    };
    private IconClickDelay iconClickDelay = new DefaultIconClickDelay();
    private IconUpdate iconUpdate = (inventoryPage, player) -> null;
    private Delayable<InventoryPage> updateDelay = new DefaultUpdateDelay<>();

    @NonNull
    public IconBuilder setIconClick(@NonNull IconClick iconClick) {
        this.iconClick = iconClick;
        return this;
    }

    @NonNull
    public IconBuilder setIconUpdate(@NonNull IconUpdate iconUpdate) {
        this.iconUpdate = iconUpdate;
        return this;
    }

    @NonNull
    @Override
    public IconBuilder setUpdateDelay(@NonNull Delayable<InventoryPage> updateDelay) {
        this.updateDelay = updateDelay;
        return this;
    }

    @NonNull
    public IconBuilder setIconClickDelay(@NonNull IconClickDelay iconClickDelay) {
        this.iconClickDelay = iconClickDelay;
        return this;
    }

    @NonNull
    @Override
    public IconHandlerFactory build() {
        return () -> new IconHandler() {

            @Override
            public void onClick(@NonNull InventoryPage page, @NonNull ItemIcon icon, @NonNull Player player, @NonNull ClickType click) {
                IconBuilder.this.iconClick.onClick(page, icon, player, click);
            }

            @Override
            public Duration getClickTime(@NonNull InventoryPage page, @NonNull Player player, @NonNull ClickType click) {
                return IconBuilder.this.iconClickDelay.getClickTime(page, player, click);
            }

            @Override
            public ItemStack onUpdate(@NonNull InventoryPage consume, @NonNull Player player) {
                return IconBuilder.this.iconUpdate.onUpdate(consume, player);
            }

            @Override
            public Duration onUpdateTime(@NonNull InventoryPage page, boolean force) {
                return IconBuilder.this.updateDelay.onUpdateTime(page, force);
            }

        };
    }

}
