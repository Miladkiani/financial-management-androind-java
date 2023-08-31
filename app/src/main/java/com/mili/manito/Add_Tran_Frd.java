package com.mili.manito;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;


import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.mili.manito.Adapter.AccountSpinnerAdapter;
import com.mili.manito.Adapter.CategorySpinnerAdapter;
import com.mili.manito.Adapter.TagGridAdapter;
import com.mili.manito.Model.AccountList;
import com.mili.manito.Model.CategoryEntity;
import com.mili.manito.Model.LabelsEntity;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Model.TransactionEntity;
import com.mili.manito.Thread.AppExecutors;
import com.mili.manito.Utilities.DateUtil;
import com.mili.manito.Utilities.PersianDigitConverter;
import com.mili.manito.Utilities.PrefManager;
import com.mili.manito.Utilities.TextFormatter;
import com.mili.manito.ViewModel.EditTranVM;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class Add_Tran_Frd extends DialogFragment {


    private final static String LOG_TAG = Add_Tran_Frd.class.getSimpleName();

    private static final int DEFAULT_ID = -1;
    private Button pay_btn, get_btn;
    private EditText
            mTranTime,
            mTranDate;
    private TextView mTranAccountTitle , mTranTitle;
    private ProgressBar progressBar;
    private ImageButton  tran_accept_imgBtn;

    private EditText mTranCost;
    private Spinner
            tran_acc_spn,
            tran_cate_spn;

    private CategorySpinnerAdapter mCateAdapter;
    private AccountSpinnerAdapter mAccountAdapter;
    private TagGridAdapter mTagsAdapter;

    private int mTranType;

    private int mTranId = DEFAULT_ID;
    private int mLoanId = DEFAULT_ID;
    private static final String INSTANCE_TRAN_TYPE = "tran_type";
    private EditTranVM viewModel;


    public Add_Tran_Frd() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =
                inflater.inflate(R.layout.frd_add_tran,
                        container,
                        false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initView(View rootView) {

        pay_btn = rootView.findViewById(R.id.tran_pay_btn);
        get_btn = rootView.findViewById(R.id.tran_get_btn);
        mTranDate = rootView.findViewById(R.id.tran_date_etxt);
        mTranTime = rootView.findViewById(R.id.tran_time_etxt);
        mTranAccountTitle = rootView.findViewById(R.id.tran_acctitle_txt);
        progressBar = rootView.findViewById(R.id.tran_progress);
        mTranCost = rootView.findViewById(R.id.tran_cost_etxt);
        mTranTitle = rootView.findViewById(R.id.title_add_tran_txt);

        TextView unit = rootView.findViewById(R.id.cost_unit_txt);

        unit.setText(PrefManager.getUnitPref());

        mTranCost.addTextChangedListener(TextFormatter.onTextChangedListener(mTranCost, new TextFormatter.onChanged() {
            @Override
            public void OnTextChanged() {
                changePermission(checkInput());
            }
        }));
        tran_acc_spn = rootView.findViewById(R.id.tran_acc_spn);
        tran_cate_spn = rootView.findViewById(R.id.tran_cate_spn);
        tran_accept_imgBtn = rootView.findViewById(R.id.tran_accept_imBtn);
        ImageButton tran_cancel_imgBtn= rootView.findViewById(R.id.tran_cancel_imgBtn);
        ImageButton add_tag = rootView.findViewById(R.id.add_tag);
        RecyclerView   recyclerView = rootView.findViewById(R.id.recycler_view_tags);

        add_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TagFrag tagFrag = TagFrag.newInstance();
                tagFrag.onButtonPressed(
                        new TagFrag.Listener() {
                                            @Override
                               public void onItemClick(LabelsEntity labelsEntity) {
                                                int find = -1;
                                                for (LabelsEntity e: mTagsAdapter.getList()) {
                                                    if (e.getLabel_id().equals(labelsEntity.getLabel_id())){
                                                        find =1;
                                                    }
                                                }
                                                if (find==-1) {
                                                    mTagsAdapter.setEntity(labelsEntity);
                                                }
                                            }
                                        });
                        tagFrag.show(getActivity().getSupportFragmentManager(), "add_tag_dialog");
            }
        });
        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTransaction_Type(TransactionEntity.TRANSACTION_PAY);
                changePermission(checkInput());
            }
        });
        get_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTransaction_Type(TransactionEntity.TRANSACTION_GET);
                changePermission(checkInput());
            }
        });
        tran_accept_imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    saveTransaction();
                    dismiss();
            }
        });
        tran_cancel_imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mTranDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        mTranTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        tran_acc_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changePermission(checkInput());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tran_cate_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changePermission(checkInput());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mTagsAdapter = new TagGridAdapter(getActivity(), new ArrayList<>());

        // Create the FlexboxLayoutMananger, only flexbox library version 0.3.0 or higher support.
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(
                getActivity().getApplicationContext());
        // Set flex direction.
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        // Set JustifyContent.
        flexboxLayoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);
        recyclerView.setLayoutManager(flexboxLayoutManager);
        recyclerView.setAdapter(mTagsAdapter);
        }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAccountAdapter = new AccountSpinnerAdapter(getActivity(),
                R.layout.spn_list_acc
                , new ArrayList<>());
        tran_acc_spn.setAdapter(mAccountAdapter);

        mCateAdapter = new CategorySpinnerAdapter(getActivity(),
                R.layout.spn_list_cate
                , new ArrayList<>());
        tran_cate_spn.setAdapter(mCateAdapter);


        PersianCalendar pc = new PersianCalendar();
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = calendar.getTimeZone();
        pc.setTimeZone(tz);
        setDateTime(pc);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(TranFrag.INSTANCE_TRANSACTION_ID)) {
                mTranId = savedInstanceState.getInt(
                        TranFrag.INSTANCE_TRANSACTION_ID,
                        DEFAULT_ID);
                if (mTranId!= DEFAULT_ID) {
                    mTranTitle.setText(getResources().getString(R.string.edit_tran));
                }
            }

            if ( savedInstanceState.containsKey(INSTANCE_TRAN_TYPE)) {
                setTransaction_Type(savedInstanceState.getInt(INSTANCE_TRAN_TYPE)
                );
            }

        }else {
            setTransaction_Type(TransactionEntity.TRANSACTION_PAY);
        }




        if (mTranId == DEFAULT_ID) {
            if (getArguments() != null &&
                    getArguments().containsKey(TranFrag.INSTANCE_TRANSACTION_ID)) {
                mTranId = getArguments().getInt(TranFrag.INSTANCE_TRANSACTION_ID,
                        DEFAULT_ID);

                mTranTitle.setText(getResources().getString(R.string.edit_tran));
            }

        }
        setupViewModel();
    }
    private void setupViewModel(){
        viewModel = ViewModelProviders.of(this)
                .get(EditTranVM.class);
        viewModel.getAccounts().observe(this, new Observer<List<AccountList>>() {
            @Override
            public void onChanged(List<AccountList> accountEntities) {
                mAccountAdapter.setData(accountEntities);
                if (mTranId != DEFAULT_ID){
                    viewModel.setTranId(mTranId);
                }
            }
        });

        viewModel.getCategories().observe(this, new Observer<List<CategoryEntity>>() {
            @Override
            public void onChanged(List<CategoryEntity> categoryEntities) {
                mCateAdapter.setData(categoryEntities);
            }
        });

        viewModel.getTransaction().observe(this, new Observer<TransactionEntity>() {
            @Override
            public void onChanged(TransactionEntity transactionEntity) {
                if (transactionEntity != null) {
                    if (transactionEntity.getLoan_id()!= null) {
                        mLoanId = transactionEntity.getLoan_id();
                    }
                   int type= transactionEntity.getTransaction_type();
                    setTransaction_Type(type);

                    Long cost =  transactionEntity.getTransaction_cost();


                    if (PrefManager.getUnitPref().equals(PrefManager.TOOMAN_UNIT_VALUE)){
                        cost = cost/10;
                    }

                    int accountId = -1;

                    if (type == TransactionEntity.TRANSACTION_PAY){
                        accountId = transactionEntity.getAccount_id_pay();
                    }else if (type == TransactionEntity.TRANSACTION_GET){
                        accountId = transactionEntity.getAccount_id_get();
                    }

                    mTranCost.setText(Long.toString(cost));
                    setDateTime( transactionEntity.getTransaction_date());
                    for (int i = 0; i < mCateAdapter.getCount(); i++) {
                        if (mCateAdapter.getItemId(i) ==
                                transactionEntity.getCategory_id()) {
                            tran_cate_spn.setSelection(i);
                            break;
                        }
                    }

                    for (int j = 0; j < mAccountAdapter.getCount(); j++) {
                        if (mAccountAdapter.getItemId(j) ==
                                accountId) {
                            tran_acc_spn.setSelection(j);
                            break;
                        }
                    }

                }
            }
        });

        viewModel.getLabels().observe(this, new Observer<List<LabelsEntity>>() {
            @Override
            public void onChanged(List<LabelsEntity> labelsEntities) {
                mTagsAdapter.setList(labelsEntities);
            }
        });

    }
    private void setTransaction_Type(int transaction_type_request){

        if (mTranType == transaction_type_request){
            return;
        }

        if (transaction_type_request == TransactionEntity.TRANSACTION_PAY
        || transaction_type_request == TransactionEntity.TRANSACTION_LOAN
        ) {
            mTranType = TransactionEntity.TRANSACTION_PAY;
            pay_btn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_pay_btn));
            pay_btn.setTextColor(Color.parseColor("#ffffff"));
            get_btn.setTextColor(Color.parseColor("#ff4081"));
            get_btn.setBackgroundColor(0x00000000);
            mTranAccountTitle.setText("از حساب");
        }else if (transaction_type_request == TransactionEntity.TRANSACTION_GET){
            mTranType = TransactionEntity.TRANSACTION_GET;
            get_btn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_pay_btn));
            get_btn.setTextColor(Color.parseColor("#ffffff"));
            pay_btn.setTextColor(Color.parseColor("#ff4081"));
            pay_btn.setBackgroundColor(0x00000000);
            mTranAccountTitle.setText("به حساب");
        }
    }
    private void setDateTime(PersianCalendar date){
        mTranDate.setText(
                PersianDigitConverter.PersianNumber(date.getPersianWeekDayName()+" "
                +date.getPersianDay()+" "+date.getPersianMonthName()+" "
                        +date.getPersianYear()));
        mTranTime.setText(
                PersianDigitConverter.PersianNumber(DateUtil.getTimeString(date.get(PersianCalendar.HOUR_OF_DAY)
                        ,date.get(PersianCalendar.MINUTE)))
        );

    }
    private void showDatePickerDialog(){
        final PersianCalendar pc = new PersianCalendar();
        int year = pc.getPersianYear();
        int month = pc.getPersianMonth();
        int day = pc.getPersianDay();

        if (!mTranDate.getText().toString().trim().matches("")){
            String[] date = mTranDate.getText().toString().split(" ");
             day = Integer.parseInt(date[1]);
             month  = DateUtil.intMonth(date[2]);
             year  = Integer.parseInt(date[3]);
        }

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(

                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year,
                                          int monthOfYear, int dayOfMonth) {
                       pc.setPersianDate(year,monthOfYear,dayOfMonth);

                      mTranDate.setText(PersianDigitConverter.PersianNumber(
                                      pc.getPersianWeekDayName()+" "+
                                          pc.getPersianDay()+" " +
                                          pc.getPersianMonthName()+" "+
                                          pc.getPersianYear()));

                      }
                }, year, month,
                   day);

        datePickerDialog.show(getActivity().getFragmentManager(), "tpd");
    }
    private void showTimePickerDialog(){
        final PersianCalendar pc = new PersianCalendar();
        int hour  = pc.get(PersianCalendar.HOUR_OF_DAY);
        int minute= pc.get(PersianCalendar.MINUTE);
        if (!mTranTime.getText().toString().trim().matches("")){
            String[] arrayTime =
                    mTranTime.getText().toString().trim().split(":");
             hour = Integer.parseInt(arrayTime[0]);
             minute = Integer.parseInt(arrayTime[1]);
        }
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                        mTranTime.setText(PersianDigitConverter.PersianNumber(
                                DateUtil.getTimeString(hourOfDay,minute)));
                    }
                }

                ,hour,
                minute,
                true);
        timePickerDialog.show(getActivity().getFragmentManager(),"tpt");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    private void saveTransaction() {

        MyDatabase db = MyDatabase.getInstance(getActivity().getApplicationContext());

        final int accountId = (int)tran_acc_spn.getSelectedItemId();
       final int cateId = (int) tran_cate_spn.getSelectedItemId();

        String cost = mTranCost.getText().toString();

        if (cost.contains(",")) {
            cost = cost.replaceAll(",","");
        }

        Long longCost = Long.parseLong(cost);

        if (PrefManager.getUnitPref().equals(PrefManager.TOOMAN_UNIT_VALUE)){
            longCost = longCost*10;
        }

        Long  tranCost = longCost;

        String stringTime = mTranTime.getText().toString();
        String[] arrayTime = stringTime.split(":");
        int hour = Integer.parseInt(arrayTime[0]);
        int minute = Integer.parseInt(arrayTime[1]);
        PersianCalendar tranDate =
                DateUtil.StringFullToPersianCalendar(mTranDate.getText().toString());

        tranDate.set(PersianCalendar.HOUR_OF_DAY,hour);
        tranDate.set(PersianCalendar.MINUTE,minute);

        final TransactionEntity transactionEntity = new TransactionEntity(tranDate,
                tranCost
                ,null,
                null,
                 cateId,
                null,
                mTranType);

        if (mTranType==TransactionEntity.TRANSACTION_GET && mLoanId==DEFAULT_ID){
            transactionEntity.setAccount_id_get(accountId);
        }else {
            transactionEntity.setAccount_id_pay(accountId);
        }


        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mTranId == DEFAULT_ID) {
                    db.transactionDao().addTransaction(
                            transactionEntity, mTagsAdapter.getList());
                }else{
                        transactionEntity.setTransaction_id(mTranId);
                        if (mLoanId != DEFAULT_ID){
                            transactionEntity.setLoan_id(mLoanId);
                            transactionEntity.setTransaction_type(TransactionEntity.TRANSACTION_LOAN);
                            transactionEntity.setCategory_id(11);
                        }
                        db.transactionDao().updateTransaction(
                                transactionEntity, mTagsAdapter.getList());
                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TranFrag.INSTANCE_TRANSACTION_ID, mTranId);
        outState.putInt(INSTANCE_TRAN_TYPE,TransactionEntity.TRANSACTION_PAY);
    }

    private boolean checkInput(){
        if (mTranCost.getText().toString().matches("") ){
            return false;
        }else if(tran_acc_spn.getSelectedItem()== null) {
            return false;
        }else if(tran_cate_spn.getSelectedItem()== null){
            return false;
        }else if (mTranType != TransactionEntity.TRANSACTION_GET &&
                mTranType != TransactionEntity.TRANSACTION_PAY){
            return false;
        }else{
            return true;
        }

    }

    private void changePermission(boolean p) {
        if (!p){
            tran_accept_imgBtn.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }else{
            tran_accept_imgBtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
