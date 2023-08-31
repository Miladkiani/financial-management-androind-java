package com.mili.manito.Model;

import androidx.room.Embedded;

public class AccountList {


    private Long account_balance;

    @Embedded
    private AccountEntity account;

    public Long getAccount_balance() {
        return account_balance;
    }

    public AccountEntity getAccount() {
        return account;
    }


    public void setAccount_balance(Long account_balance) {
        this.account_balance = account_balance;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }
}
