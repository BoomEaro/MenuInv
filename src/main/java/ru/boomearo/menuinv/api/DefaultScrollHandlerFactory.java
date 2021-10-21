package ru.boomearo.menuinv.api;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DefaultScrollHandlerFactory implements ScrollHandlerFactory {

    private final boolean next;

    public DefaultScrollHandlerFactory(boolean next) {
        this.next = next;
    }

    @Override
    public ScrollHandler create() {

        return new ScrollHandler() {

            @Override
            public ItemStack onVisible(int currentPage, int maxPage) {
                int nextPage = (DefaultScrollHandlerFactory.this.next ? (currentPage + 1) : (currentPage - 1));

                int amount = nextPage ;
                if (amount <= 0) {
                    amount = 1;
                }
                if (amount > 64) {
                    amount = 64;
                }
                ItemStack item = new ItemStack(Material.PAPER, amount);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(DefaultScrollHandlerFactory.this.next ? "§6Вперед " + nextPage + "/" + maxPage : "§6Назад " + nextPage + "/" + maxPage);
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
