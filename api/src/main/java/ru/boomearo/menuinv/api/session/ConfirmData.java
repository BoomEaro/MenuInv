package ru.boomearo.menuinv.api.session;

import lombok.NonNull;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.InventoryPage;

import javax.annotation.Nullable;

public interface ConfirmData {

    void executeConfirm(@NonNull InventoryPage page);

    void executeCancel(@NonNull InventoryPage page);

    @Nullable
    ItemStack getConfirmItem(@NonNull InventoryPage page);

    @Nullable
    ItemStack getCancelItem(@NonNull InventoryPage page);

    @Nullable
    String getInventoryName(@NonNull InventorySession session);

}
