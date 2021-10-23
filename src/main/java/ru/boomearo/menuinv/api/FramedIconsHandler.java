package ru.boomearo.menuinv.api;

import java.util.List;

/**
 * Представляет обработчик рамочных предметов.
 * На данный момент это маркерный интерфейс, реализующий Updatable с некоторыми дженериками.
 */
public interface FramedIconsHandler extends Updatable<List<IconHandler>, InventoryPage> {


}
