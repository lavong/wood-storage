package com.jordylangen.woodstorage.utils;

import android.graphics.Color;

import java.util.Random;

public class ColorUtils {

    private static final int MAX_RGB_VALUE = 256;
    private static final int BASE_RED = 255;
    private static final int BASE_BLUE = 255;
    private static final int BASE_GREEN = 255;

    public static int randomColor() {
        Random random = new Random();
        int red = random.nextInt(MAX_RGB_VALUE);
        int green = random.nextInt(MAX_RGB_VALUE);
        int blue = random.nextInt(MAX_RGB_VALUE);

        red = (red + Color.red(BASE_RED)) / 2;
        green = (green + Color.green(BASE_BLUE)) / 2;
        blue = (blue + Color.blue(BASE_GREEN)) / 2;

        return Color.rgb(red, green, blue);
    }
}
