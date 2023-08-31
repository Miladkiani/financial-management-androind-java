package com.mili.manito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mili.manito.Adapter.CateCostAdapter;
import com.mili.manito.Adapter.CategorySpinnerAdapter;
import com.mili.manito.Model.BudgetEntity;
import com.mili.manito.Model.CategoryEntity;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Thread.AppExecutors;
import com.mili.manito.Utilities.DateUtil;
import com.mili.manito.Utilities.MyItemDecoration;
import com.mili.manito.Utilities.PersianDigitConverter;
import com.mili.manito.Utilities.PrefManager;
import com.mili.manito.Utilities.TextFormatter;
import com.mili.manito.ViewModel.EditBudVM;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class EditBudgetAc extends AppCompatActivity {

    private EditText budTitle,budMax,budStartDate,budEndDate,budCateCost;

    private Spinner cateSpinner;

    private int mUserID = DEFAULT_ID;
    private int mBudgetId = DEFAULT_ID;
    private CategorySpinnerAdapter cateAdapter;
    private CateCostAdapter cateCostAdapter;
    private  EditBudVM   viewModel;
    private final static int DEFAULT_ID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_budget);
        Toolbar toolbar = findViewById(R.id.tag_toolbar);
        setSupportActionBar(toolbar);
        initView();
        setupViewModel();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState!=null){

            if (savedInstanceState.containsKey(MainActivity.INSTANCE_USER_ID)){
                mUserID = savedInstanceState.getInt(MainActivity.INSTANCE_USER_ID,DEFAULT_ID);
            }

            if (savedInstanceState.containsKey(BudgetFrag.INSTANCE_BUDGET_ID)){
                mBudgetId = savedInstanceState.getInt(BudgetFrag.INSTANCE_BUDGET_ID,DEFAULT_ID);
                viewModel.setBudID(mBudgetId);
            }

        }

        if(mUserID == DEFAULT_ID){
            Bundle bundle = getIntent().getExtras();
            if (bundle!=null && bundle.containsKey(MainActivity.INSTANCE_USER_ID)){
                mUserID = bundle.getInt(MainActivity.INSTANCE_USER_ID,DEFAULT_ID);
            }
        }

        if(mBudgetId == DEFAULT_ID){
            Bundle bundle = getIntent().getExtras();
            if (bundle!=null && bundle.containsKey(BudgetFrag.INSTANCE_BUDGET_ID)){
                mBudgetId = bundle.getInt(BudgetFrag.INSTANCE_BUDGET_ID,DEFAULT_ID);
                viewModel.setBudID(mBudgetId);
            }
        }

    }

    private void setupViewModel(){

      viewModel = ViewModelProviders.of(this).get(EditBudVM.class);
        viewModel.getmCateList().observe(this, new Observer<List<CategoryEntity>>() {
            @Override
            public void onChanged(List<CategoryEntity> categoryEntities) {
                cateAdapter.setData(categoryEntities);
            }
        });
        viewModel.getBudget().observe(this, new Observer<BudgetEntity>() {
            @Override
            public void onChanged(BudgetEntity budgetEntity) {
                if (budgetEntity!=null){
                    String title = budgetEntity.getBudget_title();
                    Long max = budgetEntity.getBudget_cost();
                    if (PrefManager.getUnitPref().equals(PrefManager.TOOMAN_UNIT_VALUE)){
                        max = max/10;
                    }
                    String startTime =
                          DateUtil.toStringStandard(budgetEntity.getBudget_start_time().getTimeInMillis());
                    String endTime =
                            DateUtil.toStringStandard(budgetEntity.getBudget_end_time().getTimeInMillis());
                    budTitle.setText(
                            PersianDigitConverter.PersianNumber(title));
                    budStartDate.setText(startTime);
                    budEndDate.setText(endTime);
                    budMax.setText(Long.toString(max));
               }
            }
        });

        viewModel.getmCateCost().observe(this, new Observer<Cursor>() {
            @Override
            public void onChanged(Cursor cursor) {
                if (cursor!=null)
                    if (cursor.moveToFirst()){
                        do{

                            int cateId = cursor.getInt(cursor.getColumnIndex("cate_id"));
                            String cateName = cursor.getString(cursor.getColumnIndex("cate_name"));
                            String cateColor = cursor.getString(cursor.getColumnIndex("cate_color"));
                            Long cateCost = cursor.getLong(cursor.getColumnIndex("cate_cost"));
                            if (PrefManager.getUnitPref().equals(PrefManager.TOOMAN_UNIT_VALUE)){
                                cateCost= cateCost/10;
                            }
                            cateCostAdapter.addCateCost(new CategoryEntity(cateId,cateCost,cateName,cateColor));
                        }while (cursor.moveToNext());
                    }
            }
        });
    }



    private void initView(){
        budTitle = findViewById(R.id.bud_title_etxt);
        budMax = findViewById(R.id.bud_maxcost_etxt);
        budStartDate = findViewById(R.id.bud_sdate_etxt);
        budEndDate = findViewById(R.id.bud_edate_etxt);
        budCateCost = findViewById(R.id.bud_cate_cost_etxt);

        TextView titleMax = findViewById(R.id.budget_max_title_txt);
        titleMax.setText(getResources().getString(R.string.bud_cost).concat("(").
                concat(PrefManager.getUnitPref()).concat(")"));

        ImageButton    addStartDate = findViewById(R.id.bud_sdate_imgBtn);
        ImageButton addEndDate = findViewById(R.id.bud_edate_imgBtn);
        ImageButton addCateCost = findViewById(R.id.bud_add_imgBtn);
        cateSpinner = findViewById(R.id.bud_cate_spn);
        RecyclerView    recyclerView = findViewById(R.id.edit_bud_recycler);

        cateAdapter = new CategorySpinnerAdapter(EditBudgetAc.this,
                R.layout.spn_list_cate
                , new ArrayList<>());
        cateSpinner.setAdapter(cateAdapter);

        cateCostAdapter = new CateCostAdapter(this,new ArrayList<>());
        RecyclerView.LayoutManager mLayoutManager = new
                LinearLayoutManager(getApplicationContext());
        recyclerView.addItemDecoration(new MyItemDecoration(10));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cateCostAdapter);

        addStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(budStartDate);
            }
        });

        addEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(budEndDate);
            }
        });

        addCateCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCateCost();
            }
        });

        budMax.addTextChangedListener(
                TextFormatter.onTextChangedListener(budMax, new TextFormatter.onChanged() {
                    @Override
                    public void OnTextChanged() {

                    }
                }));
        budCateCost.addTextChangedListener(TextFormatter.onTextChangedListener(budCateCost, new TextFormatter.onChanged() {
            @Override
            public void OnTextChanged() {

            }
        }));

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

        private  void addCateCost() {
            if (budMax.getText().toString().trim().length() != 0) {
                if (budCateCost.getText().toString().trim().length() != 0 &&
                        cateSpinner.getSelectedItem() != null) {
                    String stringMax =
                            budMax.getText().toString().replaceAll(",", "");
                    Long max = Long.parseLong(stringMax);
                    String stringCost =
                            budCateCost.getText().toString().replaceAll(",", "");
                    CategoryEntity categoryEntity =
                            (CategoryEntity) cateSpinner.getSelectedItem();
                    int id = categoryEntity.getCategory_id();
                    String cate_name = categoryEntity.getCategory_name();
                    String color = categoryEntity.getCategory_color();
                    Long cost = Long.parseLong(stringCost);
                    int position = -1;
                    for (int i = 0; i < cateCostAdapter.getmCateCostList().size(); i++) {
                        if (cateCostAdapter.getmCateCostList().get(i).getCategory_id() == id) {
                            position = i;
                            break;
                        }
                    }

                    if (position != -1) {
                        cateCostAdapter.removeCateCost(
                                position);
                    }
                    int message =
                            cateCostAdapter.addCateCost(
                                    new CategoryEntity(id, cost, cate_name, color), max );
                    if (message == -1) {
                        Toast.makeText(this.getApplicationContext(),
                                getResources().getString(R.string.check_budgeting),Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }else{
                Toast.makeText(this.getApplicationContext(),
                        getResources().getString(R.string.first_max),Toast.LENGTH_SHORT)
                        .show();
            }
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_budget_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                finish();
            case R.id.done:
                if (checkInput()) {
                    saveBudget();
                }else{
                    Toast.makeText(this.getApplicationContext(),
                            getResources().getString(R.string.check_budget),Toast.LENGTH_SHORT).show();
                }
            default:
                return false;
        }
    }

    private boolean checkInput(){
        if (budMax.getText().toString().matches("")){
            return false;
        } else if (budStartDate.getText().toString().matches("")){
              return false;
        } else if (budEndDate.getText().toString().matches("")){
            return false;
        }  else if (budTitle.getText().toString().matches("")){
            return false;
        } else if(cateCostAdapter.getmCateCostList().size()==0){
            return false;
        }
        return true;
    }

    private void saveBudget(){
        String title = budTitle.getText().toString();
        String sCost = budMax.getText().toString();
        Long cost = Long.parseLong(sCost.replaceAll(",",""));

        if (PrefManager.getUnitPref().equals(PrefManager.TOOMAN_UNIT_VALUE)){
            cost = cost*10;
            for (CategoryEntity c: cateCostAdapter.getmCateCostList()){
               Long k = c.getCategory_cost();
               k = k*10;
               c.setCategory_cost(k);
            }
        }
        PersianCalendar pcStart = DateUtil.StringToPersianCalendar(budStartDate.getText().toString());
        PersianCalendar pcEnd = DateUtil.StringToPersianCalendar(budEndDate.getText().toString());
        pcEnd.setTimeInMillis(pcEnd.getTimeInMillis()+DateUtil.MAX_TIME_DAY);
        BudgetEntity budgetEntity = new BudgetEntity(title, cost, pcStart, pcEnd, 1);

        if (pcStart.getTimeInMillis()>= pcEnd.getTimeInMillis()){
            Toast.makeText(this.getApplicationContext(),
                    getResources().getString(R.string.check_budget_time),Toast.LENGTH_LONG
                    ).show();
            return;
        }


        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                MyDatabase db = MyDatabase.getInstance(getApplicationContext()) ;

                if (mBudgetId == DEFAULT_ID) {
                    db.budgetDao().insertBudget(
                            budgetEntity,
                            cateCostAdapter.getmCateCostList());
                    finish();
                }else{
                    budgetEntity.setBudget_id(mBudgetId);
                    db.budgetDao().updateBudget
                            (budgetEntity, cateCostAdapter.getmCateCostList());
                    finish();
                }

            }
        });
    }

    private void showDatePickerDialog(EditText editText){
        final PersianCalendar pc = new PersianCalendar();
        int year = pc.getPersianYear();
        int month = pc.getPersianMonth();
        int day = pc.getPersianDay();

        if (!editText.getText().toString().trim().matches("")){
            String[] date = editText.getText().toString().split("/");
            day = Integer.parseInt(date[2]);
            month  = Integer.parseInt(date[1])-1;
            year  = Integer.parseInt(date[0]);
        }
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        DecimalFormat df = new DecimalFormat("00");
                        editText.setText(PersianDigitConverter.PersianNumber(
                                year+"/"+
                                        df.format(monthOfYear+1)+"/" +
                                        df.format(dayOfMonth)));
                    }
                }, year,
                month,
                day);
        datePickerDialog.show(getFragmentManager(), "tpd");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MainActivity.INSTANCE_USER_ID,DEFAULT_ID);
        outState.putInt(BudgetFrag.INSTANCE_BUDGET_ID,DEFAULT_ID);
    }
}


