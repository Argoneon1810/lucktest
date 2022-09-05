package ark.noah.lucktest.RandomModule;

import ark.noah.lucktest.RandomMode;

public class MathRandomModule extends RandomModule {
    public MathRandomModule() {
        selfMode = RandomMode.MathRandom;
    }

    @Override
    protected void finalize() throws Throwable {

    }

    @Override
    public float getValue() {
        return (float) Math.random();
    }
}
