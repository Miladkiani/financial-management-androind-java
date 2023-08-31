package com.mili.manito.Utilities;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class TextFormatter  {
   public static TextWatcher onTextChangedListener(EditText editText,onChanged onChanged) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onChanged.OnTextChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.removeTextChangedListener(this);
                try {
                    String originalString = s.toString();


                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    Long longVal = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = PersianDigitConverter.PersianNumber(formatter.format(longVal));

                    //setting text after format to EditText
                    editText.setText(formattedString);
                    editText.setSelection(editText.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }


                editText.addTextChangedListener(this);
            }
        };
    }

    public interface onChanged{
       void OnTextChanged();
    }
}
