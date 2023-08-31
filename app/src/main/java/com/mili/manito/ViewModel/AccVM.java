package com.mili.manito.ViewModel;


import android.util.Log;

import com.mili.manito.Model.AccountList;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Repo.Repository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class AccVM extends ViewModel {
    private LiveData<List<AccountList>> mAccounts ;
    private MutableLiveData<Integer> mCondition = new MutableLiveData<Integer>();
    private MyDatabase mDb;
    private Repository mRepository;

    public AccVM(MyDatabase db , int userId) {
        mDb = db;
        mRepository = Repository.getInstance();
        mAccounts = Transformations.switchMap(mCondition,
                c -> mRepository.getAccountsByCondition(db,userId,c));

    }

    public void setCondition(int condition){
        mCondition.setValue(condition);
    }


    public LiveData<List<AccountList>> getAccounts() {
        return mAccounts;
    }
}
