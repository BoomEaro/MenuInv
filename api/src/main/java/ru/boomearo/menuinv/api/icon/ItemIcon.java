package ru.boomearo.menuinv.api.icon;

import lombok.NonNull;

public interface ItemIcon {

    int getSlot();

    void setIconHandler(@NonNull IconHandler iconHandler);

    @NonNull
    IconHandler getIconHandler();

    void forceUpdate();

    boolean isForceUpdate();

}
