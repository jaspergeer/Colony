package com.jaspergeer.simulation;

public class Utils {
    public static int clamp(int val, int min, int max) {
        if(val > max) {
            return max;
        } else if (val < min) {
            return min;
        }
        return val;
    }
}
