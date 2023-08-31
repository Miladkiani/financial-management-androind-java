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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mili.manito.Model.AccountEntity;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Thread.AppExecutors;
import com.mili.manito.Utilities.PrefManager;
import com.mili.manito.Utilities.TextFormatter;
import com.mili.manito.ViewModel.EditAccVM;
import com.mili.manito.ViewModel.EditAccVMF;


/**
 * A simple {@link Fragment} subclass.
 */
public class Add_Acc_Frd extends DialogFragment {
    private final static String LOG_TAG = Add_Acc_Frd.class.getSimpleName();
    private EditText acc_name,acc_pri_balance;
    private ImageButton acc_accept;
    private RadioGroup radioGroup;
    private int mAccCondition = AccountEntity.ACCOUNT_ACTIVE;
    private MyDatabase mDB;
    private ProgressBar progressBar;
    private TextView mAccTitle;

    private static final int DEFAULT_USER_ID = -1;
    private static final String INSTANCE_ACCOUNT_ID = "acc_id";
    private int mUserId = DEFAULT_USER_ID;
    private int mAccountId = DEFAULT_ACCOUNT_ID;
    private final static int DEFAULT_ACCOUNT_ID = -1;



    public Add_Acc_Frd() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=
                inflater.inflate(R.layout.frd_add_acc, container, false);
        TextView costTitle = rootView.findViewById(R.id.account_cost_title_txt);
        costTitle.setText(
                getResources().getString(R.string.default_balance).concat("(")
        .concat(PrefManager.getUnitPref()).concat(")")
        );
        mAccTitle = rootView.findViewById(R.id.title_acc_add_txt);
        acc_name = rootView.findViewById(R.id.acc_name_etxt);
        acc_pri_balance = rootView.findViewById(R.id.acc_pri_balance_etxt);
        acc_pri_balance.addTextChangedListener(TextFormatter.onTextChangedListener(acc_pri_balance, new TextFormatter.onChanged() {
            @Override
            public void OnTextChanged() {

            }
        }) );



        acc_name.addTextChangedListener(new TextWatcher() {
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
        progressBar = rootView.findViewById(R.id.acc_progress);
        radioGroup = rootView.findViewById(R.id.condition_rad);
        acc_accept = rootView.findViewById(R.id.acc_accept_imgBtn);
       ImageButton acc_cancel = rootView.findViewById(R.id.acc_cancel_imgBtn);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.acc_ac_rad ){
                    mAccCondition = AccountEntity.ACCOUNT_ACTIVE;
                }else{
                    mAccCondition = AccountEntity.ACCOUNT_BLOCKED;
                }
            }
        });

        acc_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAccount();
            }
        });

        acc_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDB = MyDatabase.getInstance(getActivity().getApplicationContext());
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MainActivity.INSTANCE_USER_ID)) {
                mUserId = savedInstanceState.getInt(MainActivity.INSTANCE_USER_ID,
                        DEFAULT_USER_ID);
            }

            if (savedInstanceState.containsKey(INSTANCE_ACCOUNT_ID)) {
                mAccountId = savedInstanceState.getInt(
                        INSTANCE_ACCOUNT_ID,
                        DEFAULT_ACCOUNT_ID);
                mAccTitle.setText(getResources().getString(R.string.edit_acc));
            }
        }

        if (mUserId == DEFAULT_USER_ID) {
            if (getArguments() != null &&
                    getArguments().containsKey(MainActivity.INSTANCE_USER_ID)) {
                mUserId = getArguments().getInt(MainActivity.INSTANCE_USER_ID, DEFAULT_USER_ID);
            }
        }

        if (mAccountId == DEFAULT_ACCOUNT_ID) {
            if (getArguments() != null &&
                    getArguments().containsKey(INSTANCE_ACCOUNT_ID)) {
                mAccountId = getArguments().getInt(INSTANCE_ACCOUNT_ID,
                        DEFAULT_ACCOUNT_ID);
                mAccTitle.setText(getResources().getString(R.string.edit_acc));
                EditAccVMF viewModelFactory = new EditAccVMF
                        (mDB, mUserId, mAccountId);
                final EditAccVM viewModel = ViewModelProviders.of(this, viewModelFactory)
                        .get(EditAccVM.class);
                viewModel.getsAccounts().observe(this, new Observer<AccountEntity>() {
                    @Override
                    public void onChanged(AccountEntity accountEntity) {
                        viewModel.getsAccounts().removeObserver(this);
                        populateUi(accountEntity);
                    }
                });

            }
        }


    }


    private void populateUi(AccountEntity accountEntity){
        if(accountEntity!=null){
        acc_name.setText(accountEntity.getAccount_name());
            Long cost =  accountEntity.getAcc_primitive_ballance();

            if (PrefManager.getUnitPref().equals(PrefManager.TOOMAN_UNIT_VALUE)){
                cost = cost/10;
            }
        acc_pri_balance.setText(Long.toString(cost));

        int condition = accountEntity.getAcc_condition();
        switch (condition) {
            case AccountEntity.ACCOUNT_ACTIVE:
                radioGroup.check(R.id.acc_ac_rad);
                break;
            case AccountEntity.ACCOUNT_BLOCKED:
                radioGroup.check(R.id.acc_diac_rad);
                break;
            }
        }
}

    private void saveAccount(){

        String mName = acc_name.getText().toString();
        long mBalance = 0;
        String cost = acc_pri_balance.getText().toString();
        if (!cost.matches("")) {
            if (cost.contains(",")) {
                cost = cost.replaceAll(",","");
            }
            long balance = Long.parseLong(cost);
            if (balance != 0) {
                mBalance = balance;
            }
        }

        if (PrefManager.getUnitPref().equals(PrefManager.TOOMAN_UNIT_VALUE)){
            mBalance = mBalance*10;
        }

        final AccountEntity accountEntity = new AccountEntity(
                mName,mAccCondition,mBalance,mUserId);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mAccountId == DEFAULT_ACCOUNT_ID) {
                    mDB.accountDao().insert(accountEntity);
                    dismiss();
                }else{
                   accountEntity.setAccount_id(mAccountId);
                    mDB.accountDao().update(accountEntity);
                    dismiss();
                }
            }
        });

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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INSTANCE_ACCOUNT_ID,mAccountId);
        outState.putInt(MainActivity.INSTANCE_USER_ID,mUserId);
    }

    private boolean checkInput(){
          return (!acc_name.getText().toString().matches(""));
    }

    private void changePermission(boolean p){
        if (!p){
            acc_accept.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }else {
            acc_accept.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

}
