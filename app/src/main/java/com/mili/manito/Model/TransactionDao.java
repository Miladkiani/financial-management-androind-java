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
public abstract class TransactionDao {

     @Transaction
     public  void addTransaction(TransactionEntity transactionEntity,
           List<LabelsEntity> labelsEntities)
     {
           long row_id =  insert(transactionEntity);
           if (labelsEntities!=null) {
               for (LabelsEntity labelsEntity : labelsEntities) {
                   insertLabelTran(new LabelTranEntity(labelsEntity.getLabel_id(),
                           (int) row_id));
               }
           }
     }

    @Transaction
    public void updateTransaction(
            TransactionEntity transactionEntity
            ,List<LabelsEntity>labelsEntities){
            update(transactionEntity);
            deleteLabelByTranId(transactionEntity.getTransaction_id());
            if (labelsEntities != null) {
                for (LabelsEntity labelsEntity : labelsEntities) {
                    insertLabelTran(new LabelTranEntity(labelsEntity.getLabel_id(),
                            transactionEntity.getTransaction_id()));
                }
            }
    }

    @Transaction
    public void deleteTransaction (int trans_id){
        deleteTransactionById(trans_id);
    }

    @Query("DELETE  FROM lbl_tran where tran_id = :id ")
    public abstract void deleteLabelByTranId(int id);

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    public abstract void  insertLabelTran(LabelTranEntity... labelTranEntities);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insert (TransactionEntity transactionEntity);


    @Update(onConflict = OnConflictStrategy.IGNORE)
    public abstract void update (TransactionEntity transactionEntity);

    @Query("DELETE FROM transactions")
    public abstract void deleteAll();

    @Query("DELETE FROM transactions where tran_id = :id")
    public abstract void deleteTransactionById(int id);

    @Query("SELECT * FROM transactions where tran_id= :id")
    public abstract LiveData<TransactionEntity> getTransactionById(int id);

    @Query("  SELECT tran_id,tran_date,tran_type,tran_cost,loan_id," +
            "a1.acc_name as acc_name_pay,a2.acc_name as acc_name_get,cate_name,cate_color from transactions t " +
            "left join account a1 on t.acc_id_pay = a1.acc_id " +
            "left join account a2 on t.acc_id_get = a2.acc_id " +
            "left join category on t.cate_id = category.cate_id "+
            "where  " +
            " tran_date between :startTime and :endTime  " +
            "order by tran_date desc ")
   public abstract LiveData<List<TransactionList>> getAllTransactionsByDate( long startTime , long endTime);

    @Query("  SELECT tran_id,tran_date,tran_type,tran_cost,loan_id," +
            "a1.acc_name as acc_name_pay,a2.acc_name as acc_name_get,cate_name,cate_color from transactions t " +
            "left join account a1 on t.acc_id_pay = a1.acc_id " +
            "left join account a2 on t.acc_id_get = a2.acc_id " +
            "left join category on t.cate_id = category.cate_id "+
            "where t.tran_type = :type and tran_date between :startTime and :endTime  " +
            "order by tran_date desc ")
    public abstract LiveData<List<TransactionList>> getAllTransactionsByDateBuy(
             long startTime , long endTime,int type);

    @Query("select tran_type , sum(tran_cost) as tran_cost " +
            "from transactions as t " +
            " where t.tran_date between :startTime and :endTime " +
            "group by tran_type")
    public abstract LiveData<List<TransactionSum>> getSumCostByDate(
            long startTime, long endTime);

    @Query ("SELECT  tran_date, tran_type, total(tran_cost) as sum_pay"+
            " from transactions t" +
            " where tran_date between :startTime and :endTime" +
            " group by strftime('%Y-%m-%d',tran_date/1000,'unixepoch') , tran_type" +
            " order by tran_date asc")
    public abstract Cursor getSumByDay( Long startTime, Long endTime);


    @Query ("SELECT   tran_type ,count(tran_id) as count_tran, " +
            "total(tran_cost) as tran_cost, avg(tran_cost) as tran_avg" +
            " from transactions t" +
            " where tran_date between :startTime and :endTime" +
            " group by tran_type" )
    public abstract Cursor getReportByDay( Long startTime, Long endTime);


}
