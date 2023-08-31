package com.mili.manito.ViewModel;

import com.mili.manito.Model.LabelsEntity;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Repo.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class EditTagVM extends ViewModel {


    private LiveData<LabelsEntity> label ;
    public EditTagVM(MyDatabase mDb, int mTagId) {
        label = Repository.getInstance().getLabelsById(mDb,mTagId);
    }

    public LiveData<LabelsEntity> getLabel() {
        return label;
    }
}
