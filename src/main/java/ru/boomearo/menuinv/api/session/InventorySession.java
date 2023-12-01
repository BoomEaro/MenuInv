package ru.boomearo.menuinv.api.session;

import ru.boomearo.menuinv.api.PluginPage;

public interface InventorySession {

    PluginPage<?> getCurrentPage();

    PluginPage<?> getLastPage();

    void setCurrentPage(PluginPage<?> page);

    ConfirmData getConfirmData();

    void setConfirmData(ConfirmData confirmData);

}
