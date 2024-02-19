package ru.boomearo.menuinv.api.frames;

import lombok.Getter;
import ru.boomearo.menuinv.api.*;
import ru.boomearo.menuinv.api.frames.iteration.FrameIterationHandler;
import ru.boomearo.menuinv.api.icon.*;
import ru.boomearo.menuinv.api.icon.scrolls.ScrollType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class PagedIconsImpl extends FramedIcons implements PagedIcons {

    private long updateHandlerCooldown = 0;

    private int page = 1;

    private int maxPage = 1;

    private boolean changes = false;
    private boolean forceUpdate = false;

    private List<IconHandler> cachedHandlers = null;
    private long cacheTime = 0;

    public PagedIconsImpl(String name,
                          InventoryLocation first,
                          InventoryLocation second,
                          FramedIconsHandler iconsHandler,
                          FrameIterationHandler iterationHandler,
                          Delayable<InventoryPage> cacheHandler) {
        super(name, first, second, iconsHandler, iterationHandler, cacheHandler);
    }

    @Override
    public int getCurrentPage() {
        return this.page;
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    public boolean scrollPage(ScrollType type) {
        if (type == ScrollType.NEXT) {
            return nextPage();
        } else if (type == ScrollType.PREVIOUSLY) {
            return previouslyPage();
        }
        return false;
    }

    @Override
    public void resetChanges() {
        this.changes = false;
    }

    @Override
    public void forceUpdate() {
        this.forceUpdate = true;
    }

    private List<IconHandler> getHandlers(InventoryPageImpl page, UpdateExceptionHandler updateExceptionHandler) {
        List<IconHandler> handlers = null;
        try {
            handlers = this.iconsHandler.onUpdate(page, page.getPlayer());
        } catch (Exception e) {
            updateExceptionHandler.onException(page, page.getPlayer(), e);
        }

        if (handlers == null) {
            handlers = new ArrayList<>();
        }

        return handlers;
    }

    private List<IconHandler> getCachedHandlers(InventoryPageImpl page, boolean force, UpdateExceptionHandler updateExceptionHandler) {
        if (this.cachedHandlers == null || this.cacheHandler.canUpdate(page, force, this.cacheTime)) {
            this.cacheTime = System.currentTimeMillis();

            this.cachedHandlers = getHandlers(page, updateExceptionHandler);
        }

        return this.cachedHandlers;
    }

    public List<ItemIcon> updateActiveIcons(InventoryPageImpl page,
                                            ItemIconImpl[] activeIcons,
                                            boolean force,
                                            boolean create,
                                            UpdateExceptionHandler updateExceptionHandler) {

        boolean updateForce = this.forceUpdate || force;

        try {
            if (this.iconsHandler.canUpdate(page, updateForce, this.updateHandlerCooldown) || create) {
                this.updateHandlerCooldown = System.currentTimeMillis();

                List<IconHandler> handlers = getCachedHandlers(page, updateForce, updateExceptionHandler);

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

                List<ItemIcon> updatedIcons = new ArrayList<>();
                for (int z = iterationHandler.startPositionZ(getHeight()); iterationHandler.hasNextZ(z, getHeight()); z = iterationHandler.manipulateZ(z)) {
                    for (int x = iterationHandler.startPositionX(getWidth()); iterationHandler.hasNextX(x, getWidth()); x = iterationHandler.manipulateX(x)) {

                        int slotOffset;
                        if (iterationHandler.isReverse()) {
                            slotOffset = this.first.getZ() * type.getWidth() + this.first.getX() + z + (x * type.getWidth());
                        } else {
                            slotOffset = this.first.getZ() * type.getWidth() + this.first.getX() + x + (z * type.getWidth());
                        }

                        if (i > (maxSize - 1)) {
                            updatedIcons.add(setItemIcon(activeIcons, slotOffset, DummyIconHandler.INSTANCE));
                        } else {
                            updatedIcons.add(setItemIcon(activeIcons, slotOffset, handlers.get(i)));
                        }

                        i++;
                    }
                }

                return updatedIcons;
            }
        }
        finally {
            this.forceUpdate = false;
        }

        return null;
    }

    private ItemIconImpl setItemIcon(ItemIconImpl[] activeIcons, int slot, IconHandler iconHandler) {
        ItemIconImpl current = activeIcons[slot];
        if (current != null) {
            current.setIconHandler(iconHandler);
            return current;
        }

        ItemIconImpl newItemIcon = new ItemIconImpl(slot, iconHandler);

        activeIcons[slot] = newItemIcon;

        return newItemIcon;
    }

}
