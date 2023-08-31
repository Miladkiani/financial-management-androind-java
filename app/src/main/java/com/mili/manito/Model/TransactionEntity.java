package com.mili.manito.Model;

import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "transactions", foreignKeys = {
        @ForeignKey(entity = CategoryEntity.class,
                parentColumns = "cate_id",
                childColumns = "cate_id",
                onDelete = ForeignKey.CASCADE),

        @ForeignKey(entity = AccountEntity.class,
        parentColumns = "acc_id",
        childColumns = "acc_id_pay",
        onDelete = ForeignKey.CASCADE),

        @ForeignKey(entity = AccountEntity.class,
                parentColumns = "acc_id",
                childColumns = "acc_id_get",
                onDelete = ForeignKey.CASCADE),

        @ForeignKey(entity = LoanEntity.class,
        parentColumns = "loan_id",
        childColumns = "loan_id",
        onDelete = ForeignKey.CASCADE)},

        indices = {@Index("cate_id")
        ,@Index("loan_id")
        ,@Index("acc_id_pay")
        ,@Index("acc_id_get")}
        )
public class TransactionEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "tran_id")
    private int transaction_id;

    @NonNull
    @ColumnInfo(name = "tran_date")
    private PersianCalendar transaction_date;


    @NonNull
    @ColumnInfo(name = "tran_cost")
    private Long transaction_cost;


    @Nullable
    @ColumnInfo(name = "acc_id_pay")
    private Integer account_id_pay;

    @Nullable
    @ColumnInfo(name = "acc_id_get")
    private Integer account_id_get;

    @Nullable
    @ColumnInfo(name = "cate_id")
    private Integer category_id;

    @Nullable
    @ColumnInfo(name = "loan_id")
    private Integer loan_id;

    @NonNull
    @ColumnInfo(name = "tran_type")
    // loan, pay,get,transfer;
    private int transaction_type;


    public TransactionEntity(int transaction_id, @NonNull PersianCalendar transaction_date, @NonNull Long transaction_cost, @Nullable Integer account_id_pay, @Nullable Integer account_id_get, @Nullable Integer category_id, @Nullable Integer loan_id, int transaction_type) {
        this.transaction_id = transaction_id;
        this.transaction_date = transaction_date;
        this.transaction_cost = transaction_cost;
        this.account_id_pay = account_id_pay;
        this.account_id_get = account_id_get;
        this.category_id = category_id;
        this.loan_id = loan_id;
        this.transaction_type = transaction_type;
    }

    @Ignore
    public TransactionEntity(@NonNull PersianCalendar transaction_date, @NonNull Long transaction_cost, @Nullable Integer account_id_pay, @Nullable Integer account_id_get, @Nullable Integer category_id, @Nullable Integer loan_id, int transaction_type) {
        this.transaction_date = transaction_date;
        this.transaction_cost = transaction_cost;
        this.account_id_pay = account_id_pay;
        this.account_id_get = account_id_get;
        this.category_id = category_id;
        this.loan_id = loan_id;
        this.transaction_type = transaction_type;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    @NonNull
    public PersianCalendar getTransaction_date() {
        return transaction_date;
    }

    @NonNull
    public Long getTransaction_cost() {
        return transaction_cost;
    }


    @Nullable
    public Integer getAccount_id_pay() {
        return account_id_pay;
    }

    @Nullable
    public Integer getAccount_id_get() {
        return account_id_get;
    }

    @Nullable
    public Integer getCategory_id() {
        return category_id;
    }

    @Nullable
    public Integer getLoan_id() {
        return loan_id;
    }

    public int getTransaction_type() {
        return transaction_type;
    }


    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public void setLoan_id(@Nullable Integer loan_id) {
        this.loan_id = loan_id;
    }

    public void setAccount_id_pay(@Nullable Integer account_id_pay) {
        this.account_id_pay = account_id_pay;
    }

    public void setAccount_id_get(@Nullable Integer account_id_get) {
        this.account_id_get = account_id_get;
    }

    public void setTransaction_type(int transaction_type) {
        this.transaction_type = transaction_type;
    }


    public void setCategory_id(@Nullable Integer category_id) {
        this.category_id = category_id;
    }

    public final static int TRANSACTION_PAY =11;
    public final static int TRANSACTION_GET =22;
    public final static int TRANSACTION_TRANSFER =33;
    public final static int TRANSACTION_LOAN =44;

}
