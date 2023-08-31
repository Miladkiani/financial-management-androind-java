package com.mili.manito.Model;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface LoanDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(LoanEntity loanEntity);

    @Query("DELETE FROM loan where loan_id = :id")
    void deleteById(int id);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(LoanEntity loanEntity);

    @Query("select loan.loan_id, loan.loan_title, loan.loan_ins_cost ,loan_ins_count," +
            "loan.loan_start_time,loan.loan_end_time," +
            "total(transactions.tran_cost) as loan_paid" +
            " from loan left join transactions on loan.loan_id = transactions.loan_id" +
            " where loan.user_id= :id" +
            " group by loan.loan_id")
    LiveData<List<LoanList>> getLoansByUser(int id);

    @Query("select loan.loan_id, loan.loan_title, loan.loan_ins_cost ,loan_ins_count," +
            "loan.loan_start_time,loan.loan_end_time," +
            "total(transactions.tran_cost) as loan_paid" +
            " from loan left join transactions on loan.loan_id = transactions.loan_id" +
            " where loan.loan_id= :id" )
    LiveData<LoanList> getLoanByPaid(int id);

    @Query("select * from loan where loan_id = :id")
    LiveData<LoanEntity> getLoanById(int id);

}
