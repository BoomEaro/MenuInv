package ru.boomearo.menuinv.api.frames;

import ru.boomearo.menuinv.api.InventoryPage;
import ru.boomearo.menuinv.api.Updatable;
import ru.boomearo.menuinv.api.icon.IconHandler;

import java.util.List;

/**
 * Представляет обработчик рамочных предметов.
 * На данный момент это маркерный интерфейс, реализующий Updatable с некоторыми дженериками.
 */
public interface FramedIconsHandler extends Updatable<List<IconHandler>, InventoryPage> {


}
