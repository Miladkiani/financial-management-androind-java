package com.mili.manito;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.mili.manito.Utilities.PrefManager;

public class SettingsActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Switch pinSwitch;
    private TextView changePin,unitTxt;

    public final static String INSTANCE_CHANGE_PIN = "change_mPin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.setting_toolbar);
        RelativeLayout passRelative = findViewById(R.id.relative_pass);
        RelativeLayout unitRelative = findViewById(R.id.relative_unit);
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");

         pinSwitch = findViewById(R.id.pin_switch);
         changePin = findViewById(R.id.change_pin_txt);
         unitTxt= findViewById(R.id.settings_unit_txt);

         pinSwitch.setClickable(false);
         passRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinSwitch.isChecked()){
                    PrefManager.setPinPref(PrefManager.DEFAULT_PIN_VALUE);

                }else{
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag("pinDialog");
                    if (fragment != null) {
                        ft.remove(fragment);
                    }
                    ft.addToBackStack(null);
                    DialogFragment dialogFragment = new PinFragment();
                    dialogFragment.show(ft, "pinDialog");
                }
            }
        });

         unitRelative.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                 Fragment fragment = getSupportFragmentManager().findFragmentByTag("unitDialog");
                 if (fragment != null) {
                     ft.remove(fragment);
                 }
                 ft.addToBackStack(null);
                 DialogFragment dialogFragment = new UnitFragment();
                 dialogFragment.show(ft, "unitDialog");
             }
         });

        if (PrefManager.getPinPref().equals(PrefManager.DEFAULT_PIN_VALUE)){
            pinSwitch.setChecked(false);
            changePin.setTextColor(getResources().getColor(R.color.hLine));
            changePin.setClickable(false);
            changePin.setFocusable(false);
            changePin.setBackgroundResource(R.color.transparent);
        }else{
            pinSwitch.setChecked(true);
            changePin.setTextColor(Color.parseColor("#808080"));
            changePin.setClickable(true);
            changePin.setFocusable(true);
            TypedValue outValue = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            changePin.setBackgroundResource(outValue.resourceId);
        }

        unitTxt.setText(PrefManager.getUnitPref());


        changePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinSwitch.isChecked()){
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag("pinDialog");
                    if (fragment != null) {
                        ft.remove(fragment);
                    }
                    ft.addToBackStack(null);
                    DialogFragment dialogFragment = new PinFragment();
                    Bundle b = new Bundle();
                    b.putInt(INSTANCE_CHANGE_PIN,1);
                    dialogFragment.setArguments(b);
                    dialogFragment.show(ft, "pinDialog");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        PrefManager.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        PrefManager.getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PrefManager.PIN_PREF_ITEM)){
            String s = sharedPreferences.getString
                    (PrefManager.PIN_PREF_ITEM,PrefManager.DEFAULT_PIN_VALUE);
            if (s!=null && s.equals(PrefManager.DEFAULT_PIN_VALUE)){
                pinSwitch.setChecked(false);
                changePin.setTextColor(getResources().getColor(R.color.hLine));
                changePin.setClickable(false);
                changePin.setFocusable(false);
                changePin.setBackgroundResource(R.color.transparent);
            }else{
                pinSwitch.setChecked(true);
                changePin.setTextColor(Color.parseColor("#808080"));
                changePin.setClickable(true);
                changePin.setFocusable(true);
                TypedValue outValue = new TypedValue();
                getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                changePin.setBackgroundResource(outValue.resourceId);
            }
        }

        if (key.equals(PrefManager.UNIT_PREF_ITEM)){
            String s = sharedPreferences.getString(PrefManager.UNIT_PREF_ITEM,
                    PrefManager.TOOMAN_UNIT_VALUE);
            if (s!=null){
                unitTxt.setText(s);
            }
        }
    }
}
