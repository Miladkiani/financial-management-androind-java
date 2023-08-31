package com.mili.manito;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.mili.manito.Utilities.PrefManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class Filter_Tran_Frd extends DialogFragment {

    private RadioGroup date_filter;
    private ImageButton filter_accept,filter_cancel;

    public Filter_Tran_Frd() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.frd_filter_tran_list,
                container, false);
        date_filter = rootView.findViewById(R.id.condition_rad);
        filter_accept = rootView.findViewById(R.id.filter_accept_imgBtn);
        filter_cancel =rootView.findViewById(R.id.filter_cancel_imgBtn);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        checkDatePref();

        filter_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedDate = date_filter.getCheckedRadioButtonId();
                switch (selectedDate){
                    case R.id.filter_monthly_radio:
                        PrefManager.setIntervalPref(PrefManager.MONTHLY_VALUE_PREF);
                        break;
                    case R.id.filter_yearly_radio:
                        PrefManager.setIntervalPref(PrefManager.YEARLY_VALUE_PREF);
                        break;
                    default:
                        PrefManager.setIntervalPref(PrefManager.DAILY_VALUE_PREF);
                }
                dismiss();
            }
        });

        filter_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void checkDatePref(){
        int checkedItem = PrefManager.getIntervalPref();

        switch (checkedItem){
            case PrefManager.DAILY_VALUE_PREF:
                date_filter.check(R.id.filter_daily_radio);
                break;
            case PrefManager.MONTHLY_VALUE_PREF:
                date_filter.check(R.id.filter_monthly_radio);
                break;
            case PrefManager.YEARLY_VALUE_PREF:
                date_filter.check(R.id.filter_yearly_radio);
                break;
        }
    }
}
