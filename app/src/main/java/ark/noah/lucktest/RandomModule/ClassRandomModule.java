package ark.noah.lucktest.RandomModule;

import java.util.Random;

import ark.noah.lucktest.RandomMode;

public class ClassRandomModule extends RandomModule{
    static Random prng;

    public ClassRandomModule() {
        if(prng != null) return;
        prng = new Random();
        selfMode = RandomMode.ClassRandom;
    }

    @Override
    protected void finalize() throws Throwable {
        prng = null;
    }

    @Override
    public float getValue() {
        return prng.nextFloat();
    }
}
