package com.mili.manito.ViewModel;

import com.mili.manito.Model.BudgetEntity;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Repo.Repository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class BudVM extends ViewModel {
    private MyDatabase sDb;
    private int sUserId;
    private Repository mRepo;
    private LiveData<List<BudgetEntity>> budgets;
    public BudVM(MyDatabase sDb, int sUserId) {
        this.sDb = sDb;
        this.sUserId = sUserId;
        mRepo = Repository.getInstance();
        budgets = mRepo.getBudgets(sDb,sUserId);
    }

    public LiveData<List<BudgetEntity>> getBudgets() {
        return budgets;
    }
}
