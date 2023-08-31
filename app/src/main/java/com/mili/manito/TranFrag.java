package com.mili.manito;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mili.manito.Adapter.TransactionListAdapter;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Model.TransactionEntity;
import com.mili.manito.Model.TransactionList;
import com.mili.manito.Model.TransactionSum;
import com.mili.manito.Thread.AppExecutors;
import com.mili.manito.Utilities.DateUtil;
import com.mili.manito.Utilities.MyItemDecoration;
import com.mili.manito.Utilities.PersianDigitConverter;
import com.mili.manito.Utilities.PrefManager;
import com.mili.manito.ViewModel.TranVM;
import com.mili.manito.ViewModel.TranVMF;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TranFrag extends Fragment {


    public TranFrag() {
        // Required empty public constructor
    }


    private TextView show_date,
            count_tran, sumGet,sumPay , emptyTxt;
    private Spinner option_spinner;

    private FloatingActionButton add_transaction_btn;
     private  MyDatabase sDB;
    private  TransactionListAdapter transactionListAdapter;
    private RecyclerView recyclerView;
    private int datePrefValue;
    private PersianCalendar request_date;
    private SharedPreferences preferences;
    private TranVM viewModel;
    private DateUtil dateUtil;
   public final static String INSTANCE_TRANSACTION_ID = "trans_id";
    public final static String INSTANCE_TRANSACTION_DATE = "trans_date";

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener
            = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(PrefManager.DATE_PREF_ITEM)) {
                datePrefValue = PrefManager.getIntervalPref();
                viewModel.setTime(request_date);
            }

            if (key.equals(PrefManager.UNIT_PREF_ITEM)){
                viewModel.setTime(request_date);
            }

        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fr_tran, container, false);
        show_date = rootView.findViewById(R.id.show_date_txt);
        TextView  after_date = rootView.findViewById(R.id.after_date_txt);
        TextView  previous_date = rootView.findViewById(R.id.previous_date_txt);
        add_transaction_btn = rootView.findViewById(R.id.add_transaction_fab);
        ImageButton filter_img = rootView.findViewById(R.id.filter_list_imgBtn);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        option_spinner = rootView.findViewById(R.id.tran_option_spn);
        count_tran = rootView.findViewById(R.id.count_tran_txt);
        sumGet = rootView.findViewById(R.id.tran_sumget_txt);
        sumPay = rootView.findViewById(R.id.tran_sumpay_txt);
        emptyTxt = rootView.findViewById(R.id.tran_empty_txt);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).setTitle(R.string.nav_trans);


        add_transaction_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arg = new Bundle();
                arg.putInt(MainActivity.INSTANCE_USER_ID, 1);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("transDialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                DialogFragment dialogFragment = new Add_Tran_Frd();
                dialogFragment.setArguments(arg);
                dialogFragment.show(ft, "transDialog");
            }
        });

        filter_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("filterDialog");
                if (fragment != null) {
                    ft.remove(fragment);
                }
                ft.addToBackStack(null);
                DialogFragment dialogFragment = new Filter_Tran_Frd();
                dialogFragment.show(ft, "filterDialog");
            }
        });

        after_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAfterDate();
            }
        });

        previous_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPreviousDate();
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        request_date = new PersianCalendar();

        if (savedInstanceState != null) {
            request_date.setTimeInMillis(savedInstanceState.getLong(
                    INSTANCE_TRANSACTION_DATE));
        }
        initAll();

    }

    private void initAll() {


        sDB = MyDatabase.getInstance(getActivity().getApplicationContext());

        String[] tran_option = getActivity().getResources().getStringArray(R.array.tran_option);
        ArrayAdapter<String> optionAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.spn_option_item,
                tran_option
        );

        optionAdapter.setDropDownViewResource(R.layout.spn_option_item);
        option_spinner.setAdapter(optionAdapter);

        option_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
                switch (position){
                    case 1:
                        position = 11;
                        break;
                    case 2:position = 22;
                    break;
                    case 3:position = 33;
                    break;
                    default:
                        position = 0;
                }
                viewModel.setmTranBuy(position);
                viewModel.setTime(request_date);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setupSharedPreferences();
        setupViewModel();

        dateUtil = new DateUtil(request_date);

        transactionListAdapter = new TransactionListAdapter(getActivity(), new ArrayList<>(), new TransactionListAdapter.MoreListener() {

            @Override
            public void updateTransfer(int id) {
                Bundle args = new Bundle();
                args.putInt(INSTANCE_TRANSACTION_ID, id);
                args.putInt(MainActivity.INSTANCE_USER_ID, 1);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("transferDialog");
                if (fragment != null) {
                    ft.remove(fragment);
                }
                ft.addToBackStack(null);
                    DialogFragment dialogFragment = new Add_Transfer_Frd();
                    dialogFragment.setArguments(args);
                    dialogFragment.show(ft, "transferDialog");
            }

            @Override
            public void updateTransaction(int id) {
                Bundle args = new Bundle();
                args.putInt(INSTANCE_TRANSACTION_ID, id);
                args.putInt(MainActivity.INSTANCE_USER_ID, 1);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("transDialog");
                if (fragment != null) {
                    ft.remove(fragment);
                }
                ft.addToBackStack(null);

                    DialogFragment dialogFragment = new Add_Tran_Frd();
                    dialogFragment.setArguments(args);
                    dialogFragment.show(ft, "transDialog");

            }

            @Override
            public void removeTransaction(final int tran_id) {

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                            sDB.transactionDao().deleteTransaction(
                                    tran_id);
                    }
                });
            }


        });

        RecyclerView.LayoutManager mLayoutManager = new
                LinearLayoutManager(getActivity().getApplicationContext());

        recyclerView.addItemDecoration(new MyItemDecoration(10));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(transactionListAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && add_transaction_btn.getVisibility() == View.VISIBLE) {
                    add_transaction_btn.hide();
                } else if (dy < 0 && add_transaction_btn.getVisibility() != View.VISIBLE) {
                    add_transaction_btn.show();
                }
            }
        });

    }

    private void setupViewModel() {
        TranVMF viewModelFactory = new TranVMF
                (sDB, 1,option_spinner.getSelectedItemPosition());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get
                (TranVM.class);
        viewModel.setTime(request_date);
        viewModel.getMainTransaction().observe(this, new Observer<List<TransactionList>>() {
            @Override
            public void onChanged(List<TransactionList> transactionLists) {
                transactionListAdapter.setList(transactionLists);
                String tranTitle = getActivity().getResources().getString(R.string.title_tran);
                if (transactionLists.size()!=0){
                     emptyTxt.setVisibility(View.GONE);
                }else{
                    emptyTxt.setVisibility(View.VISIBLE);

                }
                count_tran.setText(PersianDigitConverter.PersianNumber(
                        PersianDigitConverter.NumberFormat(transactionLists.size())).concat(" ").concat(tranTitle));

            }
        });
        viewModel.getTransaction_sum().observe(this, new Observer<
                List<TransactionSum>>() {
            @Override
            public void onChanged(List<TransactionSum> transactionSums) {
                Long sumP = 0L;
                Long sumG = 0L;

             for (TransactionSum t : transactionSums){
                 switch (t.getTran_type()){
                     case TransactionEntity.TRANSACTION_PAY:
                     case TransactionEntity.TRANSACTION_LOAN:
                         sumP+= t.getTran_cost();
                         break;
                     case TransactionEntity.TRANSACTION_GET:
                         sumG+= t.getTran_cost();
                         break;
                 }
             }

                if (PrefManager.getUnitPref().equals(PrefManager.TOOMAN_UNIT_VALUE)){
                    sumP = sumP /10;
                    sumG = sumG /10;
                }
                if (sumP != 0) {
                    sumPay.setText(PersianDigitConverter.PersianNumber(
                            PersianDigitConverter.NumberFormat(sumP))
                            .concat(" ").concat(PrefManager.getUnitPref())
                    );
                }else{
                    sumPay.setText(PersianDigitConverter.PersianNumber(
                            PersianDigitConverter.NumberFormat(sumP))
                    );
                }
                if (sumG != 0) {
                    sumGet.setText(PersianDigitConverter.PersianNumber(
                            PersianDigitConverter.NumberFormat(sumG))
                            .concat(" ").concat(PrefManager.getUnitPref())
                    );
                }else{
                    sumGet.setText(PersianDigitConverter.PersianNumber(
                            PersianDigitConverter.NumberFormat(sumG))
                    );
                }
            }
        });

        viewModel.getStringDate().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                show_date.setText(s);
            }
        });

        //viewModel.get

    }

    private void setPreviousDate(){
         dateUtil.addDateInput(-1);
         request_date = dateUtil.getmDateInput();
        viewModel.setTime(request_date);
    }

    private void setAfterDate(){
        dateUtil.addDateInput(1);
        request_date = dateUtil.getmDateInput();
        viewModel.setTime(request_date);
    }

    private void setupSharedPreferences(){
        preferences = PrefManager.getSharedPreferences();
        datePrefValue = PrefManager.getIntervalPref();
        preferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(INSTANCE_TRANSACTION_DATE,request_date.getTimeInMillis());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        preferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
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
