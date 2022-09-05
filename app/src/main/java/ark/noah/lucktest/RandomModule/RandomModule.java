package ark.noah.lucktest.RandomModule;

import ark.noah.lucktest.RandomMode;

public class RandomModule {
    RandomMode selfMode;

    public float getValue() {
        return 0;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    public void release() throws Throwable {
        finalize();
    }

    public RandomMode getRandomMode() {
        return selfMode;
    }
}
