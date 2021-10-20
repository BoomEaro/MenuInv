package ru.boomearo.menuinv.objects;

import ru.boomearo.menuinv.MenuInv;
import ru.boomearo.menuinv.api.AbstractButtonHandler;
import ru.boomearo.menuinv.api.InvType;
import ru.boomearo.menuinv.api.ListedIconsHandler;
import ru.boomearo.menuinv.api.TemplateListedIcons;

import java.util.List;

public class ListedIconItems extends TemplateListedIcons {

    private long updateHandlerCooldown = 0;

    private int page = 1;

    private int maxPage;

    public ListedIconItems(String name, int x, int z, int width, int height, ListedIconsHandler handler) {
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
        ListedIconsHandler handler = getHandler();
        List<AbstractButtonHandler> handlers = getHandler().onUpdate(page, page.getPlayer());

        if (((System.currentTimeMillis() - this.updateHandlerCooldown) > (handler.getUpdateTime() * 50)) || force) {
            this.updateHandlerCooldown = System.currentTimeMillis();

            int maxSize = handlers.size();

            InvType type = page.getType();

            ItemIcon[] activeIcons = page.getActiveIcons();

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

                    int offset = getZ() * type.getMaxWidth() + getX() + x + (z * type.getMaxWidth());

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

    public static enum ScrollType {

        NEXT,
        PREVIOUSLY;

    }
}
