package com.mili.manito.Model;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface LabelsDao {

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insert(LabelsEntity... labelsEntities);

    @Update (onConflict = OnConflictStrategy.IGNORE)
    void update(LabelsEntity... labelsEntities);

    @Query("DELETE FROM labels where lbl_id = :lblId")
    void deleteLabel(int lblId);

    @Query("DELETE FROM labels")
    void deleteAll();

    @Query("SELECT * FROM labels where user_id = :userId order by lbl_id DESC ")
    LiveData<List<LabelsEntity>> getLblByUser(int userId);

    @Query("SELECT labels.lbl_color,labels.lbl_name,labels.lbl_id,labels.user_id FROM labels inner join lbl_tran on " +
            "labels.lbl_id = lbl_tran.lbl_id where tran_id = :tranId " +
            "Order by lbl_name ")
    List<LabelsEntity> getLblByTran(int tranId);

    @Query("SELECT distinct * FROM labels where lbl_id = :lblId ")
    LiveData<LabelsEntity> getlblByTagId(int lblId);

}
