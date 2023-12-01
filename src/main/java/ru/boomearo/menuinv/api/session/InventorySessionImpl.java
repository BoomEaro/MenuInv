package ru.boomearo.menuinv.api.session;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.boomearo.menuinv.api.PluginPage;

@Getter
@Setter
@NoArgsConstructor
public class InventorySessionImpl implements InventorySession {

    private PluginPage<?> currentPage = null;
    private PluginPage<?> lastPage = null;
    private boolean first = true;

    private ConfirmData confirmData = null;

    @Override
    public void setCurrentPage(PluginPage<?> page) {
        if (this.first) {
            this.currentPage = page;
            this.lastPage = page;

            this.first = false;
            return;
        }

        this.lastPage = this.currentPage;

        this.currentPage = page;
    }

}
