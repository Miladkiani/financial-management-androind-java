package com.mili.manito.ViewModel;

import android.app.Application;

import com.mili.manito.Model.AccountList;
import com.mili.manito.Model.LoanList;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Repo.Repository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class PayLoanVM extends AndroidViewModel {

    private LiveData<List<AccountList>> accounts;
    private LiveData<LoanList> loan;
    private MutableLiveData<Integer> mLoanId  = new MutableLiveData<>();

    private MutableLiveData<Integer> mUserId  = new MutableLiveData<>();

    public PayLoanVM(@NonNull Application application) {
        super(application);
        MyDatabase db = MyDatabase.getInstance(application.getApplicationContext());
        Repository repo = Repository.getInstance();
        accounts = Transformations.switchMap(mUserId,
                a->repo.getAccountsByCondition(db,a,-1));
        loan = Transformations.switchMap(mLoanId,
                j -> repo.getLoanByPaid(db, j));

    }

    public LiveData<List<AccountList>> getAccounts() {
        return accounts;
    }

    public LiveData<LoanList> getLoan() {
        return loan;
    }

    public void setLoanId(Integer id) {
        this.mLoanId.setValue(id);
    }

    public void setUserId(Integer id) {
        this.mUserId.setValue(id);
    }

}
