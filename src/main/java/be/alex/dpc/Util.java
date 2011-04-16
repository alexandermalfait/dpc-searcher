package be.alex.dpc;

import java.text.DecimalFormat;

public class Util {
    public static String getUsedMemoryFormatted() {
        long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        return new DecimalFormat("#,###,###").format(usedMemory) + "B";
    }
}
