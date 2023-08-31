package com.mili.manito;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Model.TransactionEntity;
import com.mili.manito.Utilities.DateUtil;
import com.mili.manito.Utilities.PersianDigitConverter;
import com.mili.manito.Utilities.PrefManager;
import com.mili.manito.ViewModel.ReportVM;
import com.mili.manito.ViewModel.ReportVMF;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFrag extends Fragment {

    private LineChart chart;
    private TextView show_date, next_date,prev_date
            ,sum_get,count_get,sum_pay,count_pay,avg_get,avg_pay;
    private MyDatabase sDb;
    private ReportVM viewModel;
    private Long[] interval = new Long[2];

    private SharedPreferences preferences;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener
            = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            if (key.equals(PrefManager.UNIT_PREF_ITEM)){
                setupViewModel();
            }
        }
    };

    public ReportFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootV = inflater.inflate(R.layout.fr_report, container, false);
        chart = rootV.findViewById(R.id.chart);
        sum_get = rootV.findViewById(R.id.sum_get);
        sum_pay = rootV.findViewById(R.id.sum_pay);
        count_get = rootV.findViewById(R.id.count_get);
        count_pay = rootV.findViewById(R.id.count_pay);
        show_date = rootV.findViewById(R.id.show_date_txt);
        next_date = rootV.findViewById(R.id.after_date_txt);
        prev_date = rootV.findViewById(R.id.prev_date_txt);
        avg_get = rootV.findViewById(R.id.avg_get);
        avg_pay =rootV.findViewById(R.id.avg_pay);

        Toolbar toolbar = rootV.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).setTitle(R.string.nav_report);

        next_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDate(35);
            }
        });

        prev_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDate(-15);
            }
        });

        return rootV;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        sDb = MyDatabase.getInstance(getActivity().getApplicationContext());
        ReportVMF factory = new ReportVMF(sDb, 1);
        viewModel =
                ViewModelProviders.of(this, factory).get(ReportVM.class);

        PersianCalendar pc = new PersianCalendar();


        setInterval( pc.getTimeInMillis());


       setupViewModel();
       setupSharedPreferences();

    }


    private void setNothing(){
        sum_get.setText(" ");
        sum_pay.setText(" ");
        avg_get.setText(" ");
        avg_pay.setText(" ");
        count_get.setText(" ");
        count_pay.setText(" ");
    }

    private  void  setInterval( Long longDate) {

        if (longDate != null)

        {
            PersianCalendar pc = new PersianCalendar();
            pc.setTimeInMillis(longDate);
            pc.set(PersianCalendar.HOUR_OF_DAY,0);
            pc.set(PersianCalendar.MINUTE,0);
            pc.set(PersianCalendar.SECOND,0);
            pc.set(PersianCalendar.MILLISECOND,0);
            int year = pc.getPersianYear();
            int month = pc.getPersianMonth();
            pc.setPersianDate(year,month,1);
            interval[0] = pc.getTimeInMillis();
            show_date.setText(DateUtil.toStringMonthly(longDate));

            int lastDayOfMonth =
                    DateUtil.getLastDayOfMonth(year,month);

            pc.setPersianDate(year,month,lastDayOfMonth);
            interval[1] = pc.getTimeInMillis()+DateUtil.MAX_TIME_DAY;

                viewModel.setInterval(interval);
        }

    }

    private ArrayList<Long> getAllInterval(Long[] interval){
        ArrayList<Long> dates = new ArrayList<>();
        PersianCalendar pc = new PersianCalendar();
        Long start = interval[0];
        Long end = interval[1];
        while (start<=end){
            dates.add(start);
            pc.setTimeInMillis(start);

            start = start + (24*60*60*1000);
        }
        return dates;
    }

    private void addDate(int i){
        PersianCalendar pc = new PersianCalendar();
        pc.clear();

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        String date = show_date.getText().toString();
        calendar.setTimeInMillis(DateUtil.StringMonthlyToLong(date));
        pc.setTimeInMillis(DateUtil.StringMonthlyToLong(date));
        calendar.add(Calendar.DATE,i);
        pc.setTimeInMillis(calendar.getTimeInMillis());
        setInterval(calendar.getTimeInMillis());
    }

    private  void setupViewModel(){
        viewModel.getSumCost().observe(this, new Observer<Cursor>() {
            @Override
            public void onChanged(Cursor cursor) {

                ArrayList<Entry> entryP = new ArrayList<>();
                ArrayList<Entry> entryG = new ArrayList<>();
                ArrayList<String> dateName = new ArrayList<>();
                ArrayList<Long> allInterval = getAllInterval(interval);
                ;
                for (int j = 0; j<allInterval.size();j++){
                    entryP.add(new Entry(j,0));
                    entryG.add(new Entry(j,0));
                    dateName.add(DateUtil.toStringDaily(allInterval.get(j)));
                }

                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            Long date = cursor.getLong(0);
                            int buy = cursor.getInt(1);
                            long cost = cursor.getLong(2);
                            if (PrefManager.getUnitPref().equals(PrefManager.TOOMAN_UNIT_VALUE)){
                                cost = cost/10;
                            }
                            for (int l = 0; l<allInterval.size();l++) {
                                if (date >= allInterval.get(l) &&
                                        date<=(allInterval.get(l)+(DateUtil.MAX_TIME_DAY))
                                )
                                {
                                    if (buy == TransactionEntity.TRANSACTION_PAY ||
                                            buy == TransactionEntity.TRANSACTION_LOAN){
                                        entryP.set(l,new Entry(l,cost));

                                    }else if (buy == TransactionEntity.TRANSACTION_GET){
                                        entryG.set(l,new Entry(l,cost));
                                    }
                                    break;
                                }
                            }

                        } while (cursor.moveToNext());

                    }

                }
                List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

                LineDataSet getDataSet  = new LineDataSet(entryG, "درآمد");
                getDataSet.setCircleColor(Color.parseColor("#000000"));
                getDataSet.setColor(Color.parseColor("#03A9F4"));
                dataSets.add(getDataSet);

                LineDataSet payDataSet = new LineDataSet(entryP, "هزینه");
                payDataSet.setColor(Color.parseColor("#F44336"));
                payDataSet.setCircleColor(Color.parseColor("#000000"));
                dataSets.add(payDataSet);
                //****
                // Controlling X axis
                XAxis xAxis = chart.getXAxis();
                // Set the xAxis position to bottom. Default is top
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                //Customizing x axis value
                xAxis.setLabelRotationAngle(-93f);
                xAxis.setTextColor(Color.parseColor("#FF5722"));
                xAxis.setAxisMinimum(0);


                // xAxis.setDrawAxisLine(true);
                // xAxis.setDrawGridLines(false);


                xAxis.setValueFormatter(new IndexAxisValueFormatter(dateName));

                //***
                // Controlling right side of y axis
                YAxis yAxisRight = chart.getAxisRight();
                yAxisRight.setEnabled(false);

                //***
                // Controlling left side of y axis
                YAxis yAxisLeft = chart.getAxisLeft();

                yAxisLeft.setAxisMinimum(0);

                yAxisLeft.setDrawZeroLine(false); // draw a zero line
                yAxisLeft.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return
                                PersianDigitConverter.PersianNumber(
                                        PersianDigitConverter.NumberFormat(value))+
                                        " "+PrefManager.getUnitPref();
                    }
                });

                // Setting Data
                LineData data = new LineData(dataSets);
                data.setDrawValues(false);
                chart.setData(data);
                chart.setDragEnabled(true);
                chart.setBackgroundColor(Color.WHITE);
                chart.animateX(1000);
                chart.setDescription(null);

                //refresh
                chart.invalidate();
            }
        });

        viewModel.getReportAll().observe(this, new Observer<Cursor>() {
            @Override
            public void onChanged(Cursor cursor) {
                setNothing();
                if (cursor!=null){
                    if (cursor.moveToFirst()){
                        do {
                            int buy = cursor.getInt(0);
                            int count= cursor.getInt(1);

                            Long sum= cursor.getLong(2);
                            float avg = cursor.getFloat(3);

                            if (PrefManager.getUnitPref().equals(PrefManager.TOOMAN_UNIT_VALUE)) {
                                sum = sum/10;
                                avg = avg/10;
                            }

                            if (buy == TransactionEntity.TRANSACTION_PAY ||
                                    buy == TransactionEntity.TRANSACTION_LOAN){

                                count_pay.setText(
                                        PersianDigitConverter.PersianNumber(
                                                Integer.toString(count))
                                );
                                sum_pay.setText(PersianDigitConverter.PersianNumber(
                                        PersianDigitConverter.NumberFormat(sum)
                                ).concat(" ").concat(PrefManager.getUnitPref()));

                                avg_pay.setText(PersianDigitConverter.PersianNumber(
                                        PersianDigitConverter.NumberFormat(avg).concat(" "))
                                        .concat(PrefManager.getUnitPref()));

                            }else if (buy == TransactionEntity.TRANSACTION_GET){
                                count_get.setText(
                                        PersianDigitConverter.PersianNumber(
                                                Integer.toString(count))
                                );
                                sum_get.setText(PersianDigitConverter.PersianNumber(
                                        PersianDigitConverter.NumberFormat(sum)
                                ).concat(" ").concat(PrefManager.getUnitPref()));
                                avg_get.setText(PersianDigitConverter.PersianNumber(
                                        PersianDigitConverter.NumberFormat(avg)
                                ).concat(" ").concat(PrefManager.getUnitPref()));

                            }
                        }while (cursor.moveToNext());
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        preferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    private void setupSharedPreferences(){
        preferences = PrefManager.getSharedPreferences();
        preferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.app_bar_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.tags_item_menu:
                Intent intent = new Intent(getActivity(),TagsActivity.class);
                startActivity(intent);
                return true;
            case R.id.settings_item_menu:
                Intent intent2 = new Intent(getActivity(),SettingsActivity.class);
                startActivity(intent2);
                return true;
            case R.id.about_item_menu:
                Intent intent3 = new Intent(getActivity(),AboutActivity.class);
                startActivity(intent3);
                return true;
        }
        return false;
    }

}