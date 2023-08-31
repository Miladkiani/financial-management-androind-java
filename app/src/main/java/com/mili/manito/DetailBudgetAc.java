package com.mili.manito;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.mili.manito.Adapter.BudgetDetailListAdapter;
import com.mili.manito.Model.BudgetDetailList;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Utilities.MyItemDecoration;
import com.mili.manito.Utilities.PersianDigitConverter;
import com.mili.manito.Utilities.PrefManager;
import com.mili.manito.ViewModel.DetailBudVM;
import com.mili.manito.ViewModel.DetailBudVMF;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class DetailBudgetAc extends AppCompatActivity {


    private final static String INSTANCE_BUDGET_ID = "myBud_id";
    private final int DEFAULT_BUDGET_ID = -1;
    private int mBudgetId = DEFAULT_BUDGET_ID;
    private BarChart barChart;
    private DetailBudVM viewModel;
    private BudgetDetailListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_budget);
        initView();
        if (savedInstanceState!= null && savedInstanceState.containsKey(INSTANCE_BUDGET_ID)){
            mBudgetId = savedInstanceState.getInt(INSTANCE_BUDGET_ID,DEFAULT_BUDGET_ID);
        }

        if (mBudgetId == DEFAULT_BUDGET_ID ){
            Intent intent = getIntent();

            if (intent.hasExtra(INSTANCE_BUDGET_ID)){
                Bundle b = intent.getExtras();
                if (b != null) {
                    if (b.containsKey(INSTANCE_BUDGET_ID)) {
                        mBudgetId = b.getInt(INSTANCE_BUDGET_ID);
                    }
                }
            }

        }

        setupViewModel(mBudgetId);

    }

    private void initView(){
        Toolbar toolbar = findViewById(R.id.toolbar_budget_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        RecyclerView  recyclerView = findViewById(R.id.budget_detail_recycler);
        barChart = findViewById(R.id.barChart);
        adapter = new BudgetDetailListAdapter(this,new ArrayList<>());
        RecyclerView.LayoutManager mLayoutManager = new
                LinearLayoutManager(getApplicationContext());
        recyclerView.addItemDecoration(new MyItemDecoration(10));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home){
            finish();
            return true;
        }
        return false;
    }

    private void setupViewModel(int budId){

        MyDatabase sDb = MyDatabase.getInstance(getApplicationContext());
        DetailBudVMF factory = new DetailBudVMF(sDb,budId);

        viewModel = ViewModelProviders.of(this,factory)
                .get(DetailBudVM.class);
        viewModel.getBudget().observe(this, new Observer<List<BudgetDetailList>>() {
            @Override
            public void onChanged(List<BudgetDetailList> budgetDetailLists) {
                viewModel.getBudget().removeObserver(this);
                adapter.setBudgetDetails(budgetDetailLists);
                setChart();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void setChart(){
        ArrayList<BarEntry> entryC = new ArrayList<>();
        ArrayList<BarEntry> entryS = new ArrayList<>();
        ArrayList<String> cateName = new ArrayList<>();
        for (int i =0;i<adapter.getBudgetDetails().size(); i++){
            Long budget = adapter.getBudgetDetails().get(i).getBudCateCost();
            Long paid = adapter.getBudgetDetails().get(i).getBudPaid();

            if (PrefManager.getUnitPref().equals(PrefManager.TOOMAN_UNIT_VALUE)){
                budget = budget/10;
                paid = paid/10;
            }

            entryC.add(new BarEntry(i,budget));
            entryS.add(new BarEntry(i,paid));
            cateName.add(adapter.getBudgetDetails().get(i).getBudCate());
        }



        BarDataSet budgetGroup = new BarDataSet(entryC, "بودجه");
        budgetGroup.setColor(Color.parseColor("#4CAF50"));
        BarDataSet payGroup = new BarDataSet(entryS, "هزینه");
        payGroup.setColor(Color.parseColor("#FFEB3B"));

        XAxis xAxis = barChart.getXAxis();
        // Set the xAxis position to bottom. Default is top
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setTextColor(Color.parseColor("#E91E63"));
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(6);
        xAxis.setLabelRotationAngle(-93f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(cateName));

        barChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return PersianDigitConverter.PersianNumber(
                        PersianDigitConverter.NumberFormat(value))+
                        " "+PrefManager.getUnitPref();
            }
        });

        float groupSpace = 0.4f;
        float barSpace = 0f; // x2 dataset
        float barWidth = 0.3f; // x2 dataset
        // (0.4 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"
        //(barWidth+barSpace) *numberOfBar + groupSapace = 1.00

        BarData data = new BarData(budgetGroup, payGroup);
        data.setBarWidth(barWidth); // set the width of each bar
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return PersianDigitConverter.PersianNumber(
                        format((long)value));
            }
    });
        barChart.setData(data);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum
                (0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * entryC.size());
        barChart.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping
        barChart.setDescription(null);
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);
        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setYOffset(5f);
        l.setXOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(11f);
        barChart.animateY(1000);
        barChart.invalidate(); // refresh

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(INSTANCE_BUDGET_ID,mBudgetId);
    }
    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return PersianDigitConverter.NumberFormat(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

}
