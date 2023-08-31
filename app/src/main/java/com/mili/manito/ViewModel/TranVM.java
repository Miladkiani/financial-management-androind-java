package com.mili.manito.ViewModel;

import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Model.TransactionList;
import com.mili.manito.Model.TransactionSum;
import com.mili.manito.Repo.Repository;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.List;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class TranVM extends ViewModel {

    private LiveData<List<TransactionList>> mainTransaction;
    private LiveData<List<TransactionSum>> transaction_sum;
    private LiveData<String>  stringDate;
    private MutableLiveData<PersianCalendar> time = new MutableLiveData<>();
    private MyDatabase sDb;
    private Repository mRepo;
    private int mUserId;
    private int mTranBuy;

    private final static String LOG_TAG = TranVM.class.getSimpleName();

    public TranVM(MyDatabase db, int user_id, int tranBuy) {
        this.sDb = db;
        this.mUserId = user_id;
        mRepo = Repository.getInstance();
        mTranBuy = tranBuy;
        LiveData<List<TransactionList>>    transactions = Transformations.switchMap(time,
                t -> mRepo.getTransactions(sDb, t,mTranBuy));
        mainTransaction = Transformations.map(transactions,
                new Function<List<TransactionList>, List<TransactionList>>() {
            @Override
            public List<TransactionList> apply(List<TransactionList> input) {

                for (TransactionList t:input){
                    t.setTags(sDb.labelsDao().getLblByTran(t.getTransaction_id()));
                }
                return input;
            }
        });
        transaction_sum = Transformations.switchMap(time,
                z-> mRepo.getTransactionSum(sDb,z));
        stringDate = Transformations.switchMap(time,
                x-> mRepo.getStringTime(x));

    }

    public void setTime(PersianCalendar time){

        this.time.setValue(time);
    }


    public LiveData <List<TransactionSum>> getTransaction_sum() {
        return transaction_sum;
    }

    public LiveData<String> getStringDate() {
        return stringDate;
    }

    public LiveData<List<TransactionList>> getMainTransaction() {
        return mainTransaction;
    }

    public void setmTranBuy(int mTranBuy) {
        this.mTranBuy = mTranBuy;
    }
}
