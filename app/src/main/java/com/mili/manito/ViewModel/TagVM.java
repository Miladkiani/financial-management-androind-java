package com.mili.manito.ViewModel;

import android.app.Application;

import com.mili.manito.Model.LabelsEntity;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Repo.Repository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TagVM extends AndroidViewModel {

    private LiveData<List<LabelsEntity>> labels;
    private MyDatabase mDb;
    private Repository mRepo;

    public TagVM(@NonNull Application application) {
        super(application);
        mDb = MyDatabase.getInstance(application.getApplicationContext());
        this.mRepo = Repository.getInstance();
        labels= mRepo.getLabelsByUserId(mDb,1);

    }

    public LiveData<List<LabelsEntity>> getLabels() {
        return labels;
    }
}
