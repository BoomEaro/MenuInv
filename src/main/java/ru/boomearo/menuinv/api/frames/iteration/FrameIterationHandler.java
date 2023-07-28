package ru.boomearo.menuinv.api.frames.iteration;

public interface FrameIterationHandler {

    int startPositionX(int maxX);

    int startPositionZ(int maxZ);

    boolean hasNextX(int x, int maxX);

    boolean hasNextZ(int z, int maxZ);

    int manipulateX(int x);

    int manipulateZ(int z);

    boolean isReverse();
}
