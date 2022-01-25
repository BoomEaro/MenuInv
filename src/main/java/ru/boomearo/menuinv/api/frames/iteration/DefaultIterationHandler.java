package ru.boomearo.menuinv.api.frames.iteration;

public class DefaultIterationHandler implements FrameIterationHandler {

    @Override
    public int startPositionX() {
        return 0;
    }

    @Override
    public int startPositionZ() {
        return 0;
    }

    @Override
    public boolean hasNextX(int x, int maxX) {
        return x < maxX;
    }

    @Override
    public boolean hasNextZ(int z, int maxZ) {
        return z < maxZ;
    }

    @Override
    public int manipulateX(int x) {
        return x + 1;
    }

    @Override
    public int manipulateZ(int z) {
        return z + 1;
    }

}
