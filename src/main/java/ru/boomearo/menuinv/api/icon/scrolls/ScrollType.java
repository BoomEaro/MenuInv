package ru.boomearo.menuinv.api.icon.scrolls;

public enum ScrollType {

    NEXT() {
        @Override
        public int getNextPage(int currentPage) {
            return currentPage + 1;
        }
    },

    PREVIOUSLY() {
        @Override
        public int getNextPage(int currentPage) {
            return currentPage - 1;
        }
    };

    public abstract int getNextPage(int currentPage);
}
