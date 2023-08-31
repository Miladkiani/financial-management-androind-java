package com.mili.manito;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mili.manito.Utilities.PrefManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class PinFragment extends DialogFragment {

    private String newPass;
    private String acceptPass;
    private final static int DEFAULT_HAS_CHANGE = -1;
    private int hasChange = DEFAULT_HAS_CHANGE;

    public PinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootView =
               inflater.inflate(R.layout.frd_pin, container, false);

        ImageButton closeDialog = rootView.findViewById(R.id.pin_cancel_imgBtn);
        EditText passTxt = rootView.findViewById(R.id.pin_pass_etxt);
        TextView messageTxt = rootView.findViewById(R.id.pin_title_txt);

        if (savedInstanceState != null &&
                savedInstanceState.containsKey(SettingsActivity.INSTANCE_CHANGE_PIN)){
            hasChange = savedInstanceState.getInt(SettingsActivity.INSTANCE_CHANGE_PIN);
            messageTxt.setText(getActivity().getResources().getString(R.string.pin_pass_new));
        }

        if ( hasChange == DEFAULT_HAS_CHANGE){
            if (getArguments()!=null &&
                    getArguments().containsKey(SettingsActivity.INSTANCE_CHANGE_PIN)){
                hasChange = getArguments().getInt(SettingsActivity.INSTANCE_CHANGE_PIN);
                messageTxt.setText(getActivity().getResources().getString(R.string.pin_pass_new));
            }
        }

        passTxt.addTextChangedListener(new  TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (passTxt.length()  == 4){
                        if (newPass == null) {
                            messageTxt.setText(getResources().getString(R.string.pin_pass_accept));
                            newPass = passTxt.getText().toString().trim();
                            passTxt.setText("");
                        }else{
                            acceptPass = passTxt.getText().toString().trim();
                            if (acceptPass.equals(newPass)){
                                PrefManager.setPinPref(acceptPass);
                                dismiss();
                            }else {
                                Toast.makeText(getActivity().getApplicationContext(),
                                        " گذرواژه ها همخوانی ندارند",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
       return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SettingsActivity.INSTANCE_CHANGE_PIN,hasChange);
    }
}
