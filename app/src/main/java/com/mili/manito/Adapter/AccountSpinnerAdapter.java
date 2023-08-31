package com.mili.manito.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mili.manito.Model.AccountList;
import com.mili.manito.R;
import com.mili.manito.Utilities.PersianDigitConverter;
import com.mili.manito.Utilities.PrefManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AccountSpinnerAdapter extends ArrayAdapter<AccountList> {
    private  LayoutInflater inflater;
    private Context context;
    private final int[] arrayColor;
    private List<AccountList> accountEntities;
    public AccountSpinnerAdapter(@NonNull Activity context, int resource,
                                 @NonNull List<AccountList> list) {
        super(context, resource,  list);
        inflater = context.getLayoutInflater();
        this.context = context;
        this.accountEntities = list;
        arrayColor = context.getResources().getIntArray(R.array.androidColors);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int accountColor = arrayColor[(position+arrayColor.length)%(arrayColor.length)];

        if (convertView == null){
            convertView  =  inflater.inflate(R.layout.spn_list_acc,parent,false);
        }

        AccountList accountEntity = accountEntities.get(position);
        TextView txtNameTitle = convertView.findViewById(R.id.acc_name_title_txt);
        TextView txtName = convertView.findViewById(R.id.acc_name_txt);

        txtNameTitle.setText(accountEntity.getAccount().getAccount_name().substring(0,1));

        GradientDrawable bgColor = (GradientDrawable)txtNameTitle.getBackground();
        bgColor.setColor(accountColor);
        txtName.setText(accountEntity.getAccount().getAccount_name());
        return convertView;
    }

    @Override
    public int getCount() {
        return accountEntities.size();

    }

    public void setData( List<AccountList> list){
        accountEntities.clear();
        accountEntities.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return accountEntities.get(position).getAccount().getAccount_id();
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView  =  inflater.inflate(R.layout.spn_list_acc,parent,false);
        }

        AccountList accountEntity = accountEntities.get(position);
        TextView txtNameTitle = convertView.findViewById(R.id.acc_name_title_txt);
        TextView txtName = convertView.findViewById(R.id.acc_name_txt);
        TextView txtBalance = convertView.findViewById(R.id.acc_balance_txt);

        int accountColor = arrayColor[(position+arrayColor.length)%(arrayColor.length)];
        txtNameTitle.setText(accountEntity.getAccount().getAccount_name().substring(0,1));
        GradientDrawable bgColor = (GradientDrawable)txtNameTitle.getBackground();
        bgColor.setColor(accountColor);

        Long cost = accountEntity.getAccount_balance();

        if (PrefManager.getUnitPref().equals(PrefManager.TOOMAN_UNIT_VALUE)){
            cost = cost/10;
        }

        txtName.setText(accountEntity.getAccount().getAccount_name());

        txtBalance.setText(PersianDigitConverter.PersianNumber(PersianDigitConverter.NumberFormat(
                cost)));
        return convertView;
    }
}
