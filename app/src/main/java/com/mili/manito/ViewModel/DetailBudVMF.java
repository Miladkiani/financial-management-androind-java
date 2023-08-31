package com.mili.manito.ViewModel;

import com.mili.manito.Model.MyDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DetailBudVMF extends ViewModelProvider.NewInstanceFactory {
    private final MyDatabase sDb;
    private final int sBudId;

    public DetailBudVMF(MyDatabase sDb, int sBudId) {
        this.sDb = sDb;
        this.sBudId = sBudId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailBudVM(sDb,sBudId);
    }
}
