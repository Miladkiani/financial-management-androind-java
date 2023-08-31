package com.mili.manito.Model;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AccountDao {

    @Query("SELECT * from account WHERE account.acc_id = :accountId LIMIT 1 " )
     LiveData<AccountEntity> getAccountById(int accountId);

    @Query("select a.acc_id, a.acc_name , a.acc_condition , a.acc_pri_balance, " +
            "coalesce(tdebit.debit, 0) - coalesce(tcredit.credit, 0) + coalesce(a.acc_pri_balance,0) as account_balance" +
            " , user_id " +
            "from account a left join" +
            " (select t.acc_id_get, sum(t.tran_cost) as debit " +
            "    from transactions t group by t.acc_id_get ) tdebit " +
            "    on a.acc_id = tdebit.acc_id_get left join " +
            "  (select t.acc_id_pay, sum(t.tran_cost) as credit " +
            "     from transactions t " +
            "      group by t.acc_id_pay " +
            "    ) tcredit " +
            "     on a.acc_id = tcredit.acc_id_pay " +
            " where acc_condition = :condition and user_id = :userId" +
            " order by acc_name")
    LiveData<List<AccountList>> getAllAccountsByCondition(int userId,int condition);

    @Query("select a.acc_id, a.acc_name , a.acc_condition,  a.acc_pri_balance," +
            "coalesce(tdebit.debit, 0) - coalesce(tcredit.credit, 0) + coalesce(a.acc_pri_balance,0) as account_balance , user_id " +
            "from account a left join" +
            " (select t.acc_id_get, sum(t.tran_cost) as debit " +
            "    from transactions t group by t.acc_id_get ) tdebit " +
            "    on a.acc_id = tdebit.acc_id_get left join " +
            "  (select t.acc_id_pay, sum(t.tran_cost) as credit " +
            "     from transactions t " +
            "      group by t.acc_id_pay " +
            "    ) tcredit " +
            "     on a.acc_id = tcredit.acc_id_pay " +
            " where user_id = :userId" +
            " order by acc_name")
    LiveData<List<AccountList>> getAllAccountsByUser(int userId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(AccountEntity... accountEntities);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(AccountEntity accountEntity);

    @Query(" DELETE FROM account WHERE acc_id = :accountId")
    void deleteAccount(int accountId);

    @Query("DELETE FROM account")
    void deleteAll();

}
