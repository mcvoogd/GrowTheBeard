package nl.avans.a3.util;

/**
 * Created by FlorisBob on 07-Jun-16.
 */
public class MathExtended {
    public static float clamp(float value, float min, float max)
    {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }
}
