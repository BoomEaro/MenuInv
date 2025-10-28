package ru.boomearo.menuinv.api.frames;

import lombok.NonNull;
import org.bukkit.entity.Player;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.icon.IconHandler;

import javax.annotation.Nullable;
import java.util.List;

@FunctionalInterface
public interface PagedIconsUpdate {

    @Nullable
    List<IconHandler> onUpdate(@NonNull InventoryPage inventoryPage, @NonNull Player player);

}
