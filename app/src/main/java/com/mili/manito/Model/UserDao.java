package com.mili.manito.Model;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE user_name LIKE :username LIMIT 1")
    UserEntity getUserByUsername(String username);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(UserEntity userEntity);

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insert(UserEntity... userEntities);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(UserEntity userEntity);

    @Query("DELETE FROM users")
    void deleteAll();

    @Query("SELECT * FROM users ORDER BY user_name ASC ")
    LiveData<List<UserEntity>> getAllUsers();


}
