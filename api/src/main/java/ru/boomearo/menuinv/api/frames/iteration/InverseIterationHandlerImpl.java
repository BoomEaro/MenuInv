package ru.boomearo.menuinv.api.frames.iteration;

public class InverseIterationHandlerImpl implements FrameIterationHandler {

    public static final InverseIterationHandlerImpl DEFAULT = new InverseIterationHandlerImpl(false);
    public static final InverseIterationHandlerImpl REVERSE = new InverseIterationHandlerImpl(true);

    private final boolean reverse;

    private InverseIterationHandlerImpl(boolean reverse) {
        this.reverse = reverse;
    }

    private InverseIterationHandlerImpl() {
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
        return z - 1;
    }

    @Override
    public boolean isReverse() {
        return this.reverse;
    }
}
