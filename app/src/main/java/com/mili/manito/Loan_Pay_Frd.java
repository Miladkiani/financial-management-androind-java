package com.mili.manito;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.mili.manito.Adapter.AccountSpinnerAdapter;
import com.mili.manito.Model.AccountList;
import com.mili.manito.Model.LoanList;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Model.TransactionEntity;
import com.mili.manito.Thread.AppExecutors;
import com.mili.manito.Utilities.DateUtil;
import com.mili.manito.Utilities.PersianDigitConverter;
import com.mili.manito.Utilities.TextFormatter;
import com.mili.manito.ViewModel.PayLoanVM;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Loan_Pay_Frd extends DialogFragment {
    private EditText payHowMany , payDate,payTime;
    private Spinner payAccSpn;
    private  ImageButton payAccept;
    private AccountSpinnerAdapter accAdapter;
    private ProgressBar progressBar;
    private final static int DEFAULT_ID = -1;
    private int mUserId = DEFAULT_ID;
    private int mLoanId = DEFAULT_ID;
    private final static int LOAN_CATE_ID = 11;
    private int mRemindCount;
    private Long mLoancost;

    private PayLoanVM viewModel;

    public Loan_Pay_Frd() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.frd_loan_pay, container, false);
        payHowMany = v.findViewById(R.id.payloan_howmany_etxt);
        payHowMany.addTextChangedListener(TextFormatter.onTextChangedListener(payHowMany, new TextFormatter.onChanged() {
            @Override
            public void OnTextChanged() {
                checkPermission(checkInput());
            }
        }));
        payAccSpn = v.findViewById(R.id.payloan_acc_spn);
        payAccSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkPermission(checkInput());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        payDate = v.findViewById(R.id.payloan_dates_etxt);
        payTime = v.findViewById(R.id.payloan_time_etxt);
        payDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkPermission(checkInput());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        payTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkPermission(checkInput());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        progressBar = v.findViewById(R.id.payloan_progress);
        payDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        payTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
         payAccept = v.findViewById(R.id.payloan_accept_imBtn);
        payAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertPay();
            }
        });
        ImageButton payCancel = v.findViewById(R.id.payloan_cancel_imgBtn);
        payCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return v;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        accAdapter = new AccountSpinnerAdapter(getActivity(),
                R.layout.spn_list_acc
                , new ArrayList<>());
        payAccSpn.setAdapter(accAdapter);
        setupViewModel();


        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MainActivity.INSTANCE_USER_ID)) {
                mUserId = savedInstanceState.getInt(MainActivity.INSTANCE_USER_ID,
                        DEFAULT_ID);
                viewModel.setUserId(mUserId);
            }

            if (savedInstanceState.containsKey(LoanFrag.INSTANCE_LOAN_ID)) {
                mLoanId = savedInstanceState.getInt(
                      LoanFrag.INSTANCE_LOAN_ID,
                        DEFAULT_ID);
                viewModel.setLoanId(mLoanId);
            }


        }

        if (mUserId == DEFAULT_ID) {
            if (getArguments() != null &&
                    getArguments().containsKey(MainActivity.INSTANCE_USER_ID)) {
                mUserId = getArguments().getInt(MainActivity.INSTANCE_USER_ID,
                        DEFAULT_ID);

                if (mUserId != DEFAULT_ID) {
                    viewModel.setUserId(mUserId);
                }
            }
        }

        if (mLoanId == DEFAULT_ID)
        {
            if (getArguments() != null &&
                    getArguments().containsKey(LoanFrag.INSTANCE_LOAN_ID)) {
                mLoanId = getArguments().getInt(LoanFrag.INSTANCE_LOAN_ID,
                        DEFAULT_ID);
                viewModel.setLoanId(mLoanId);

            }
        }



    }

    private void setupViewModel(){
        viewModel =
                ViewModelProviders.of(this).get(PayLoanVM.class);
        viewModel.getAccounts().observe(this, new Observer<List<AccountList>>() {
            @Override
            public void onChanged(List<AccountList> accountEntities) {
                accAdapter.setData(accountEntities);
            }
        });
        viewModel.getLoan().observe(this, new Observer<LoanList>() {
            @Override
            public void onChanged(LoanList loan) {
                 mLoancost = loan.getLoan_ins_cost();

                int count = loan.getLoan_ins_count();

                Long loanPay = loan.getLoan_paid();


                if (loanPay == 0){
                    mRemindCount = count;
                }else{
                    mRemindCount = (count-(int)(loanPay/mLoancost));
                }

            }
        });
    }

    private void insertPay(){

        String payHowManyText =  payHowMany.getText().toString();

        if (payHowManyText.contains(",")) {
            payHowManyText = payHowManyText.replaceAll(",","");
        }

        final int howMany = Integer.parseInt(payHowManyText);

        if (howMany > mRemindCount){
            Toast.makeText(getActivity().getApplicationContext()
                    ,getActivity().getResources().getString(R.string.check_remind_loan)
                    ,Toast.LENGTH_LONG).show();
            return;
        }
         final int accId = (int)payAccSpn.getSelectedItemId();

         long payCost = howMany*mLoancost;

         String d = payDate.getText().toString();
         PersianCalendar pc = DateUtil.StringFullToPersianCalendar(d);

         String t = payTime.getText().toString();
         String[] arrayTime = t.split(":");
         int hour = Integer.parseInt(arrayTime[0]);
         int minute = Integer.parseInt(arrayTime[1]);
          pc.set(PersianCalendar.HOUR_OF_DAY,hour);
          pc.set(PersianCalendar.MINUTE,minute);

         TransactionEntity entity = new TransactionEntity(pc,
                 payCost,
                 accId,
                 null,
                 LOAN_CATE_ID,
                 mLoanId,
                 TransactionEntity.TRANSACTION_LOAN);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                MyDatabase db = MyDatabase.getInstance(getActivity().getApplicationContext());
                    db.transactionDao().addTransaction(entity,
                            new ArrayList<>());
                    dismiss();
            }
        });
    }


    private void showDatePickerDialog(){
       final PersianCalendar pc = new PersianCalendar();
        int year = pc.getPersianYear();
        int month = pc.getPersianMonth();
        int day = pc.getPersianDay();

        if (!payDate.getText().toString().trim().matches("")){
            String[] date = payDate.getText().toString().split(" ");
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

                        payDate.setText(PersianDigitConverter.PersianNumber(
                                pc.getPersianWeekDayName()+" "+
                                        pc.getPersianDay()+" " +
                                        pc.getPersianMonthName()+" "+
                                        pc.getPersianYear()));

                    }
                }, year,
                month,
                day);
        datePickerDialog.show(getActivity().getFragmentManager(), "tpd");
    }

    private void showTimePickerDialog(){
        final PersianCalendar pc = new PersianCalendar();
        int hour  = pc.get(PersianCalendar.HOUR_OF_DAY);
        int minute= pc.get(PersianCalendar.MINUTE);
        if (!payTime.getText().toString().trim().matches("")){
            String[] arrayTime =
                    payTime.getText().toString().trim().split(":");
            hour = Integer.parseInt(arrayTime[0]);
            minute = Integer.parseInt(arrayTime[1]);
        }
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                        payTime.setText(PersianDigitConverter.PersianNumber(
                                DateUtil.getTimeString(hourOfDay,minute)));
                    }
                }

                ,hour,
                minute,
                true);
        timePickerDialog.show(getActivity().getFragmentManager(),"tpt");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MainActivity.INSTANCE_USER_ID, mUserId);
        outState.putInt(LoanFrag.INSTANCE_LOAN_ID, mLoanId);
    }

    private boolean checkInput(){
        if (payHowMany.getText().toString().matches("")){
            return false;
        }else if (payAccSpn.getSelectedItem()==null){
            return false;
        }else if(payTime.getText().toString().matches("")){
            return false;
        }else if(payDate.getText().toString().matches("")){
            return false;
        }
        return true;
    }

    private void checkPermission(boolean p){
        if (!p){
            payAccept.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }else{
            payAccept.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
