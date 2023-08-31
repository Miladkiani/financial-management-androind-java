package com.mili.manito.Model;

import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import androidx.room.Ignore;

public class BudgetDetailList  {
    private Long budMax;
    private String budTitle;
    private PersianCalendar budSDate;
    private PersianCalendar budEDate;
    private String budCate;
    private int cateId;
    private String budCateColor;
    private Long budCateCost;

    @Ignore
    private Long budPaid;

    public BudgetDetailList(Long budMax, String budTitle, PersianCalendar budSDate, PersianCalendar budEDate, String budCate, int cateId, String budCateColor, Long budCateCost) {
        this.budMax = budMax;
        this.budTitle = budTitle;
        this.budSDate = budSDate;
        this.budEDate = budEDate;
        this.budCate = budCate;
        this.cateId = cateId;
        this.budCateColor = budCateColor;
        this.budCateCost = budCateCost;

    }



    public Long getBudMax() {
        return budMax;
    }

    public String getBudTitle() {
        return budTitle;
    }

    public PersianCalendar getBudSDate() {
        return budSDate;
    }

    public PersianCalendar getBudEDate() {
        return budEDate;
    }

    public String getBudCate() {
        return budCate;
    }

    public int getCateId() {
        return cateId;
    }

    public String getBudCateColor() {
        return budCateColor;
    }

    public Long getBudCateCost() {
        return budCateCost;
    }

    public Long getBudPaid() {
        return budPaid;
    }

    public void setBudPaid(Long budPaid) {
        this.budPaid = budPaid;
    }
}
