package ru.boomearo.menuinv.objects;

import ru.boomearo.menuinv.api.AbstractButtonHandler;
import ru.boomearo.menuinv.api.InvType;
import ru.boomearo.menuinv.api.ListedIconsHandler;
import ru.boomearo.menuinv.api.TemplateListedIcons;

import java.util.List;
import java.util.Map;

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

    public void nextPage() {
        int next = this.page + 1;
        if (next > this.maxPage) {
            next = this.maxPage;
        }

        this.page = next;
    }

    public void previouslyPage() {
        int back = this.page - 1;
        if (back <= 0) {
            back = 1;
        }
        this.page = back;
    }

    public void scrollPage(ScrollType type) {
        if (type == ScrollType.NEXT) {
            nextPage();
        }
        else if (type == ScrollType.PREVIOUSLY) {
            previouslyPage();
        }
    }

    public void updateActiveIcons(InventoryPage page, boolean force) {
        ListedIconsHandler handler = getHandler();
        List<AbstractButtonHandler> handlers = getHandler().update(page, page.getPlayer());

        if (((System.currentTimeMillis() - this.updateHandlerCooldown) > (handler.getUpdateTime() * 50)) || force) {
            this.updateHandlerCooldown = System.currentTimeMillis();

            int maxSize = handlers.size();

            InvType type = page.getType();

            Map<Integer, ItemIcon> activeIcons = page.getActiveIcons();

            int currentPage = this.page;

            int pageLimit = (getWidth() * getHeight());

            int offSet = (currentPage - 1) * pageLimit;
            if (offSet > maxSize) {
                offSet = maxSize;
            }

            this.maxPage = maxSize / pageLimit + (maxSize % pageLimit > 0 ? 1 : 0);

            int i = offSet;
            for (int z = 0; z < getHeight(); z++) {
                for (int x = 0; x < getWidth(); x++) {

                    int offset = getZ() * type.getMaxWidth() + getX() + x + (z * type.getMaxWidth());

                    if (i > (maxSize - 1)) {
                        activeIcons.remove(offset);
                    }
                    else {
                        activeIcons.put(offset, new ItemIcon(offset, handlers.get(i)));
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
