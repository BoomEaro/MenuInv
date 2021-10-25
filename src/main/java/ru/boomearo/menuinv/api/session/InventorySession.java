package ru.boomearo.menuinv.api.session;

import ru.boomearo.menuinv.api.PageData;

/**
 * Абстрактная сессия инвентаря, используемая для хранения внутренней информацией для наследника внутри меню
 */
public abstract class InventorySession {

    private PageData currentPage = null;
    private PageData lastPage = null;
    private boolean first = true;

    private ConfirmData confirmData = null;

    public PageData getCurrentPage() {
        return this.currentPage;
    }

    public PageData getLastPage() {
        return this.lastPage;
    }

    public void setCurrentPage(PageData page) {
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
