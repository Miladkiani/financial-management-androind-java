package com.mili.manito.ViewModel;

import com.mili.manito.Model.MyDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public class LoanVMF extends ViewModelProvider.NewInstanceFactory {
    private final MyDatabase sDb;
    private final int sUserId;

    public LoanVMF(MyDatabase sDb, int sUserId) {
        this.sDb = sDb;
        this.sUserId = sUserId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new LoanVM(sDb,sUserId);
    }
}
