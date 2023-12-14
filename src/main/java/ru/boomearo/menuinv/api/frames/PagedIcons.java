package ru.boomearo.menuinv.api.frames;

import lombok.Getter;
import ru.boomearo.menuinv.api.InventoryLocation;
import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.MenuType;
import ru.boomearo.menuinv.api.frames.iteration.FrameIterationHandler;
import ru.boomearo.menuinv.api.InventoryPageImpl;
import ru.boomearo.menuinv.api.icon.DummyIconHandler;
import ru.boomearo.menuinv.api.icon.IconHandler;
import ru.boomearo.menuinv.api.icon.ItemIcon;
import ru.boomearo.menuinv.api.icon.UpdateExceptionHandler;
import ru.boomearo.menuinv.api.icon.scrolls.ScrollType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Getter
public class PagedIcons extends FramedIcons {

    private long updateHandlerCooldown = 0;

    private int page = 1;

    private int maxPage = 1;

    private boolean changes = false;

    private List<IconHandler> cachedHandler = null;

    public PagedIcons(String name,
                      InventoryLocation first,
                      InventoryLocation second,
                      FramedIconsHandler iconsHandler,
                      FrameIterationHandler iterationHandler,
                      Predicate<InventoryPage> cacheHandler) {
        super(name, first, second, iconsHandler, iterationHandler, cacheHandler);
    }

    public int getCurrentPage() {
        return this.page;
    }

    public boolean setCurrentPage(int page) {
        int oldPage = this.page;

        int nextPage = page;

        if (nextPage > this.maxPage) {
            nextPage = this.maxPage;
        }
        if (nextPage <= 0) {
            nextPage = 1;
        }

        this.page = nextPage;

        boolean change = (this.page != oldPage);
        if (change) {
            this.changes = true;
        }

        return change;
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

    public boolean nextPage() {
        int oldPage = this.page;

        int newPage = this.page + 1;

        if (newPage > this.maxPage) {
            newPage = this.maxPage;
        }
        if (newPage <= 0) {
            newPage = 1;
        }

        this.page = newPage;

        boolean change = (this.page != oldPage);
        if (change) {
            this.changes = true;
        }

        return change;
    }

    public boolean previouslyPage() {
        int oldPage = this.page;

        int newPage = this.page - 1;

        if (newPage <= 0) {
            newPage = 1;
        }
        this.page = newPage;

        boolean change = (this.page != oldPage);
        if (change) {
            this.changes = true;
        }

        return change;
    }

    public boolean scrollPage(ScrollType type) {
        if (type == ScrollType.NEXT) {
            return nextPage();
        }
        else if (type == ScrollType.PREVIOUSLY) {
            return previouslyPage();
        }
        return false;
    }

    private List<IconHandler> getHandlers(InventoryPageImpl page, UpdateExceptionHandler updateExceptionHandler) {
        List<IconHandler> handlers = null;
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

    private List<IconHandler> getCachedHandler(InventoryPageImpl page, UpdateExceptionHandler updateExceptionHandler) {
        if (this.cacheHandler.test(page)) {
            if (this.cachedHandler != null) {
                return this.cachedHandler;
            }

            this.cachedHandler = getHandlers(page, updateExceptionHandler);
            return this.cachedHandler;
        }

        return getHandlers(page, updateExceptionHandler);
    }

    public void updateActiveIcons(InventoryPageImpl page,
                                  ItemIcon[] activeIcons,
                                  boolean force,
                                  boolean create,
                                  UpdateExceptionHandler updateExceptionHandler) {

        if (this.iconsHandler.canUpdate(page, force, this.updateHandlerCooldown) || create) {
            this.updateHandlerCooldown = System.currentTimeMillis();

            List<IconHandler> handlers = getCachedHandler(page, updateExceptionHandler);

            Collections.sort(handlers);

            int maxSize = handlers.size();

            MenuType type = page.getMenuType();

            int pageLimit = (getWidth() * getHeight());

            setMaxPage(maxSize / pageLimit + (maxSize % pageLimit > 0 ? 1 : 0));

            setCurrentPage(this.page);

            int pageOffset = (this.page - 1) * pageLimit;
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
                        setItemIcon(activeIcons, slotOffset, DummyIconHandler.INSTANCE);
                    }
                    else {
                        setItemIcon(activeIcons, slotOffset, handlers.get(i));
                    }

                    i++;
                }
            }
        }
    }

    private void setItemIcon(ItemIcon[] activeIcons, int slot, IconHandler iconHandler) {
        ItemIcon current = activeIcons[slot];
        if (current != null) {
            current.setHandler(iconHandler);
            return;
        }

        activeIcons[slot] = new ItemIcon(slot, iconHandler);
    }

    public void resetChanges() {
        this.changes = false;
    }

}
