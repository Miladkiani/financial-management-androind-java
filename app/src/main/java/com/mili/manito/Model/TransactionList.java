package com.mili.manito.Model;

import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;


public class TransactionList {
    public TransactionList(Integer transaction_id, PersianCalendar transaction_date,
                           Long transaction_cost, Integer transaction_type,
                           String account_name_pay, String account_name_get,
                           String category_name, String category_color,
                           @Nullable Integer loan_id) {
        this.transaction_id = transaction_id;
        this.transaction_date = transaction_date;
        this.transaction_cost = transaction_cost;
        this.transaction_type = transaction_type;
        this.account_name_pay = account_name_pay;
        this.account_name_get = account_name_get;
        this.category_name = category_name;
        this.category_color = category_color;
        this.loan_id = loan_id;
    }

    public Integer getTransaction_id() {
        return transaction_id;
    }

    public PersianCalendar getTransaction_date() {
        return transaction_date;
    }

    public Long getTransaction_cost() {
        return transaction_cost;
    }

    public Integer getTransaction_type() {
        return transaction_type;
    }


    public String getAccount_name_pay() {
        return account_name_pay;
    }

    public String getAccount_name_get() {
        return account_name_get;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getCategory_color() {
        return category_color;
    }

    @Nullable
    public Integer getLoan_id() {
        return loan_id;
    }

    @ColumnInfo(name = "tran_id")
    private Integer transaction_id;

    @ColumnInfo(name = "tran_date")
    private PersianCalendar transaction_date;

    @ColumnInfo(name = "tran_cost")
    private Long transaction_cost;

    @ColumnInfo(name = "tran_type")
    private Integer transaction_type;

    @ColumnInfo(name = "acc_name_pay")
    private String account_name_pay;

    @ColumnInfo(name = "acc_name_get")
    private String account_name_get;

    @ColumnInfo(name = "cate_name")
    private String category_name;

    @ColumnInfo(name = "cate_color")
    private String category_color;

    @Nullable
    @ColumnInfo(name = "loan_id")
    private Integer loan_id;

    @Ignore
    private List<LabelsEntity> tags;

    public List<LabelsEntity> getTags() {
        return tags;
    }

    public void setTags(List<LabelsEntity> tags) {
        this.tags = tags;
    }
}


