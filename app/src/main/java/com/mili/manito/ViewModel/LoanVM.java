package com.mili.manito.ViewModel;

import com.mili.manito.Model.LoanList;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Repo.Repository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class LoanVM extends ViewModel {

    private Repository mRepo;
    private MyDatabase sDb;
    private int mUserId;
    private LiveData<List<LoanList>> loans;

    public LoanVM(MyDatabase sDb, int mUserId) {
        this.sDb = sDb;
        this.mUserId = mUserId;
        mRepo = Repository.getInstance();
        loans = mRepo.getLoansByUser(sDb,mUserId);
    }

    public LiveData<List<LoanList>> getLoans() {
        return loans;
    }
}
