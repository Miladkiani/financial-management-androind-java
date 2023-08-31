package com.mili.manito.ViewModel;

import com.mili.manito.Model.AccountEntity;
import com.mili.manito.Model.MyDatabase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class EditAccVM extends ViewModel {

    private LiveData<AccountEntity> sAccounts;
    private MyDatabase sDb;
    public EditAccVM(MyDatabase db , int userId , int accId) {
        sDb = db;
        sAccounts = sDb.accountDao().getAccountById(accId);
    }

    public LiveData<AccountEntity> getsAccounts() {
        return sAccounts;
    }
}
