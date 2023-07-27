package ru.boomearo.menuinv.api.frames.inventory;

import ru.boomearo.menuinv.api.IconHandler;
import ru.boomearo.menuinv.api.MenuType;
import ru.boomearo.menuinv.api.FramedIconsHandler;
import ru.boomearo.menuinv.api.frames.iteration.FrameIterationHandler;
import ru.boomearo.menuinv.api.InventoryPageImpl;
import ru.boomearo.menuinv.api.ItemIcon;
import ru.boomearo.menuinv.api.scrolls.ScrollType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Представляет страничную рамки, которую можно скроллить (перемещать вперед/назад)
 */
public class PagedItems extends FramedIcons {

    private long updateHandlerCooldown = 0;

    private int page = 1;

    private int maxPage = 1;

    private boolean changes = false;

    private List<IconHandler> cachedHandler = null;

    public PagedItems(String name, int x, int z, int width, int height, FramedIconsHandler iconsHandler, FrameIterationHandler iterationHandler, boolean permanentCached) {
        super(name, x, z, width, height, iconsHandler, iterationHandler, permanentCached);
    }

    /**
     * @return Текущую страницу
     */
    public int getCurrentPage() {
        return this.page;
    }

    /**
     * Устанавливает страницу
     *
     * @param page Номер страницы
     * @return true если удалось прокрутить страницу
     */
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

    /**
     * @return Максимальную страницу
     */
    public int getMaxPage() {
        return this.maxPage;
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

    /**
     * Прокручивает страницу вперед на 1 номер
     *
     * @return true если удалось прокрутить страницу
     */
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

    /**
     * Прокручивает страницу назад на 1 номер
     *
     * @return true если удалось прокрутить страницу
     */
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

    /**
     * Прокручивает страницу на 1 номер
     *
     * @param type Тип прокрутки
     * @return true если удалось прокрутить страницу
     */
    public boolean scrollPage(ScrollType type) {
        if (type == ScrollType.NEXT) {
            return nextPage();
        }
        else if (type == ScrollType.PREVIOUSLY) {
            return previouslyPage();
        }
        return false;
    }

    private List<IconHandler> getHandlers(InventoryPageImpl page) {
        List<IconHandler> handlers = null;
        try {
            handlers = getIconsHandler().onUpdate(page, page.getPlayer());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (handlers == null) {
            handlers = new ArrayList<>();
        }

        return handlers;
    }

    private List<IconHandler> getCachedHandler(InventoryPageImpl page) {
        if (isPermanentCached()) {
            if (this.cachedHandler != null) {
                return this.cachedHandler;
            }

            this.cachedHandler = getHandlers(page);
            return this.cachedHandler;
        }

        return getHandlers(page);
    }

    /**
     * Обновляет актуальное состояние страницы
     *
     * @param page  Страница инвентаря
     * @param force Игнорировать ли задержку обновления
     */
    public void updateActiveIcons(InventoryPageImpl page, boolean force) {
        FramedIconsHandler handler = getIconsHandler();

        if (((System.currentTimeMillis() - this.updateHandlerCooldown) > handler.getUpdateTime(page)) || force) {
            this.updateHandlerCooldown = System.currentTimeMillis();

            List<IconHandler> handlers = getCachedHandler(page);

            //TODO не будет ли нагружать..
            Collections.sort(handlers);

            int maxSize = handlers.size();

            MenuType type = page.getMenuType();

            ItemIcon[] activeIcons = page.getUnsafeActiveIcons();

            int pageLimit = (getWidth() * getHeight());

            setMaxPage(maxSize / pageLimit + (maxSize % pageLimit > 0 ? 1 : 0));

            setCurrentPage(this.page);

            int pageOffset = (this.page - 1) * pageLimit;
            if (pageOffset > maxSize) {
                pageOffset = maxSize;
            }

            int i = pageOffset;

            FrameIterationHandler iterationHandler = getIterationHandler();

            for (int z = iterationHandler.startPositionZ(getHeight()); iterationHandler.hasNextZ(z, getHeight()); z = iterationHandler.manipulateZ(z)) {
                for (int x = iterationHandler.startPositionX(getWidth()); iterationHandler.hasNextX(x, getWidth()); x = iterationHandler.manipulateX(x)) {

                    int slotOffset;
                    if (iterationHandler.isReverse()) {
                        slotOffset = getFirstZ() * type.getWidth() + getFirstX() + z + (x * type.getWidth());
                    }
                    else {
                        slotOffset = getFirstZ() * type.getWidth() + getFirstX() + x + (z * type.getWidth());
                    }

                    if (i > (maxSize - 1)) {
                        activeIcons[slotOffset] = null;
                    }
                    else {
                        activeIcons[slotOffset] = new ItemIcon(slotOffset, handlers.get(i));
                    }

                    i++;
                }
            }
        }
    }

    public boolean hasChanges() {
        return this.changes;
    }

    public void resetChanges() {
        this.changes = false;
    }

}
