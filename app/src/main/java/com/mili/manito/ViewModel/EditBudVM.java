package com.mili.manito.ViewModel;

import android.app.Application;
import android.database.Cursor;

import com.mili.manito.Model.BudgetEntity;
import com.mili.manito.Model.CategoryEntity;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Repo.Repository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class EditBudVM extends AndroidViewModel {

    private MutableLiveData<Integer> budID = new MutableLiveData<>();

    private LiveData<List<CategoryEntity>> mCateList;
    private LiveData<BudgetEntity> mBudget;
    private LiveData<Cursor> mCateCost;

    public EditBudVM(@NonNull Application application) {
        super(application);
         MyDatabase db = MyDatabase.getInstance(application.getApplicationContext());
       Repository repo = Repository.getInstance();
        mCateList = repo.getCategories(db);
        mBudget = Transformations.switchMap(budID,
                b->db.budgetDao().getBudgetById(b));
        mCateCost  = Transformations.switchMap(budID,
                c->repo.getBudgetsCateById(db,c));

    }


    public void setBudID(int budID) {
        this.budID.setValue(budID);
    }


    public LiveData<BudgetEntity> getBudget() {
        return mBudget;
    }

    public LiveData<Cursor> getmCateCost() {
        return mCateCost;
    }

    public LiveData<List<CategoryEntity>>
    getmCateList() {
        return mCateList;
    }
}
