package ru.boomearo.menuinv.api.session;

import ru.boomearo.menuinv.api.PageData;
import ru.boomearo.menuinv.api.PluginPage;

/**
 * Сессия инвентаря, используемая для хранения внутренней информацией.
 * Может быть расширена путем наследования для расширения функциональности меню.
 */
public class InventorySession {

    private PluginPage currentPage = null;
    private PluginPage lastPage = null;
    private boolean first = true;

    private ConfirmData confirmData = null;

    public PluginPage getCurrentPage() {
        return this.currentPage;
    }

    public PluginPage getLastPage() {
        return this.lastPage;
    }

    public void setCurrentPage(PluginPage page) {
        if (this.first) {
            this.currentPage = page;
            this.lastPage = page;

            this.first = false;
            return;
        }

        this.lastPage = this.currentPage;

        this.currentPage = page;
    }

    public ConfirmData getConfirmData() {
        return this.confirmData;
    }

    public void setConfirmData(ConfirmData confirmData) {
        this.confirmData = confirmData;
    }
}
