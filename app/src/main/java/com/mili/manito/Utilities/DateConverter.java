package com.mili.manito.Utilities;

import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import androidx.room.TypeConverter;


public class DateConverter {
    @TypeConverter
    public static PersianCalendar toDate(Long timestamp) {
        if (timestamp!=null){
         PersianCalendar pc = new PersianCalendar();
         pc.setTimeInMillis(timestamp);
         return pc;
        }else{
            return null;
        }
    }

    @TypeConverter
    public static Long toLong(PersianCalendar pc) {
        return  pc == null ? null : pc.getTimeInMillis();
    }
}
