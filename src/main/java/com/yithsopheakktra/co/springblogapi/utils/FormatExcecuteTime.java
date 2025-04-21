package com.yithsopheakktra.co.springblogapi.utils;

public class FormatExcecuteTime {

    public static String formatExecutionTime(double seconds) {
        if (seconds < 0.001) {
            return String.format("%.0f microseconds", seconds * 1_000_000);
        } else if (seconds < 1) {
            return String.format("%.2f milliseconds", seconds * 1_000);
        } else {
            return String.format("%.3f seconds", seconds);
        }
    }

}
