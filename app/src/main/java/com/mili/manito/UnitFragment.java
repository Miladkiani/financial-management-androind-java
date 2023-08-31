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
public class UnitFragment extends DialogFragment {

    private RadioGroup unit_rad;
    private ImageButton unit_accept,unit_cancel;
    public UnitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v =
               inflater.inflate(R.layout.fragment_unit, container, false);
       unit_rad = v.findViewById(R.id.unit_rad);
       unit_accept = v.findViewById(R.id.unit_accept_imgBtn);
        unit_cancel = v.findViewById(R.id.unit_cancel_imgBtn);

       return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        checkPref();

        unit_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = unit_rad.getCheckedRadioButtonId();
                if (id == R.id.unit_rial_radio) {
                    PrefManager.setUnitPref(PrefManager.RIAL_UNIT_VALUE);
                } else if (id == R.id.unit_tooman_radio) {
                    PrefManager.setUnitPref(PrefManager.TOOMAN_UNIT_VALUE);
                }
                dismiss();
            }
        });

        unit_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private void checkPref(){
        String unitValue = PrefManager.getUnitPref();

        if (unitValue.equals(PrefManager.RIAL_UNIT_VALUE)){
            unit_rad.check(R.id.unit_rial_radio);
        }else if (unitValue.equals(PrefManager.TOOMAN_UNIT_VALUE)){
            unit_rad.check(R.id.unit_tooman_radio);
        }
    }
}
