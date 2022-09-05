package ark.noah.lucktest;

import android.graphics.Color;

public class RecyclerItem {
    Color color;
    boolean isIndiv;
    float givenValue;
    float valueRangedFrom;
    float valueRangedTo;
    boolean headInclusive;
    boolean tailInclusive;
    RandomMode randomMode;

    public static final int pass = Color.argb(255, 0, 255, 0);
    public static final int fail = Color.argb(255, 255, 0, 0);

    public RecyclerItem(int color, boolean isIndiv, float givenValue, float valueRangedFrom, float valueRangedTo, boolean headInclusive, boolean tailInclusive, RandomMode randomMode) {
        this.color = Color.valueOf(color);
        this.isIndiv = isIndiv;
        this.givenValue = givenValue;
        this.valueRangedFrom = valueRangedFrom;
        this.valueRangedTo = valueRangedTo;
        this.headInclusive = headInclusive;
        this.tailInclusive = tailInclusive;
        this.randomMode = randomMode;
    }
    public RecyclerItem(Color color, boolean isIndiv, float givenValue, float valueRangedFrom, float valueRangedTo, boolean headInclusive, boolean tailInclusive, RandomMode randomMode) {
        this.color = color;
        this.isIndiv = isIndiv;
        this.givenValue = givenValue;
        this.valueRangedFrom = valueRangedFrom;
        this.valueRangedTo = valueRangedTo;
        this.headInclusive = headInclusive;
        this.tailInclusive = tailInclusive;
        this.randomMode = randomMode;
    }

    public boolean result() {
        return color.toArgb() == pass;
    }
}
