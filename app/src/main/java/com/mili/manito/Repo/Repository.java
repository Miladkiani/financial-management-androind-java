package com.mili.manito.Repo;

import android.database.Cursor;

import com.mili.manito.Model.AccountEntity;
import com.mili.manito.Model.AccountList;
import com.mili.manito.Model.BudgetEntity;
import com.mili.manito.Model.CategoryEntity;
import com.mili.manito.Model.LabelsEntity;
import com.mili.manito.Model.LoanEntity;
import com.mili.manito.Model.LoanList;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Model.TransactionEntity;
import com.mili.manito.Model.TransactionList;
import com.mili.manito.Model.TransactionSum;
import com.mili.manito.Thread.AppExecutors;
import com.mili.manito.Utilities.DateUtil;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class Repository {
    private static Repository INSTANCE = null;

    public static synchronized Repository getInstance(){
        if(INSTANCE == null){
            INSTANCE = new Repository();
        }
        return INSTANCE;
    }

        public LiveData<List<TransactionList>> getTransactions
                (MyDatabase db ,  PersianCalendar time,int type)
        {
                    LiveData<List<TransactionList>> data;
                    DateUtil dateUtil = new DateUtil(time);
                    if (type == 0) {
                        data = db.transactionDao().getAllTransactionsByDate( dateUtil.getStartTime(),
                                dateUtil.getEndTime());
                    }else{
                        data = db.transactionDao().getAllTransactionsByDateBuy(dateUtil.getStartTime(),
                                dateUtil.getEndTime(),type);
                    }
            return data;
            }


        public LiveData<TransactionEntity> getTransaction(MyDatabase db , int tranID){
            return db.transactionDao().getTransactionById(tranID);
        }

    public LiveData<LoanEntity> getLoan(MyDatabase db , int loanID){
        return db.loanDao().getLoanById(loanID);
    }

    public LiveData<LoanList> getLoanByPaid(MyDatabase db , int loanID){
        return db.loanDao().getLoanByPaid(loanID);
    }


    public LiveData<List<TransactionSum>> getTransactionSum
            (MyDatabase db , PersianCalendar time)
    {
        DateUtil dateUtil = new DateUtil(time);
        return db.transactionDao().getSumCostByDate(dateUtil.getStartTime(),
                dateUtil.getEndTime());
    }

        public LiveData<String> getStringTime(PersianCalendar date){
            MutableLiveData<String> data = new MutableLiveData<>();
            DateUtil dateUtil = new DateUtil(date);
            data.setValue(dateUtil.getStringDate());
            return data;
        }

    public LiveData<List<AccountList>> getAccountsByCondition(
            final MyDatabase db, final int userId, final int condition) {
        LiveData<List<AccountList>> data;

        if (condition == AccountEntity.ACCOUNT_ACTIVE ||
                condition == AccountEntity.ACCOUNT_BLOCKED) {
            data = db.accountDao().getAllAccountsByCondition(userId, condition);
        } else {
            data = db.accountDao().getAllAccountsByUser(userId);
        }
    return data;
    }



    public LiveData<List<LoanList>> getLoansByUser(MyDatabase db , int userId){
        return db.loanDao().getLoansByUser(userId);
    }

    public LiveData<List<CategoryEntity>> getCategories(MyDatabase db){
        return db.categoryDao().getAllCategory();
    }

   /* public LiveData<Long> getSumOfAccounts(final MyDatabase db, final int userId, final int condition){
        LiveData<Long> data = new MutableLiveData<>();
        if (condition == AccountEntity.ACCOUNT_ACTIVE ||
                condition == AccountEntity.ACCOUNT_BLOCKED) {
            data = db.accountDao().getSumOfAccountsByCondition(userId, condition);
        } else {
            data = db.accountDao().getSumOfAccounts(userId);
        }

        return data;
    }*/


    public LiveData<Cursor> getSumByDay(final MyDatabase db,  Long[] interval){
        final MutableLiveData<Cursor> data = new MutableLiveData<>();
               data.postValue( db.transactionDao().getSumByDay(interval[0],interval[1]));
        return data;
    }

    public LiveData<Cursor> getBudgetsCateById(final MyDatabase db, final int budId){
        final MutableLiveData<Cursor> data = new MutableLiveData<>();
            data.setValue( db.budgetDao().getBudgetsCateById(budId));
        return data;
    }

    public LiveData<Cursor> getReportByDay(final MyDatabase db,  Long[] interval){
        final MutableLiveData<Cursor> data = new MutableLiveData<>();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                data.postValue(
                        db.transactionDao().getReportByDay(interval[0],interval[1])
                );
            }
        });
        return data;
    }


    public LiveData<List<LabelsEntity>> getLabelsByUserId(MyDatabase db,int userId){
                        return  db.labelsDao().getLblByUser(userId);
    }

    public LiveData<LabelsEntity> getLabelsById(MyDatabase db,int tagId){
        return  db.labelsDao().getlblByTagId(tagId);
    }
    public LiveData<List<BudgetEntity>> getBudgets(MyDatabase db, int userId){

       return db.budgetDao().getBudgetsByUser(userId);

    }

}
