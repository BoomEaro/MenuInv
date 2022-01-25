package ru.boomearo.menuinv.api.frames.iteration;

public class InverseIterationHandler implements FrameIterationHandler {

    private final boolean reverse;

    public InverseIterationHandler(boolean reverse) {
        this.reverse = reverse;
    }

    public InverseIterationHandler() {
        this.reverse = false;
    }

    @Override
    public int startPositionX(int maxX) {
        return maxX - 1;
    }

    @Override
    public int startPositionZ(int maxZ) {
        return maxZ - 1;
    }

    @Override
    public boolean hasNextX(int x, int maxX) {
        return x >= 0;
    }

    @Override
    public boolean hasNextZ(int z, int maxZ) {
        return z >= 0;
    }

    @Override
    public int manipulateX(int x) {
        return x - 1;
    }

    @Override
    public int manipulateZ(int z) {
        return z- 1;
    }

    @Override
    public boolean isReverse() {
        return this.reverse;
    }
}
