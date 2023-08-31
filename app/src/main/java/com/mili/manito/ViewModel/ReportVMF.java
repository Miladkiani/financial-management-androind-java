package com.mili.manito.ViewModel;

import com.mili.manito.Model.MyDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ReportVMF extends ViewModelProvider.NewInstanceFactory {

    private final MyDatabase sDb;
    private final int mUserId;


    public ReportVMF(MyDatabase sDb, int mUserId) {
        this.sDb = sDb;
        this.mUserId = mUserId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ReportVM(sDb,mUserId);
    }
}
