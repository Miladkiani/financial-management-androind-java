package com.mili.manito.Model;

import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "budget", foreignKeys = @ForeignKey(
        entity = UserEntity.class,
        parentColumns = "user_id",
        childColumns = "user_id",
        onDelete= ForeignKey.CASCADE
        ),indices = {@Index("user_id")})
public class BudgetEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "bud_id")
    private int budget_id;

    @NonNull
    @ColumnInfo(name = "bud_title")
    private String budget_title;

    @NonNull
    @ColumnInfo(name = "bud_cost")
    private Long budget_cost;

    @NonNull
    @ColumnInfo(name = "bud_start_time")
    private PersianCalendar budget_start_time;

    @NonNull
    @ColumnInfo(name = "bud_end_time")
    private PersianCalendar budget_end_time;

    @NonNull
    @ColumnInfo(name = "user_id")
    private int user_id;


    public BudgetEntity(int budget_id, @NonNull String budget_title, @NonNull Long budget_cost, @NonNull PersianCalendar budget_start_time, @NonNull PersianCalendar budget_end_time, int user_id) {
        this.budget_id = budget_id;
        this.budget_title = budget_title;
        this.budget_cost = budget_cost;
        this.budget_start_time = budget_start_time;
        this.budget_end_time = budget_end_time;
        this.user_id = user_id;
    }

    @Ignore
    public BudgetEntity(@NonNull String budget_title, @NonNull Long budget_cost, @NonNull PersianCalendar budget_start_time, @NonNull PersianCalendar budget_end_time, int user_id) {
        this.budget_title = budget_title;
        this.budget_cost = budget_cost;
        this.budget_start_time = budget_start_time;
        this.budget_end_time = budget_end_time;
        this.user_id = user_id;
    }


    @NonNull
    public Long getBudget_cost() {
        return budget_cost;
    }

    public void setBudget_id(int budget_id) {
        this.budget_id = budget_id;
    }

    public int getBudget_id() {
        return budget_id;
    }

    @NonNull
    public String getBudget_title() {
        return budget_title;
    }


    @NonNull
    public PersianCalendar getBudget_start_time() {
        return budget_start_time;
    }

    @NonNull
    public PersianCalendar getBudget_end_time() {
        return budget_end_time;
    }

    public int getUser_id() {
        return user_id;
    }
}
