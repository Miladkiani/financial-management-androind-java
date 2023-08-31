package com.mili.manito.ViewModel;

import com.mili.manito.Model.LoanEntity;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Repo.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class EditLoanVM extends ViewModel {
    private MyDatabase mDb;
    private Repository mRepo;
    private LiveData<LoanEntity> mLoan;
    public EditLoanVM(MyDatabase mDb, int mLoanId) {
        this.mDb = mDb;
        mRepo = Repository.getInstance();
        mLoan = mRepo.getLoan(mDb,mLoanId);
    }

    public LiveData<LoanEntity> getmLoan() {
        return mLoan;
    }
}
