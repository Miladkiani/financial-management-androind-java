package com.mili.manito.ViewModel;

import com.mili.manito.Model.MyDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AccVMF extends ViewModelProvider.NewInstanceFactory {

    private final int mUserId;
    private final MyDatabase mDb;

    public AccVMF(int mUserId, MyDatabase mDb) {
        this.mUserId = mUserId;
        this.mDb = mDb;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AccVM(mDb,mUserId);
    }
}
