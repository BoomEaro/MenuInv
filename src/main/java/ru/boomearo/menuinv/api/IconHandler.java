package ru.boomearo.menuinv.api;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Представляет обработчик нажатий на кнопку
 */
public abstract class IconHandler implements Updatable<ItemStack, InventoryPage> {

    private static final Map<String, Long> clickCd = new HashMap<>();

    /**
     * Обработчик нажатия на кнопку. Для точного определения типа нажатия, используйте аргумент type
     */
    public abstract void onClick(InventoryPage page, Player player, ClickType click);

    /**
     * @return задержка между выполнением метода click. По умолчанию 250 мс
     */
    public long getClickTime() {
        return 250;
    }

    public void handleClick(InventoryPage page, Player player, ClickType type) {
        if (hasClicked(player.getName(), getClickTime())) {
            clickCd.put(player.getName(), System.currentTimeMillis());

            try {
                onClick(page, player, type);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
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
