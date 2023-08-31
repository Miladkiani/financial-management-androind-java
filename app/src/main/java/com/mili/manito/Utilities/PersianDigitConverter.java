package com.mili.manito.Utilities;

import java.text.DecimalFormat;

public class PersianDigitConverter {
    private static String[] persianNumbers = new String[]{ "۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹" };

    public static String PersianNumber(String text) {
        if (text.length() == 0) {
            return "";
        }
        String out = "";
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if ('0' <= c && c <= '9') {
                int number = Integer.parseInt(String.valueOf(c));
                out += persianNumbers[number];
            } else if (c == '٫') {
                out += '،';
            } else {
                out += c;
            }
        }
        return out;
    }

    public static String NumberFormat (Long number){
        DecimalFormat df = new DecimalFormat("#,###,###,###,###");
        return  df.format(number);
    }

    public static String NumberFormat (int number){
        DecimalFormat df = new DecimalFormat("#,###,###,###,###");
        return  df.format(number);
    }

    public static String NumberFormat (float number){
        DecimalFormat df = new DecimalFormat("#,###,###,###,###");
        return  df.format(number);
    }
}
