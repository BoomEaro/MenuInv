package ru.boomearo.menuinv.api.frames;

import ru.boomearo.menuinv.api.icon.scrolls.ScrollType;

public interface PagedIcons {

    int getCurrentPage();

    int getMaxPage();

    boolean setCurrentPage(int page);

    boolean nextPage();

    boolean previouslyPage();

    boolean scrollPage(ScrollType type);

    void resetChanges();

    void forceUpdate();
}

