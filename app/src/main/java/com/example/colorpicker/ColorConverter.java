package com.example.colorpicker;

public class ColorConverter {
    public static int rgbToArgb(int alpha, int red, int green, int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public static String argbToHex(int argbValue) {
        return String.format("#%08X", argbValue);
    }
}
