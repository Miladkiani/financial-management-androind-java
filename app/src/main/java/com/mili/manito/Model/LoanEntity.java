package com.mili.manito.Model;

import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;


import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "loan" ,
        foreignKeys = @ForeignKey(entity =  UserEntity.class,
                parentColumns = "user_id",
                childColumns = "user_id",
                onDelete = CASCADE),indices = @Index("user_id")
)
public class LoanEntity {


    public LoanEntity(@NonNull Integer loan_id, @NonNull String loan_title , @NonNull PersianCalendar loan_start_time, @NonNull PersianCalendar loan_end_time, @NonNull Long loan_instalment_cost, int loan_instalment_count, int user_id) {
        this.loan_id = loan_id;
        this.loan_title= loan_title;
        this.loan_start_time = loan_start_time;
        this.loan_end_time = loan_end_time;
        this.loan_instalment_cost = loan_instalment_cost;
        this.loan_instalment_count = loan_instalment_count;
        this.user_id = user_id;
    }

    @Ignore
    public LoanEntity(@NonNull String loan_title,@NonNull PersianCalendar loan_start_time, @NonNull PersianCalendar loan_end_time, @NonNull Long loan_instalment_cost, int loan_instalment_count, int user_id) {
        this.loan_start_time = loan_start_time;
        this.loan_end_time = loan_end_time;
        this.loan_instalment_cost = loan_instalment_cost;
        this.loan_instalment_count = loan_instalment_count;
        this.user_id = user_id;
        this.loan_title= loan_title;
    }


    @NonNull
    public Integer getLoan_id() {
        return loan_id;
    }

    @NonNull
    public PersianCalendar getLoan_start_time() {
        return loan_start_time;
    }

    @NonNull
    public PersianCalendar getLoan_end_time() {
        return loan_end_time;
    }

    @NonNull
    public Long getLoan_instalment_cost() {
        return loan_instalment_cost;
    }

    public int getLoan_instalment_count() {
        return loan_instalment_count;
    }

    public int getUser_id() {
        return user_id;
    }

    @NonNull
    public String getLoan_title() {
        return loan_title;
    }

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "loan_id")
    private Integer loan_id;

    @NonNull
    @ColumnInfo(name = "loan_title")
    private String loan_title;


    @NonNull
    @ColumnInfo(name = "loan_start_time")
    private PersianCalendar loan_start_time;

    @NonNull
    @ColumnInfo(name = "loan_end_time")
    private PersianCalendar loan_end_time;

    @NonNull
    @ColumnInfo(name = "loan_ins_cost")
    private Long loan_instalment_cost;

    @NonNull
    @ColumnInfo(name = "loan_ins_count")
    private int loan_instalment_count;

    @NonNull
    @ColumnInfo(name = "user_id")
    private int user_id;

    public void setLoan_id(@NonNull Integer loan_id) {
        this.loan_id = loan_id;
    }
}
