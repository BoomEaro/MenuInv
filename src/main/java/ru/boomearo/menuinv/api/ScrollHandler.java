package ru.boomearo.menuinv.api;

import org.bukkit.inventory.ItemStack;

public interface ScrollHandler {

    public ItemStack onVisible(int currentPage, int maxPage);

    public ItemStack onHide(int currentPage, int maxPage);

}
