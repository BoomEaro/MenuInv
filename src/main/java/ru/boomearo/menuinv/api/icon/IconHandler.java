package ru.boomearo.menuinv.api.icon;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.Updatable;

import java.util.HashMap;
import java.util.Map;

/**
 * Представляет абстрактный обработчик предмета
 */
public abstract class IconHandler implements Updatable<ItemStack, InventoryPage>, Comparable<IconHandler> {

    private static final Map<String, Long> CLICK_COOLDOWN = new HashMap<>();

    /**
     * Обрабатывает нажатия на предмет
     *
     * @param page   Страница инвентаря
     * @param player Игрок нажавший на предмет
     * @param click  Тип клика
     */
    public abstract void onClick(InventoryPage page, Player player, ClickType click);

    /**
     * @return Задержку между выполнением метода onClick. По умолчанию 250 мс
     */
    public long getClickTime(InventoryPage page, Player player, ClickType click) {
        return 250;
    }

    /**
     * Обрабатывает нажатие на предмет, учитывая задержку и реализацию текущего класса
     *
     * @param page   Страница инвентаря
     * @param player Игрок нажавший на предмет
     * @param click  Тип клика
     */
    public void handleClick(InventoryPage page, Player player, ClickType click) {
        if (hasClicked(player.getName(), getClickTime(page, player, click))) {
            CLICK_COOLDOWN.put(player.getName(), System.currentTimeMillis());

            onClick(page, player, click);
        }
    }

    private static boolean hasClicked(String name, long cd) {
        Long value = CLICK_COOLDOWN.get(name);
        if (value == null) {
            return true;
        }

        if ((System.currentTimeMillis() - value) > cd) {
            CLICK_COOLDOWN.remove(name);
            return true;
        }

        return false;

    }

    @Override
    public int compareTo(IconHandler other) {
        return 1;
    }

}