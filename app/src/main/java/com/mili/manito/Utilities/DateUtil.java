package com.mili.manito.Utilities;



import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.text.DecimalFormat;
import java.util.Calendar;

public class DateUtil {

    //23:59:59:999
    public static final  int MAX_TIME_DAY = (23*60*60*1000)+(59*60*1000)+(59*1000)+(999);
    private long startTime;
    private long endTime;
    private PersianCalendar mDateInput;
    private String stringDate;
    private static int datePrefValue ;




    public DateUtil(PersianCalendar mDateInput) {
        this.mDateInput = mDateInput;
        datePrefValue = PrefManager.getIntervalPref();
        setInterval();
        setStringDate();
    }

    private void setStringDate(){
        if ( datePrefValue ==  PrefManager.DAILY_VALUE_PREF ){
           stringDate = (PersianDigitConverter.PersianNumber(
                     mDateInput.getPersianWeekDayName()+" "
                         +mDateInput.getPersianDay()+" "+
                          mDateInput.getPersianMonthName()+" "
                         +mDateInput.getPersianYear() )
            );
        }else if (datePrefValue == PrefManager.MONTHLY_VALUE_PREF){
            stringDate =(PersianDigitConverter.PersianNumber(
                                mDateInput.getPersianMonthName()+" "
                                    +mDateInput.getPersianYear()));
        }else if(datePrefValue == PrefManager.YEARLY_VALUE_PREF) {
            stringDate= (PersianDigitConverter.PersianNumber(""+
                    mDateInput.getPersianYear()));
        }
    }

    private void setInterval() {
        int month = mDateInput.getPersianMonth();
        int year = mDateInput.getPersianYear();
        int day = mDateInput.getPersianDay();
        PersianCalendar pc = new PersianCalendar();
        pc.clear();
        switch (datePrefValue) {
            case PrefManager.MONTHLY_VALUE_PREF:
                pc.setPersianDate(year, month, 1);
                this.startTime = pc.getTimeInMillis();
                pc.setPersianDate(year, month, getLastDayOfMonth(year, month));
                this.endTime = pc.getTimeInMillis() + MAX_TIME_DAY;
                break;
            case PrefManager.YEARLY_VALUE_PREF:
                pc.setPersianDate(year,
                        0,
                        1);
                startTime = pc.getTimeInMillis();
                pc.setPersianDate(year, 11, getLastDayOfMonth(year, 11));
                endTime = (pc.getTimeInMillis() + MAX_TIME_DAY);
                break;
            case PrefManager.DAILY_VALUE_PREF:
                pc.setPersianDate(year, month, day);
                startTime = pc.getTimeInMillis();
                endTime = startTime + MAX_TIME_DAY;
                break;
        }
    }

    public static String toStringDaily(long longDate){
        String s;
        PersianCalendar pc = new PersianCalendar();
        pc.clear();
        pc.setTimeInMillis(longDate);
        s= pc.getPersianDay()+" "+pc.getPersianMonthName()+" "+pc.getPersianYear();
        return PersianDigitConverter.PersianNumber(s);
    }

    public static  String toStringAll(long longDate){
        String s;
        PersianCalendar pc = new PersianCalendar();
        pc.clear();
        pc.setTimeInMillis(longDate);
        s= pc.getPersianDay()+"/"+pc.getPersianMonth()+"/"+pc.getPersianYear()+
        "-"+pc.get(PersianCalendar.HOUR_OF_DAY)+":"+pc.get(PersianCalendar.MINUTE)+":"+
        pc.get(PersianCalendar.SECOND)+":"+pc.get(PersianCalendar.MILLISECOND);
        return PersianDigitConverter.PersianNumber(s);
    }

    public static String toStringMonthly(long longDate){
        String s;
        PersianCalendar pc = new PersianCalendar();
        pc.clear();
        pc.setTimeInMillis(longDate);
        s= pc.getPersianMonthName()+" "+pc.getPersianYear();
        return PersianDigitConverter.PersianNumber(s);
    }

    public static String toStringStandard(long longDate){
        String s;
        DecimalFormat formatter = new DecimalFormat("00");
        PersianCalendar pc = new PersianCalendar();
        pc.clear();
        pc.setTimeInMillis(longDate);
        s= pc.getPersianYear()+"/"+formatter.format(pc.getPersianMonth()+1)+"/"
                +formatter.format(pc.getPersianDay());
        return PersianDigitConverter.PersianNumber(s);
    }


    public static PersianCalendar StringToPersianCalendar (String stringDate){
        String[] date = stringDate.split("/");
        int year  = Integer.parseInt(date[0]);
        int month  = Integer.parseInt(date[1])-1;
        int day  = Integer.parseInt(date[2]);

        PersianCalendar pc = new PersianCalendar();
        pc.clear();
        pc.setPersianDate(year,month,day);

        return pc;
    }

    public static Long StringMonthlyToLong(String s){
        String[] date = s.split(" ");
        int month  = intMonth(date[0]);
        int year  = Integer.parseInt(date[1]);
        PersianCalendar pc = new PersianCalendar();
        pc.clear();
        pc.setPersianDate(year,month,1);
        return pc.getTimeInMillis();
    }

    public static PersianCalendar StringFullToPersianCalendar(String s){
        String[] date = s.split(" ");
        int day = Integer.parseInt(date[1]);
        int month  = intMonth(date[2]);
        int year  = Integer.parseInt(date[3]);
        PersianCalendar pc = new PersianCalendar();
        pc.clear();
        pc.setPersianDate(year,month,day);
        return pc;
    }

    public static PersianCalendar addMonth(long startTime , int count){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(startTime);
        calendar.add(Calendar.MONTH,count);
        PersianCalendar pc = new PersianCalendar();
        pc.setTimeInMillis(calendar.getTimeInMillis());
        return pc;
    }
    public static int intMonth(String sMonth){

        switch (sMonth){
            case "فروردین":
                return 0;
            case "اردیبهشت":
                return 1;
            case "خرداد":
                return 2;
            case "تیر":
                return 3;
            case "مرداد":
                return 4;
            case "شهریور":
                return 5;
            case "مهر":
                return 6;
            case "آبان":
                return 7;
            case "آذر":
                return 8;
            case "دی":
                return 9;
            case "بهمن":
                return 10;
            case "اسفند":
                return 11;
                default:
                    return 1;
        }
    }

    public void addDateInput(int few){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mDateInput.getTimeInMillis());
        switch (datePrefValue){
            case PrefManager.MONTHLY_VALUE_PREF:
                calendar.add(Calendar.MONTH,few);
                mDateInput.setTimeInMillis(calendar.getTimeInMillis());
                break;
            case PrefManager.YEARLY_VALUE_PREF:
                calendar.add(Calendar.YEAR,few);
                mDateInput.setTime(calendar.getTime());
                break;
            default:
                calendar.add(Calendar.DATE,few);
                mDateInput.setTime(calendar.getTime());
                break;
        }
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getStringDate() {
        return stringDate;
    }

    public PersianCalendar getmDateInput() {
        return mDateInput;
    }

    public static int  getLastDayOfMonth(int year , int month){
        int day = 0;
        switch (month){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                day = 31;
                break;
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                day = 30;
                break;
            case 11:
                int y = year%33;
                if (y==1||y==5||y==9||y==13||y==17||y==22||y==26||y==30)
                    day = 30;
                else
                    day = 29;
                break;
        }
        return day;
    }

    public static String getTimeString (int hour, int minute ){
        DecimalFormat timeFormat = new DecimalFormat("00");
        return timeFormat.format(hour)+":"
                +timeFormat.format(minute);
    }

}
