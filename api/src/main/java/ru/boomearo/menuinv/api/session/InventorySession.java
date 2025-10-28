package ru.boomearo.menuinv.api.session;

import lombok.NonNull;
import ru.boomearo.menuinv.api.PluginPage;

import javax.annotation.Nullable;

public interface InventorySession {

    @NonNull
    PluginPage getCurrentPage();

    @NonNull
    PluginPage getLastPage();

    void setCurrentPage(@NonNull PluginPage page);

    @Nullable
    ConfirmData getConfirmData();

    void setConfirmData(@Nullable ConfirmData confirmData);

}
