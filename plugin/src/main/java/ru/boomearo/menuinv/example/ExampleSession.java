package ru.boomearo.menuinv.example;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.session.InventorySessionImpl;

import java.util.ArrayList;
import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class ExampleSession extends InventorySessionImpl {
    List<ItemStack> items = new ArrayList<>();
}
