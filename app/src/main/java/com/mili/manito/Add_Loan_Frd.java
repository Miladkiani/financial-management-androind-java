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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mili.manito.Model.LoanEntity;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Thread.AppExecutors;
import com.mili.manito.Utilities.DateUtil;
import com.mili.manito.Utilities.PersianDigitConverter;
import com.mili.manito.Utilities.PrefManager;
import com.mili.manito.Utilities.TextFormatter;
import com.mili.manito.ViewModel.EditLoanVM;
import com.mili.manito.ViewModel.EditLoanVMF;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class Add_Loan_Frd extends DialogFragment {

    private static final int DEFAULT_USER_ID = -1;
    private int mUserId = DEFAULT_USER_ID;
    private TextView mLoanTitle;

    private EditText loan_title,loan_cost
            ,loan_count;
    private EditText loan_date;
    private ImageButton loan_accept;
    private ProgressBar progressBar;
    private static final int DEFAULT_LOAN_ID = -1;
    private int mLoanId = DEFAULT_LOAN_ID;

    private MyDatabase sDb;

    private EditLoanVM viewModel;
    public Add_Loan_Frd() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=
                inflater.inflate(R.layout.frd_add_loan, container, false);
        mLoanTitle = rootView.findViewById(R.id.title_add_loan_txt);
        loan_title = rootView.findViewById(R.id.title_loan_etxt);
        loan_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changePermission(checkInput());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        loan_cost = rootView.findViewById(R.id.cost_loan_etxt);
        loan_cost.addTextChangedListener(TextFormatter.onTextChangedListener(loan_cost, new TextFormatter.onChanged() {
            @Override
            public void OnTextChanged() {
                changePermission(checkInput());
            }
        }));

        TextView costTitle = rootView.findViewById(R.id.loan_title_cost_txt);
        costTitle.setText(getResources().getString(R.string.cost_loan).concat("(")
                .concat(PrefManager.getUnitPref())
                .concat(")")

        );

        loan_count = rootView.findViewById(R.id.count_loan_etxt);
        loan_count.addTextChangedListener(TextFormatter.onTextChangedListener(loan_count, new TextFormatter.onChanged() {
            @Override
            public void OnTextChanged() {
                changePermission(checkInput());
            }
        }));
        loan_date = rootView.findViewById(R.id.dates_loan_etxt);
        loan_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changePermission(checkInput());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        loan_accept = rootView.findViewById(R.id.loan_accept_imBtn);
        progressBar = rootView.findViewById(R.id.loan_progress);
        ImageButton loan_cancel = rootView.findViewById(R.id.loan_cancel_imgBtn);
        ImageButton add_date = rootView.findViewById(R.id.add_date_imgbtn);
        loan_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLoan();
            }
        });
        loan_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        add_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sDb = MyDatabase.getInstance(getActivity().getApplicationContext());

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LoanFrag.INSTANCE_LOAN_ID)) {
                mLoanId = savedInstanceState.getInt(LoanFrag.INSTANCE_LOAN_ID,
                        DEFAULT_LOAN_ID);
                mLoanTitle.setText(R.string.edit_loan);
                setupViewModel();
            }
        }

       if  (mLoanId == DEFAULT_LOAN_ID) {
           if (getArguments() != null &&
                   getArguments().containsKey(LoanFrag.INSTANCE_LOAN_ID)) {
               mLoanId = getArguments().getInt(LoanFrag.INSTANCE_LOAN_ID,DEFAULT_LOAN_ID);
               mLoanTitle.setText(R.string.edit_loan);
                setupViewModel();
           }
       }



    }

    private void setupViewModel(){
        EditLoanVMF factory = new EditLoanVMF(sDb,mLoanId);
        viewModel = ViewModelProviders.of(this,factory).get(EditLoanVM.class);
        viewModel.getmLoan().observe(this, new Observer<LoanEntity>() {
            @Override
            public void onChanged(LoanEntity loanEntity) {
                viewModel.getmLoan().removeObserver(this);
                populateUi(loanEntity);
            }
        });
    }

    private void populateUi(LoanEntity loanEntity){
        if(loanEntity!=null){

            loan_title.setText(loanEntity.getLoan_title());
            loan_count.setText(Integer.toString(loanEntity.getLoan_instalment_count()));

            Long cost = loanEntity.getLoan_instalment_cost();
            if (PrefManager.getUnitPref().equals(PrefManager.TOOMAN_UNIT_VALUE)) {
                cost = cost/10;
            }

            loan_cost.setText(Long.toString(cost));
            loan_date.setText(DateUtil.toStringStandard(loanEntity.getLoan_start_time().getTimeInMillis()));
        }
    }

    private void saveLoan(){
            String loanTitle = loan_title.getText().toString();

            String cost = loan_cost.getText().toString();
        if(cost.contains(",")){
           cost = cost.replaceAll(",","");
        }
        Long loanCost =  Long.parseLong(cost);

        if (PrefManager.getUnitPref().equals(PrefManager.TOOMAN_UNIT_VALUE)){
            loanCost = loanCost*10;
        }

            int loanCount = Integer.parseInt(loan_count.getText().toString());
            PersianCalendar startTime  =
                    DateUtil.StringToPersianCalendar(loan_date.getText().toString());
            PersianCalendar endTime =
                    DateUtil.addMonth(startTime.getTimeInMillis(),(loanCount-1));
        LoanEntity loanEntity = new
                LoanEntity(loanTitle,startTime,endTime,loanCost,loanCount,1);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mLoanId == DEFAULT_LOAN_ID) {
                    sDb.loanDao().insert(loanEntity);
                    dismiss();
                }else{
                    loanEntity.setLoan_id(mLoanId);
                    sDb.loanDao().update(loanEntity);
                    dismiss();
                }
            }
        });

    }

    private void showDatePickerDialog(){
        final  PersianCalendar pc = new PersianCalendar();
        int year = pc.getPersianYear();
        int month = pc.getPersianMonth();
        int day = pc.getPersianDay();

        if (!loan_date.getText().toString().trim().matches("")){
            String[] date = loan_date.getText().toString().split("/");
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
                        loan_date.setText(PersianDigitConverter.PersianNumber(
                                    year+"/"+
                                        df.format(monthOfYear+1)+"/" +
                                        df.format(dayOfMonth)));
                    }
                }, year,
                month,
               day);
        datePickerDialog.show(getActivity().getFragmentManager(), "tpd");
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    private boolean checkInput(){
        if (loan_title.getText().toString().matches("")){
            return false;
        }else if (loan_count.getText().toString().matches("")){
            return false;
        }else if(loan_cost.getText().toString().matches("")){
            return false;
        }else if (loan_date.getText().toString().matches("")){
            return false;
        }
        return true;
    }

    private void changePermission(boolean p){
        if (!p){
            progressBar.setVisibility(View.VISIBLE);
            loan_accept.setVisibility(View.INVISIBLE);
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            loan_accept.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LoanFrag.INSTANCE_LOAN_ID,DEFAULT_LOAN_ID);
    }
}
