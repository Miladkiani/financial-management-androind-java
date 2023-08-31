package com.mili.manito.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager  {
    private static SharedPreferences mSharedPref;
    private static SharedPreferences.Editor mSharedPrefEditor;
    private final static  String MAIN_PREF = "thisH_pref";
    public final static  String DATE_PREF_ITEM = "trans_date_filter" ;
    public final static  String PIN_PREF_ITEM = "trans_pin_filter" ;
    public final static String UNIT_PREF_ITEM = "trans_unit_filter";
    public final static  int DAILY_VALUE_PREF = 25;
    public final static  int MONTHLY_VALUE_PREF = 35;
    public final static  int YEARLY_VALUE_PREF = 45;
    public final static String DEFAULT_PIN_VALUE = "not Pass";
    public final static String RIAL_UNIT_VALUE = "ریال";
    public final static String TOOMAN_UNIT_VALUE = "تومان";



    private PrefManager() {

    }
    public static void initialize(Context context) {
        mSharedPref= context.getSharedPreferences(MAIN_PREF,
                Context.MODE_PRIVATE);
        mSharedPrefEditor = context.getSharedPreferences(MAIN_PREF,Context.MODE_PRIVATE).edit();
    }
    public static SharedPreferences getSharedPreferences() {

        return mSharedPref;
    }


    public static int getIntervalPref() {

        return mSharedPref.getInt(DATE_PREF_ITEM,DAILY_VALUE_PREF);

    }

    public static String getPinPref() {

        return mSharedPref.getString(PIN_PREF_ITEM,DEFAULT_PIN_VALUE);

    }

    public static String getUnitPref() {

        return mSharedPref.getString(UNIT_PREF_ITEM,TOOMAN_UNIT_VALUE);

    }

    public static void setIntervalPref(int interval){
        mSharedPrefEditor.putInt(DATE_PREF_ITEM,interval);
        mSharedPrefEditor.apply();
    }

    public static void setPinPref(String pin){
        mSharedPrefEditor.putString(PIN_PREF_ITEM,pin);
        mSharedPrefEditor.apply();
    }

    public static void setUnitPref(String unit){
        mSharedPrefEditor.putString(UNIT_PREF_ITEM,unit);
        mSharedPrefEditor.apply();
    }


}
