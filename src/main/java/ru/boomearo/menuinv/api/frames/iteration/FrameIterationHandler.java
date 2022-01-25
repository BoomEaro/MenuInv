package ru.boomearo.menuinv.api.frames.iteration;

public interface FrameIterationHandler {

    public int startPositionX(int maxX);
    public int startPositionZ(int maxZ);

    public boolean hasNextX(int x, int maxX);
    public boolean hasNextZ(int z, int maxZ);

    public int manipulateX(int x);
    public int manipulateZ(int z);

    public boolean isReverse();
}
