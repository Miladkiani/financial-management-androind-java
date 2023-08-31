package com.mili.manito.Model;

import android.database.Cursor;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

@Dao
public abstract class BudgetDao {

    @Transaction
    public void insertBudget(BudgetEntity budgetEntity , List<CategoryEntity> categoryEntities){
        long id = insert(budgetEntity);
        for (CategoryEntity c : categoryEntities){
            insertBudgetCate(new
                    BudgetCategoryEntity((int)id,c.getCategory_id(),c.getCategory_cost()));
        }
    }


    @Transaction
    public void updateBudget(BudgetEntity budgetEntity , List<CategoryEntity> categoryEntities){
        int id = budgetEntity.getBudget_id();
        update(budgetEntity);
        removeBudsCate(id);
        for (CategoryEntity c : categoryEntities){
            insertBudgetCate(new
                    BudgetCategoryEntity(budgetEntity.getBudget_id(),c.getCategory_id(),c.getCategory_cost()));
        }
    }

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract long insert (BudgetEntity budgetEntity);

    @Update(onConflict = OnConflictStrategy.ABORT)
    public abstract void update (BudgetEntity budgetEntity);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract void insertBudgetCate(BudgetCategoryEntity budgetCategoryEntity);

    @Query("delete from budget_category where bud_id = :budId")
    public abstract void removeBudsCate(int budId);

    @Query("delete from budget where bud_id = :budId")
    public abstract void removeBudById(int budId);

    @Query("SELECT * From budget where user_id = :userId")
    public abstract LiveData<List<BudgetEntity>> getBudgetsByUser(int userId);

    @Query("select  bud_cost as budMax , bud_start_time as budSDate , bud_end_time as budEDate, " +
            " budget_category.cate_id as cateId , bud_title as budTitle , " +
            "cate_name as budCate , cate_color as budCateColor , cost budCateCost  from budget " +
            "inner join budget_category on budget.bud_id = budget_category.bud_id " +
            "inner join category on category.cate_id = budget_category.cate_id " +
            " where budget.bud_id = :budId "
            )
    public abstract LiveData<List<BudgetDetailList>> getBudgetConditionById(int budId);

    @Query("select distinct * from budget where budget.bud_id = :budId")
    public abstract LiveData<BudgetEntity> getBudgetById(int budId);

    @Query("select  sum(tran_cost) as cost from transactions " +
            "where (tran_type = 11 or tran_type = 44) and tran_date between :sDate and :eDate and cate_id = :cateId "
    )
    public abstract Long getBudgetPaidById(int cateId, Long sDate , Long eDate );

    @Query("select category.cate_id,cate_name,cate_color,budget_category.cost as cate_cost from category" +
            " inner join budget_category on category.cate_id = budget_category.cate_id " +
            "inner join budget on budget.bud_id = budget_category.bud_id " +
            "where budget.bud_id = :budID")
    public abstract Cursor getBudgetsCateById(int budID);

}
