package ru.boomearo.menuinv.api.frames.iteration;

public interface FrameIterationHandler {

    public int startPositionX();
    public int startPositionZ();

    public boolean hasNextX(int x, int maxX);
    public boolean hasNextZ(int z, int maxZ);

    public int manipulateX(int x);
    public int manipulateZ(int z);

}
