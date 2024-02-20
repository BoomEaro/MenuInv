package ru.boomearo.menuinv.api.frames.iteration;

public class DefaultIterationHandlerImpl implements FrameIterationHandler {

    public static final DefaultIterationHandlerImpl DEFAULT = new DefaultIterationHandlerImpl(false);
    public static final DefaultIterationHandlerImpl REVERSE = new DefaultIterationHandlerImpl(true);

    private final boolean reverse;

    private DefaultIterationHandlerImpl(boolean reverse) {
        this.reverse = reverse;
    }

    private DefaultIterationHandlerImpl() {
        this.reverse = false;
    }

    @Override
    public int startPositionX(int maxX) {
        return 0;
    }

    @Override
    public int startPositionZ(int maxZ) {
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

    @Override
    public boolean isReverse() {
        return this.reverse;
    }
}
