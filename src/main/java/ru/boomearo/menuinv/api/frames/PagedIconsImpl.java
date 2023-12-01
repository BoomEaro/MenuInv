package ru.boomearo.menuinv.api.frames;

import lombok.Getter;
import ru.boomearo.menuinv.api.InventoryLocation;
import ru.boomearo.menuinv.api.MenuType;
import ru.boomearo.menuinv.api.frames.iteration.FrameIterationHandler;
import ru.boomearo.menuinv.api.InventoryPageImpl;
import ru.boomearo.menuinv.api.icon.DummyIconHandler;
import ru.boomearo.menuinv.api.icon.IconHandler;
import ru.boomearo.menuinv.api.icon.ItemIcon;
import ru.boomearo.menuinv.api.icon.UpdateExceptionHandler;
import ru.boomearo.menuinv.api.icon.scrolls.ScrollType;
import ru.boomearo.menuinv.api.session.InventorySession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class PagedIconsImpl<SESSION extends InventorySession> extends FramedIcons<SESSION> implements PagedIcons<SESSION> {

    private long updateHandlerCooldown = 0;

    private int currentPage = 1;

    private int maxPage = 1;

    private boolean changes = false;

    private List<IconHandler<SESSION>> cachedHandler = null;

    public PagedIconsImpl(String name,
                          InventoryLocation first,
                          InventoryLocation second,
                          FramedIconsHandler<SESSION> iconsHandler,
                          FrameIterationHandler iterationHandler,
                          boolean permanentCached) {
        super(name, first, second, iconsHandler, iterationHandler, permanentCached);
    }

    @Override
    public boolean setCurrentPage(int page) {
        int oldPage = this.currentPage;

        int nextPage = page;

        if (nextPage > this.maxPage) {
            nextPage = this.maxPage;
        }
        if (nextPage <= 0) {
            nextPage = 1;
        }

        this.currentPage = nextPage;

        boolean change = (this.currentPage != oldPage);
        if (change) {
            this.changes = true;
        }

        return change;
    }

    @Override
    public boolean nextPage() {
        int oldPage = this.currentPage;

        int newPage = this.currentPage + 1;

        if (newPage > this.maxPage) {
            newPage = this.maxPage;
        }
        if (newPage <= 0) {
            newPage = 1;
        }

        this.currentPage = newPage;

        boolean change = (this.currentPage != oldPage);
        if (change) {
            this.changes = true;
        }

        return change;
    }

    @Override
    public boolean previouslyPage() {
        int oldPage = this.currentPage;

        int newPage = this.currentPage - 1;

        if (newPage <= 0) {
            newPage = 1;
        }
        this.currentPage = newPage;

        boolean change = (this.currentPage != oldPage);
        if (change) {
            this.changes = true;
        }

        return change;
    }

    @Override
    public boolean scrollPage(ScrollType type) {
        if (type == ScrollType.NEXT) {
            return nextPage();
        }
        else if (type == ScrollType.PREVIOUSLY) {
            return previouslyPage();
        }
        return false;
    }

    @Override
    public void resetChanges() {
        this.changes = false;
    }

    private void setMaxPage(int maxPage) {
        int oldMaxPage = this.maxPage;

        int newMaxPage = maxPage;
        if (newMaxPage <= 0) {
            newMaxPage = 1;
        }

        this.maxPage = newMaxPage;

        if (this.maxPage != oldMaxPage) {
            this.changes = true;
        }
    }

    private List<IconHandler<SESSION>> getHandlers(InventoryPageImpl<SESSION> page, UpdateExceptionHandler<SESSION> updateExceptionHandler) {
        List<IconHandler<SESSION>> handlers = null;
        try {
            handlers = this.iconsHandler.onUpdate(page, page.getPlayer());
        }
        catch (Exception e) {
            updateExceptionHandler.onException(page, page.getPlayer(), e);
        }

        if (handlers == null) {
            handlers = new ArrayList<>();
        }

        return handlers;
    }

    private List<IconHandler<SESSION>> getCachedHandler(InventoryPageImpl<SESSION> page, UpdateExceptionHandler<SESSION> updateExceptionHandler) {
        if (this.permanentCached) {
            if (this.cachedHandler != null) {
                return this.cachedHandler;
            }

            this.cachedHandler = getHandlers(page, updateExceptionHandler);
            return this.cachedHandler;
        }

        return getHandlers(page, updateExceptionHandler);
    }

    public void updateActiveIcons(InventoryPageImpl<SESSION> page,
                                  ItemIcon<SESSION>[] activeIcons,
                                  boolean force,
                                  boolean create,
                                  UpdateExceptionHandler<SESSION> updateExceptionHandler) {

        if (this.iconsHandler.canUpdate(page, force, this.updateHandlerCooldown) || create) {
            this.updateHandlerCooldown = System.currentTimeMillis();

            List<IconHandler<SESSION>> handlers = getCachedHandler(page, updateExceptionHandler);

            Collections.sort(handlers);

            int maxSize = handlers.size();

            MenuType type = page.getMenuType();

            int pageLimit = (getWidth() * getHeight());

            setMaxPage(maxSize / pageLimit + (maxSize % pageLimit > 0 ? 1 : 0));

            setCurrentPage(this.currentPage);

            int pageOffset = (this.currentPage - 1) * pageLimit;
            if (pageOffset > maxSize) {
                pageOffset = maxSize;
            }

            int i = pageOffset;

            FrameIterationHandler iterationHandler = this.iterationHandler;

            for (int z = iterationHandler.startPositionZ(getHeight()); iterationHandler.hasNextZ(z, getHeight()); z = iterationHandler.manipulateZ(z)) {
                for (int x = iterationHandler.startPositionX(getWidth()); iterationHandler.hasNextX(x, getWidth()); x = iterationHandler.manipulateX(x)) {

                    int slotOffset;
                    if (iterationHandler.isReverse()) {
                        slotOffset = this.first.getZ() * type.getWidth() + this.first.getX() + z + (x * type.getWidth());
                    }
                    else {
                        slotOffset = this.first.getZ() * type.getWidth() + this.first.getX() + x + (z * type.getWidth());
                    }

                    if (i > (maxSize - 1)) {
                        setItemIcon(activeIcons, slotOffset, new DummyIconHandler<>());
                    }
                    else {
                        setItemIcon(activeIcons, slotOffset, handlers.get(i));
                    }

                    i++;
                }
            }
        }
    }

    private void setItemIcon(ItemIcon<SESSION>[] activeIcons, int slot, IconHandler<SESSION> iconHandler) {
        ItemIcon<SESSION> current = activeIcons[slot];
        if (current != null) {
            current.setHandler(iconHandler);
            return;
        }

        activeIcons[slot] = new ItemIcon<>(slot, iconHandler);
    }
}
