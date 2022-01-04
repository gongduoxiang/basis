package com.yc.basis.utils;

public class SizeUtils {

    private static double b_size = 1024;
    private static double kb_size = b_size * 1024;
    public static double m_size = kb_size * 1024;

    public static String getSiezGB(double l) {
        String size = "";
        if (l <= 0) {
            size = "0B";
        } else if (l < b_size) {
            size = l + "B";
        } else if (l < kb_size) {
            size = DataUtils.roundDouble(l / b_size) + "KB";
        } else if (l < m_size) {
            size = DataUtils.roundDouble(l / kb_size) + "M";
        } else {
            size = DataUtils.roundDouble(l / m_size) + "G";
        }
        return size;
    }

}
