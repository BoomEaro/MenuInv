package ru.boomearo.menuinv.api.frames;

import ru.boomearo.menuinv.api.icon.scrolls.ScrollType;
import ru.boomearo.menuinv.api.session.InventorySession;

public interface PagedIcons<SESSION extends InventorySession> {

    int getCurrentPage();

    boolean setCurrentPage(int page);

    boolean nextPage();

    boolean previouslyPage();

    boolean scrollPage(ScrollType type);

    int getMaxPage();

    boolean isChanges();

    void resetChanges();
}
