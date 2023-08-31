package com.mili.manito.Adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.mili.manito.Model.AccountList;
import com.mili.manito.R;
import com.mili.manito.Utilities.PersianDigitConverter;
import com.mili.manito.Utilities.PrefManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.AccountHolder> {
    private List<AccountList> mAccounts;
    private Context mContext;
    private AccMoreLis mMoreLis;
    private final int[] arrayColor;

    public AccountListAdapter(Context context, List<AccountList> accounts, AccMoreLis moreLis) {
        this.mAccounts = accounts;
        this.mContext = context;
        arrayColor = context.getResources().getIntArray(R.array.androidColors);
        this.mMoreLis = moreLis;
    }



    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public AccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recycler_list_acc
                        , parent, false);
        return new AccountHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountHolder holder, int position) {
        AccountList account = mAccounts.get(position);
        int accountColor = arrayColor[(position + arrayColor.length) % (arrayColor.length)];
        holder.acc_name_title.setText(account.getAccount().getAccount_name().substring(0, 1));
        GradientDrawable bgColor = (GradientDrawable) holder.acc_name_title.getBackground();
        bgColor.setColor(accountColor);

        holder.acc_name.setText(account.getAccount().getAccount_name());

        Long balance = account.getAccount_balance();

        if (PrefManager.getUnitPref().equals(PrefManager.TOOMAN_UNIT_VALUE)){
            balance = balance/10;
        }
        holder.acc_balance.setText(PersianDigitConverter.PersianNumber(
                PersianDigitConverter.NumberFormat(balance
                )
        ));


        holder.unit.setText(PrefManager.getUnitPref());
    }

    public void setList(List<AccountList> list){
        mAccounts.clear();
        mAccounts.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mAccounts.size();
    }

    public class AccountHolder extends RecyclerView.ViewHolder{
        private TextView acc_name, acc_name_title , acc_balance,unit;
        private ImageButton acc_more;
        public AccountHolder(@NonNull View itemView) {
            super(itemView);
            acc_name = itemView.findViewById(R.id.acc_name_txt);
            acc_name_title  = itemView.findViewById(R.id.acc_name_title_txt);
            acc_balance  = itemView.findViewById(R.id.acc_balance_txt);
            acc_more  = itemView.findViewById(R.id.acc_more_imgBtn);
            unit = itemView.findViewById(R.id.acc_unit_txt);


            acc_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopupMenu popupMenu = new PopupMenu(mContext, acc_more);
                    popupMenu.getMenuInflater().inflate(R.menu.tran_more_menu,
                            popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int itemId = item.getItemId();
                            switch (itemId) {
                                case R.id.remove_item:
                                    mMoreLis.removeAcc(
                                            mAccounts.get(getAdapterPosition()).getAccount().getAccount_id());
                                    return true;
                                case R.id.update_item:
                                    mMoreLis.updateAcc(
                                            mAccounts.get(getAdapterPosition()).getAccount().getAccount_id());
                                    return true;
                            }
                            return true;
                        }
                    });

                    popupMenu.show();
                }
            });
        }
    }

    public interface AccMoreLis{
        void removeAcc(int id);
        void updateAcc(int id);
    }
}
