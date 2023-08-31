package com.mili.manito.ViewModel;

import com.mili.manito.Model.MyDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class EditAccVMF extends ViewModelProvider.NewInstanceFactory {
    private final MyDatabase sDb;
    private final int mUserId;
    private final int mAccId;

    public EditAccVMF(MyDatabase sDb, int mUserId, int mAccId) {
        this.sDb = sDb;
        this.mUserId = mUserId;
        this.mAccId = mAccId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EditAccVM(sDb,mUserId,mAccId);
    }
}
