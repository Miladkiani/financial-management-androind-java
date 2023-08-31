package com.mili.manito.ViewModel;

import android.database.Cursor;

import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Repo.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class ReportVM extends ViewModel {

    private LiveData<Cursor> sumCost;
    private LiveData<Cursor> reportAll;
    private Repository mRepo;
    private MyDatabase sDb;
    private int sUserId;
    private MutableLiveData<Long[]> mInterval = new MutableLiveData<>();


    public ReportVM(MyDatabase db, int userId) {
        this.sDb = db;
        this.sUserId = userId;
        mRepo = Repository.getInstance();
        sumCost = Transformations.switchMap(mInterval,
                 x->mRepo.getSumByDay(sDb,x));
        reportAll = Transformations.switchMap(mInterval,
                z->mRepo.getReportByDay(sDb,z));

    }

    public LiveData<Cursor> getSumCost() {
        return sumCost;
    }

    public LiveData<Cursor> getReportAll() {
        return reportAll;
    }

    public void setInterval(Long[] interval) {
        mInterval.setValue(interval);
    }
}
