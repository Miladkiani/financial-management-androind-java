package com.mili.manito;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.mili.manito.Utilities.PrefManager;

public class LoginAc extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        PrefManager.initialize(getApplication().getApplicationContext());
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        PinLockView pinLockView = findViewById(R.id.pinLockView);
        IndicatorDots indicatorDots = findViewById(R.id.indicator_dots);
        TextView loginMessage = findViewById(R.id.login_message_txt);

        pinLockView.attachIndicatorDots(indicatorDots);

        //set lock code length
        pinLockView.setPinLength(4);

        String mPinCode = PrefManager.getPinPref();

       pinLockView.setPinLockListener(new PinLockListener() {
           @Override
           public void onComplete(String pin) {
               Log.v(mPinCode,"onComplete : "+mPinCode);
               if (pin.equals(mPinCode)){
                   Intent intent = new Intent(LoginAc.this,MainActivity.class);
                   startActivity(intent);
                   finish();
               }else{
                loginMessage.setText("رمز عبور وارد شده صحیح نمی باشد");
               }
           }

           @Override
           public void onEmpty() {

           }

           @Override
           public void onPinChange(int pinLength, String intermediatePin) {

           }
       });
    }

    @Override
    protected void onResume() {
        super.onResume();


            if(PrefManager.getPinPref().equals(PrefManager.DEFAULT_PIN_VALUE)
                   ) {
                Intent intent = new Intent(LoginAc.this,MainActivity.class );
                startActivity(intent);
            }

    }
}
