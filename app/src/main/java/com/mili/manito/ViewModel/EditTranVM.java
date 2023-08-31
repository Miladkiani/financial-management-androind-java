package com.mili.manito.ViewModel;

import android.app.Application;

import com.mili.manito.Model.AccountEntity;
import com.mili.manito.Model.AccountList;
import com.mili.manito.Model.CategoryEntity;
import com.mili.manito.Model.LabelsEntity;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Model.TransactionEntity;
import com.mili.manito.Repo.Repository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class EditTranVM extends AndroidViewModel {
    private LiveData<List<CategoryEntity>> categories;
    private LiveData<List<AccountList>> accounts;
    private LiveData<TransactionEntity> transaction;
    private LiveData<List<LabelsEntity>> labels;
    private MutableLiveData<Integer> mTranId = new MutableLiveData<>();
    private MutableLiveData<Integer> mUserId = new MutableLiveData<>();

    public EditTranVM(@NonNull Application application) {
        super(application);
        MyDatabase db = MyDatabase.getInstance(application.getApplicationContext());
        Repository repo = Repository.getInstance();
        categories =repo.getCategories(db)  ;
        accounts = repo.getAccountsByCondition(db,1,AccountEntity.ACCOUNT_ACTIVE);
        transaction = Transformations.switchMap(mTranId,
                i->repo.getTransaction(db,i));
        labels = Transformations.map(transaction, new Function<TransactionEntity, List<LabelsEntity>>() {
            @Override
            public List<LabelsEntity> apply(TransactionEntity input) {

                return db.labelsDao().getLblByTran(input.getTransaction_id());
            }
        });

    }



    public LiveData<List<CategoryEntity>> getCategories() {
        return categories;
    }

    public LiveData<List<AccountList>> getAccounts() {
        return accounts;
    }

    public LiveData<TransactionEntity> getTransaction(){

        return this.transaction;
    }

    public void setTranId(int tranId) {
        this.mTranId.setValue(tranId);
    }

    public void setUserId(int userId) {
        this.mUserId.setValue(userId);
    }

    public LiveData<List<LabelsEntity>> getLabels() {
        return labels;
    }

}
