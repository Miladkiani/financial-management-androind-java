package com.mili.manito.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "account" ,
        foreignKeys = @ForeignKey(entity =  UserEntity.class,
                                            parentColumns = "user_id",
                                            childColumns = "user_id",
                                            onDelete = CASCADE),
        indices = {@Index("user_id"),@Index("acc_name")}
)

public class AccountEntity {

    public AccountEntity(int account_id, @NonNull String account_name, int acc_condition ,Long acc_primitive_ballance, int user_id) {
        this.account_id = account_id;
        this.account_name = account_name;
        this.acc_condition = acc_condition;
        this.user_id = user_id;
        this.acc_primitive_ballance = acc_primitive_ballance;
    }

    @Ignore
    public AccountEntity(@NonNull String account_name, int acc_condition,Long acc_primitive_ballance, int user_id) {
        this.account_name = account_name;
        this.acc_condition = acc_condition;
        this.user_id = user_id;
        this.acc_primitive_ballance = acc_primitive_ballance;
    }

    public int getAcc_condition() {
        return acc_condition;
    }

    public int getAccount_id() {
        return account_id;
    }

    @NonNull
    public String getAccount_name() {
        return account_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public Long getAcc_primitive_ballance() {
        return acc_primitive_ballance;
    }

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "acc_id")
    private int account_id;

    @NonNull
    @ColumnInfo(name = "acc_name")
    private String account_name;


    @NonNull
    @ColumnInfo(name = "acc_condition")
    private int acc_condition;

    @NonNull
    @ColumnInfo (name = "acc_pri_balance")
    private Long acc_primitive_ballance;

    @NonNull
    @ColumnInfo(name = "user_id")
    private int user_id;

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public static final int ACCOUNT_BLOCKED  = 2;
    public static final int ACCOUNT_ACTIVE  = 1;
}
