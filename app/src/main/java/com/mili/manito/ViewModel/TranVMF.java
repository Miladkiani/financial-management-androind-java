package com.mili.manito.ViewModel;

import com.mili.manito.Model.MyDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TranVMF extends ViewModelProvider.NewInstanceFactory {
    private final MyDatabase sDb;
    private final int mUserId;
    private final int mTranBuy;
    public TranVMF(MyDatabase sDb, int userId,int tranBuy) {
        this.sDb = sDb;
        this.mUserId = userId;
        this.mTranBuy = tranBuy;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TranVM(sDb,mUserId,mTranBuy);
    }
}
