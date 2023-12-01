package ru.boomearo.menuinv.api.frames;

import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.Updatable;
import ru.boomearo.menuinv.api.icon.IconHandler;
import ru.boomearo.menuinv.api.session.InventorySession;

import java.util.List;

public interface FramedIconsHandler<SESSION extends InventorySession> extends Updatable<List<IconHandler<SESSION>>, InventoryPage<SESSION>> {

}
