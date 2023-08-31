package com.mili.manito.ViewModel;

import com.mili.manito.Model.MyDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class EditTagVMF extends ViewModelProvider.NewInstanceFactory {

    private final MyDatabase mDb;
    private final int mTagId;

    public EditTagVMF(MyDatabase mDb, int mTagId) {
        this.mDb = mDb;
        this.mTagId = mTagId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EditTagVM(mDb,mTagId);
    }
}
