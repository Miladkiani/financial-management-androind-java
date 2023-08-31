package com.mili.manito.Model;

import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

public class LoanList {


    private Integer loan_id;


    private String loan_title;

    private PersianCalendar loan_start_time;


    private PersianCalendar loan_end_time;

    private Long loan_ins_cost;

    private int loan_ins_count;

    private Long loan_paid;

    public LoanList(Integer loan_id, String loan_title, PersianCalendar loan_start_time, PersianCalendar loan_end_time, Long loan_ins_cost, int loan_ins_count, Long loan_paid) {
        this.loan_id = loan_id;
        this.loan_title = loan_title;
        this.loan_start_time = loan_start_time;
        this.loan_end_time = loan_end_time;
        this.loan_ins_cost = loan_ins_cost;
        this.loan_ins_count = loan_ins_count;
        this.loan_paid = loan_paid;
    }

    public Integer getLoan_id() {
        return loan_id;
    }

    public String getLoan_title() {
        return loan_title;
    }

    public PersianCalendar getLoan_start_time() {
        return loan_start_time;
    }

    public PersianCalendar getLoan_end_time() {
        return loan_end_time;
    }

    public Long getLoan_ins_cost() {
        return loan_ins_cost;
    }

    public int getLoan_ins_count() {
        return loan_ins_count;
    }

    public Long getLoan_paid() {
        return loan_paid;
    }

    public void setLoan_id(Integer loan_id) {
        this.loan_id = loan_id;
    }
}
