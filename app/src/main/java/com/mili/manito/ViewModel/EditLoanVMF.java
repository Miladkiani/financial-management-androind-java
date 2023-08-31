package com.mili.manito.ViewModel;

import com.mili.manito.Model.MyDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class EditLoanVMF extends ViewModelProvider.NewInstanceFactory {
    private final int mLoanId;
    private final MyDatabase mDb;

    public EditLoanVMF(MyDatabase mDb, int mLoanId) {
        this.mLoanId = mLoanId;
        this.mDb = mDb;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EditLoanVM(mDb,mLoanId);
    }
}
