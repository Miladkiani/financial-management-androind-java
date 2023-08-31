package com.mili.manito.Model;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM category order by cate_id")
    LiveData<List<CategoryEntity>> getAllCategory();

    @Query("SELECT * FROM CATEGORY WHERE cate_id = :categoryId")
    LiveData<CategoryEntity> getCategoryById(int categoryId);

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insert (CategoryEntity... categoryEntities);

    @Update (onConflict = OnConflictStrategy.IGNORE)
    void update (CategoryEntity categoryEntity);

    @Query("DELETE FROM category")
    void deleteAll();
}
