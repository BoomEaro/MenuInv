package ru.boomearo.menuinv.api.frames.inventory;

import ru.boomearo.menuinv.api.IconHandler;
import ru.boomearo.menuinv.api.InvType;
import ru.boomearo.menuinv.api.FramedIconsHandler;
import ru.boomearo.menuinv.objects.InventoryPageImpl;
import ru.boomearo.menuinv.objects.ItemIcon;

import java.util.ArrayList;
import java.util.List;

/**
 * Представляет страничную рамки, которую можно скроллить (перемещать вперед/назад)
 */
public class PagedItems extends FramedIcons {

    private long updateHandlerCooldown = 0;

    private int page = 1;

    private int maxPage = 1;

    private boolean changes = false;

    public PagedItems(String name, int x, int z, int width, int height, FramedIconsHandler handler) {
        super(name, x, z, width, height, handler);
    }

    /**
     * @return Текущую страницу
     */
    public int getCurrentPage() {
        return this.page;
    }

    /**
     * Устанавливает страницу
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

    /**
     * Обновляет актуальное состояние страницы
     * @param page Страница инвентаря
     * @param force Игнорировать ли задержку обновления
     */
    public void updateActiveIcons(InventoryPageImpl page, boolean force) {
        FramedIconsHandler handler = getHandler();

        if (((System.currentTimeMillis() - this.updateHandlerCooldown) > handler.getUpdateTime()) || force) {
            this.updateHandlerCooldown = System.currentTimeMillis();

            List<IconHandler> handlers = null;
            try {
                handlers = getHandler().onUpdate(page, page.getPlayer());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            if (handlers == null) {
                handlers = new ArrayList<>();
            }

            int maxSize = handlers.size();

            InvType type = page.getType();

            ItemIcon[] activeIcons = page.getUnsafeActiveIcons();

            int pageLimit = (getWidth() * getHeight());

            setMaxPage(maxSize / pageLimit + (maxSize % pageLimit > 0 ? 1 : 0));

            setCurrentPage(this.page);

            int offSet = (this.page - 1) * pageLimit;
            if (offSet > maxSize) {
                offSet = maxSize;
            }

            int i = offSet;

            for (int z = 0; z < getHeight(); z++) {
                for (int x = 0; x < getWidth(); x++) {

                    int offset = getFirstZ() * type.getWidth() + getFirstX() + x + (z * type.getWidth());


                    if (i > (maxSize - 1)) {
                        activeIcons[offset] = null;
                    }
                    else {
                        activeIcons[offset] = new ItemIcon(offset, handlers.get(i));
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

    public static enum ScrollType {

        NEXT("Вперёд") {
            @Override
            public int getNextPage(int currentPage) {
                return currentPage + 1;
            }
        },
        PREVIOUSLY("Назад") {
            @Override
            public int getNextPage(int currentPage) {
                return currentPage - 1;
            }
        };

        private final String name;

        ScrollType(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public abstract int getNextPage(int currentPage);
    }
}
