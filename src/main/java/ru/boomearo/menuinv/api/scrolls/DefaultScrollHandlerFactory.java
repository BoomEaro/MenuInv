package ru.boomearo.menuinv.api.scrolls;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ru.boomearo.menuinv.api.frames.inventory.PagedItems;

public class DefaultScrollHandlerFactory implements ScrollHandlerFactory {

    private final PagedItems.ScrollType type;

    public DefaultScrollHandlerFactory(PagedItems.ScrollType type) {
        this.type = type;
    }

    @Override
    public ScrollHandler create() {

        return new ScrollHandler() {

            @Override
            public ItemStack onVisible(int currentPage, int maxPage) {
                int nextPage = type.getNextPage(currentPage);

                int amount = nextPage ;
                if (amount <= 0) {
                    amount = 1;
                }
                if (amount > 64) {
                    amount = 64;
                }
                ItemStack item = new ItemStack(Material.PAPER, amount);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("§6" + type.getName() + " §7[§c" + nextPage + "§7/§c" + maxPage + "§7]");
                meta.addItemFlags(ItemFlag.values());
                item.setItemMeta(meta);
                return item;
            }

            @Override
            public ItemStack onHide(int currentPage, int maxPage) {
                return null;
            }

        };
    }
}
