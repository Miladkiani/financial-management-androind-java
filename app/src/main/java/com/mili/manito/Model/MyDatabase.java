package com.mili.manito.Model;

import android.content.Context;
import android.os.AsyncTask;


import com.mili.manito.Utilities.DateConverter;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities =
        {UserEntity.class,AccountEntity.class,
        CategoryEntity.class,TransactionEntity.class,
        LoanEntity.class, LabelsEntity.class,
        BudgetEntity.class,LabelTranEntity.class, BudgetCategoryEntity.class},
        version = 1,exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class MyDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "hesabketab_db";
    private static final Object LOCK = new Object();
    private static MyDatabase sInstance ;

    public static MyDatabase getInstance(Context context){
        if(sInstance==null){
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MyDatabase.class,DATABASE_NAME)
                         .allowMainThreadQueries()
                        .addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                            new PopulateDbAsync(sInstance).execute();
                            }
                        })
                        .build();
            }
        }
        return sInstance;
        }

        public abstract UserDao userDao();
        public abstract LabelsDao labelsDao();
        public abstract CategoryDao categoryDao();
        public abstract AccountDao accountDao();
        public abstract TransactionDao transactionDao();
        public abstract BudgetDao budgetDao();
        public abstract LoanDao loanDao();

        private static class PopulateDbAsync extends AsyncTask<Void,Void,Void>{

        private final UserDao userDao;
        private final AccountDao accountDao;
        private final CategoryDao categoryDao;
        private final LabelsDao labelsDao;

            public PopulateDbAsync(MyDatabase instance) {
                this.userDao = instance.userDao();
                this.accountDao = instance.accountDao();
                this.categoryDao = instance.categoryDao();
                this.labelsDao = instance.labelsDao();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                userDao.deleteAll();
                accountDao.deleteAll();
                categoryDao.deleteAll();
                labelsDao.deleteAll();

                UserEntity userEntity = new UserEntity("admin","123");
                int userId= (int)userDao.insert(userEntity);
                accountDao.insert(new AccountEntity(
                        "نقد",
                        1,0L,
                        userId));
                labelsDao.insert(new LabelsEntity("شارژ موبایل",userId,"#2196F3"),
                        new LabelsEntity("بنزین",userId,"#E91E63"),
                        new LabelsEntity("تعمیرات",userId,"#795548"),
                        new LabelsEntity("میوه",userId,"#CDDC39"),
                        new LabelsEntity("شهریه",userId,"#9C27B0"),
                        new LabelsEntity("حقوق",userId,"#4CAF50"),
                        new LabelsEntity("کرایه",userId,"#FF5722"));
                        new LabelsEntity("قبض",userId,"#F44336");
                categoryDao.insert(CategoryEntity.populateDefaultCategory());
                labelsDao.insert();
                return null ;
            }
        }
    }

