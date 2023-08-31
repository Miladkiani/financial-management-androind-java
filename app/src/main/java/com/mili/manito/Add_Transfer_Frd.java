package com.mili.manito;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.mili.manito.Adapter.AccountSpinnerAdapter;
import com.mili.manito.Model.AccountList;
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
public class Add_Transfer_Frd extends
        DialogFragment {


    private static final int DEFAULT_ID = -1;
    //private int mUserId = DEFAULT_ID;
    private EditText
            mTransferTime,
            mTransferDate;
    private ProgressBar progressBar;
    private ImageButton transfer_accept_imgBtn;

    private EditText mTransferCost;
    private Spinner
            tran_acc_from_spn,
            tran_acc_to_spn;
    private TextView mTitleTransfer;

    private AccountSpinnerAdapter mAccountAdapter;

    private int mTransferId = DEFAULT_ID;
    private static final String INSTANCE_TRAN_TYPE = "tran_type";
    //private EditTranVM viewModel;

    private EditTranVM viewModel;

    public Add_Transfer_Frd() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =
                inflater.inflate(R.layout.frd_add_transfer, container, false);

        mTransferDate = rootView.findViewById(R.id.transfer_date_etxt);
        mTransferTime = rootView.findViewById(R.id.transfer_time_etxt);
        progressBar = rootView.findViewById(R.id.transfer_progress);
        mTransferCost = rootView.findViewById(R.id.transfer_cost_etxt);
        mTitleTransfer = rootView.findViewById(R.id.title_add_transfer_txt);

        TextView unit = rootView.findViewById(R.id.cost_unit_transfer_txt);

        unit.setText(PrefManager.getUnitPref());

        mTransferCost.addTextChangedListener(TextFormatter.onTextChangedListener(mTransferCost,
                new TextFormatter.onChanged() {
            @Override
            public void OnTextChanged() {
                changePermission(checkInput());
            }
        }));
        tran_acc_from_spn = rootView.findViewById(R.id.tran_acc_from_spn);
        tran_acc_to_spn = rootView.findViewById(R.id.tran_acc_to_spn);
        transfer_accept_imgBtn = rootView.findViewById(R.id.transfer_accept_imBtn);
        ImageButton transfer_cancel_imgBtn= rootView.findViewById(R.id.transfer_cancel_imgBtn);

                transfer_accept_imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTransfer();
                dismiss();
            }
        });
        transfer_cancel_imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mTransferDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        mTransferTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        tran_acc_from_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changePermission(checkInput());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tran_acc_to_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changePermission(checkInput());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAccountAdapter = new AccountSpinnerAdapter(getActivity(),
                R.layout.spn_list_acc
                , new ArrayList<>());
        tran_acc_from_spn.setAdapter(mAccountAdapter);
        tran_acc_to_spn.setAdapter(mAccountAdapter);




        PersianCalendar pc = new PersianCalendar();
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = calendar.getTimeZone();
        pc.setTimeZone(tz);
        setDateAndTime(pc);


        if (savedInstanceState != null) {
        /*    if (savedInstanceState.containsKey(MainActivity.INSTANCE_USER_ID)) {
                mUserId = savedInstanceState.getInt(MainActivity.INSTANCE_USER_ID,
                        DEFAULT_ID);
                viewModel.setUserId(mUserId);

            }*/

            if (savedInstanceState.containsKey(TranFrag.INSTANCE_TRANSACTION_ID)) {
                mTransferId = savedInstanceState.getInt(
                        TranFrag.INSTANCE_TRANSACTION_ID,
                        DEFAULT_ID);
                if (mTransferId!= DEFAULT_ID) {

                    mTitleTransfer.setText(getResources().getString(R.string.edit_transfer));
                }
            }

        }


        /*if (mUserId == DEFAULT_ID) {
            if (getArguments() != null &&
                    getArguments().containsKey(MainActivity.INSTANCE_USER_ID)) {
                mUserId = getArguments().getInt(MainActivity.INSTANCE_USER_ID,
                        DEFAULT_ID);
                viewModel.setUserId(mUserId);
            }
        }*/

        if (mTransferId == DEFAULT_ID) {
            if (getArguments() != null &&
                    getArguments().containsKey(TranFrag.INSTANCE_TRANSACTION_ID)) {
                mTransferId = getArguments().getInt(TranFrag.INSTANCE_TRANSACTION_ID,
                        DEFAULT_ID);

                mTitleTransfer.setText(getResources().getString(R.string.edit_transfer));
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
                if (mTransferId!= DEFAULT_ID){
                    viewModel.setTranId(mTransferId);
                }
            }
        });


        viewModel.getTransaction().observe(this, new Observer<TransactionEntity>() {
            @Override
            public void onChanged(TransactionEntity transactionEntity) {
                if (transactionEntity != null) {

                    Long cost =  transactionEntity.getTransaction_cost();


                    if (PrefManager.getUnitPref().equals(PrefManager.TOOMAN_UNIT_VALUE)){
                        cost = cost/10;
                    }

                    int accountIdPay = transactionEntity.getAccount_id_pay();

                    int accountIdGet = transactionEntity.getAccount_id_get();


                    mTransferCost.setText(Long.toString(cost));
                    setDateAndTime( transactionEntity.getTransaction_date());


                    for (int j = 0; j < mAccountAdapter.getCount(); j++) {
                        if (mAccountAdapter.getItemId(j) == accountIdPay) {
                            tran_acc_from_spn.setSelection(j);

                        }

                        if (mAccountAdapter.getItemId(j)==accountIdGet){
                            tran_acc_to_spn.setSelection(j);
                        }
                    }

                }
            }
        });
    }

    private void setDateAndTime(PersianCalendar date){
        mTransferDate.setText(
                PersianDigitConverter.PersianNumber(date.getPersianWeekDayName()+" "
                        +date.getPersianDay()+" "+date.getPersianMonthName()+" "
                        +date.getPersianYear()));
        int hour = date.get(PersianCalendar.HOUR_OF_DAY);
        int minute = date.get(PersianCalendar.MINUTE);
        mTransferTime.setText(
                PersianDigitConverter.PersianNumber(DateUtil.getTimeString(hour,minute))
        );

    }

    private void showDatePickerDialog(){
        final PersianCalendar pc = new PersianCalendar();
        int year = pc.getPersianYear();
        int month = pc.getPersianMonth();
        int day = pc.getPersianDay();

        if (!mTransferDate.getText().toString().trim().matches("")){
            String[] date = mTransferDate.getText().toString().split(" ");
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

                        mTransferDate.setText(PersianDigitConverter.PersianNumber(
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
        if (!mTransferTime.getText().toString().trim().matches("")){
            String[] arrayTime =
                    mTransferTime.getText().toString().trim().split(":");
            hour = Integer.parseInt(arrayTime[0]);
            minute = Integer.parseInt(arrayTime[1]);
        }
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                        mTransferTime.setText(PersianDigitConverter.PersianNumber(
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

    private void saveTransfer() {

        MyDatabase db = MyDatabase.getInstance(getActivity().getApplicationContext());

        final int accountIdPay = (int)tran_acc_from_spn.getSelectedItemId();
        final int accountIdGet = (int)tran_acc_to_spn.getSelectedItemId();

        String cost = mTransferCost.getText().toString();

        if (cost.contains(",")) {
            cost = cost.replaceAll(",","");
        }

        Long longCost = Long.parseLong(cost);

        if (PrefManager.getUnitPref().equals(PrefManager.TOOMAN_UNIT_VALUE)){
            longCost = longCost*10;
        }

        Long  tranCost = longCost;

        String stringTime = mTransferTime.getText().toString();
        String[] arrayTime = stringTime.split(":");
        int hour = Integer.parseInt(arrayTime[0]);
        int minute = Integer.parseInt(arrayTime[1]);
        PersianCalendar tranDate =
                DateUtil.StringFullToPersianCalendar(mTransferDate.getText().toString());

        tranDate.set(PersianCalendar.HOUR_OF_DAY,hour);
        tranDate.set(PersianCalendar.MINUTE,minute);




        final TransactionEntity transactionEntity = new TransactionEntity(tranDate,
                tranCost
                ,accountIdPay,
                accountIdGet,
                null,
                null,
                TransactionEntity.TRANSACTION_TRANSFER);


        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mTransferId == DEFAULT_ID) {
                    db.transactionDao().addTransaction(
                            transactionEntity, null);
                }else{
                    transactionEntity.setTransaction_id(mTransferId);
                    db.transactionDao().updateTransaction(
                            transactionEntity, null);
                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    //    outState.putInt(MainActivity.INSTANCE_USER_ID, mUserId);
        outState.putInt(TranFrag.INSTANCE_TRANSACTION_ID, mTransferId);
    }

    private boolean checkInput(){
        if (mTransferCost.getText().toString().matches("") ){
            return false;
        }else if(tran_acc_from_spn.getSelectedItem()== null) {
            return false;
        }else if(tran_acc_to_spn.getSelectedItem()== null){
            return false;
        }else if (tran_acc_from_spn.getSelectedItemId() == tran_acc_to_spn.getSelectedItemId()) {
            return false;
        }else{
            return true;
        }

    }

    private void changePermission(boolean p) {
        if (!p){
            transfer_accept_imgBtn.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }else{
            transfer_accept_imgBtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
