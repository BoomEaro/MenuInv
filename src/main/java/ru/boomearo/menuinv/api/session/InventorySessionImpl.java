package ru.boomearo.menuinv.api.session;

import ru.boomearo.menuinv.api.PluginPage;

public class InventorySessionImpl implements InventorySession{

    private PluginPage currentPage = null;
    private PluginPage lastPage = null;
    private boolean first = true;

    private ConfirmData confirmData = null;

    @Override
    public PluginPage getCurrentPage() {
        return this.currentPage;
    }

    @Override
    public PluginPage getLastPage() {
        return this.lastPage;
    }

    @Override
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

    @Override
    public ConfirmData getConfirmData() {
        return this.confirmData;
    }

    @Override
    public void setConfirmData(ConfirmData confirmData) {
        this.confirmData = confirmData;
    }
}
