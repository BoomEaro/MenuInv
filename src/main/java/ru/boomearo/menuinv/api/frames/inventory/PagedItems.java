package ru.boomearo.menuinv.api.frames.inventory;

import ru.boomearo.menuinv.api.IconHandler;
import ru.boomearo.menuinv.api.InvType;
import ru.boomearo.menuinv.api.FramedIconsHandler;
import ru.boomearo.menuinv.objects.InventoryPage;
import ru.boomearo.menuinv.objects.ItemIcon;

import java.util.List;

public class PagedItems extends FramedIcons {

    private long updateHandlerCooldown = 0;

    private int page = 1;

    private int maxPage;

    public PagedItems(String name, int x, int z, int width, int height, FramedIconsHandler handler) {
        super(name, x, z, width, height, handler);
    }

    public int getCurrentPage() {
        return this.page;
    }

    public void setCurrentPage(int page) {
        if (page <= 0) {
            page = 1;
        }

        if (page > this.maxPage) {
            page = this.maxPage;
        }

        this.page = page;
    }

    public int getMaxPage() {
        return this.maxPage;
    }

    public boolean nextPage() {
        int newPage = this.page + 1;

        int next = newPage;
        if (next > this.maxPage) {
            next = this.maxPage;
        }

        this.page = next;

        return this.page == newPage;
    }

    public boolean previouslyPage() {
        int newPage = this.page - 1;

        int back = newPage;
        if (back <= 0) {
            back = 1;
        }
        this.page = back;

        return this.page == newPage;
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

    public void updateActiveIcons(InventoryPage page, boolean force) {
        FramedIconsHandler handler = getHandler();

        if (((System.currentTimeMillis() - this.updateHandlerCooldown) > (handler.getUpdateTime() * 50)) || force) {
            this.updateHandlerCooldown = System.currentTimeMillis();

            List<IconHandler> handlers = null;
            try {
                handlers = getHandler().onUpdate(page, page.getPlayer());
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            if (handlers != null) {
                int maxSize = handlers.size();

                InvType type = page.getType();

                ItemIcon[] activeIcons = page.getUnsafeActiveIcons();

                int currentPage = this.page;

                int pageLimit = (getWidth() * getHeight());

                this.maxPage = maxSize / pageLimit + (maxSize % pageLimit > 0 ? 1 : 0);

                int offSet = (currentPage - 1) * pageLimit;
                if (offSet > maxSize) {
                    offSet = maxSize;

                    //TODO возможно придется это как то исправить
                    //this.page = this.maxPage;
                }

                int i = offSet;
                for (int z = 0; z < getHeight(); z++) {
                    for (int x = 0; x < getWidth(); x++) {

                        int offset = getFirstZ() * type.getMaxWidth() + getFirstX() + x + (z * type.getMaxWidth());

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
