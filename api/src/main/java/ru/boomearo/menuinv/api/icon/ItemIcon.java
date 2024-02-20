package ru.boomearo.menuinv.api.icon;

public interface ItemIcon {

    int getSlot();

    void setIconHandler(IconHandler iconHandler);

    IconHandler getIconHandler();

    void forceUpdate();

    boolean isForceUpdate();

}
