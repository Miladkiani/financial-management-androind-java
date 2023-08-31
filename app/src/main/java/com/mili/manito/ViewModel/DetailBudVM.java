package com.mili.manito.ViewModel;

import com.mili.manito.Model.BudgetDetailList;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Repo.Repository;

import java.util.List;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class DetailBudVM extends ViewModel {


    private static final String LOG_TAG = DetailBudVM.class.getSimpleName();
    private LiveData<List<BudgetDetailList>> mBudget;
    public DetailBudVM(MyDatabase sDb, int sBudId) {
        Repository  mRepo = Repository.getInstance();
         LiveData<List<BudgetDetailList>>  budget = sDb.budgetDao().getBudgetConditionById(sBudId);
        mBudget = Transformations.map(budget, new Function<List<BudgetDetailList>, List<BudgetDetailList>>() {
            @Override
            public List<BudgetDetailList> apply(List<BudgetDetailList> input) {
                for (BudgetDetailList budgetDetail : input){
                    Long paid = 0L;
                     Long x = sDb.budgetDao().getBudgetPaidById(
                             budgetDetail.getCateId(),budgetDetail.getBudSDate().getTimeInMillis(),
                              budgetDetail.getBudEDate().getTimeInMillis());
                     if (x != null ){
                         paid = x;
                     }
                     budgetDetail.setBudPaid(paid);
                }
                return input;
            }
        });
    }

    public LiveData<List<BudgetDetailList>> getBudget() {
        return mBudget;
    }
}
