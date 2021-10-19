package ru.boomearo.menuinv.api;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.MenuInv;
import ru.boomearo.menuinv.objects.InventoryPage;

import java.util.HashMap;
import java.util.Map;

/**
 * Представляет обычную кнопку или нажимаемый объект
 */
public abstract class AbstractButtonHandler implements IUpdateable<ItemStack, InventoryPage> {

    private static final Map<String, Long> clickCd = new HashMap<>();

    /**
     * Обработчик нажатия на кнопку. Для точного определения типа нажатия, используйте аргумент type
     */
    public abstract void click(InventoryPage page, Player player, ClickType type);

    /**
     * @return задержка между выполнением метода click. По умолчанию 250 мс
     */
    public long getClickTime() {
        return 250;
    }

    public void handleClick(InventoryPage page, Player player, ClickType type) {
        if (hasClicked(player.getName(), getClickTime())) {
            clickCd.put(player.getName(), System.currentTimeMillis());

            click(page, player, type);
        }
    }

    private static boolean hasClicked(String name, long cd) {
        Long value = clickCd.get(name);
        if (value == null) {
            return true;
        }

        if ((System.currentTimeMillis() - value) > cd) {
            clickCd.remove(name);
            return true;
        }

        return false;

    }

}
