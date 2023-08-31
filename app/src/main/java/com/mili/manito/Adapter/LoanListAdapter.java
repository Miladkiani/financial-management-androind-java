package com.mili.manito.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.mili.manito.Model.LoanList;
import com.mili.manito.R;
import com.mili.manito.Utilities.PersianDigitConverter;
import com.mili.manito.Utilities.PrefManager;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class LoanListAdapter extends RecyclerView.Adapter<LoanListAdapter.MyHolder> {

    private Context mContext;
    private List<LoanList> loanList;
    private MoreListener mListener;


    public LoanListAdapter(Context mContext, List<LoanList> loanList, MoreListener listener) {
        this.mContext = mContext;
        this.loanList = loanList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).
                inflate(R.layout.recycler_list_loan, parent, false);
        return new MyHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        LoanList loan = loanList.get(position);
        String title = loan.getLoan_title();
        int count = loan.getLoan_ins_count();
        Long cost = loan.getLoan_ins_cost();
        Long loanPay = loan.getLoan_paid();
        if(PrefManager.getUnitPref().equals(PrefManager.TOOMAN_UNIT_VALUE)){
            cost = cost/10;
            loanPay = loanPay/10;
        }
        Long allCost = cost * count;

        int remindCount ;
        if (loanPay == 0){
           remindCount = count;
        }else{
            remindCount = (count-(int)(loanPay/cost));
        }

        Log.v(LoanListAdapter.class.getSimpleName(),"remind count is: "+remindCount );


        int percent = (int) (long) ((loanPay * 100) / (allCost));


        holder.loanCount.setText(PersianDigitConverter.PersianNumber(
                PersianDigitConverter.NumberFormat(count)
                )
        );
        holder.loanCost.setText(
                PersianDigitConverter.PersianNumber(
                      PersianDigitConverter.NumberFormat(cost)
                   )
                .concat(" ").concat(PrefManager.getUnitPref().substring(0,1))

        );
        holder.loanTitle.setText(title);
        holder.loanRemindCount.setText(PersianDigitConverter.PersianNumber(
               PersianDigitConverter.NumberFormat(remindCount)));


        holder.progressView.setProgress(percent);
        holder.progressView.setProgressStartColor(Color.parseColor("#FFEB3B"));
        holder.progressView.setProgressEndColor(Color.parseColor("#4CAF50"));
        holder.more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, holder.more_btn);
                popupMenu.getMenuInflater().inflate(R.menu.loan_more_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        switch (itemId) {
                            case R.id.update_loan_item:
                                mListener.updateLoan(loan.getLoan_id());
                                return true;
                            case R.id.remove_loan_item:
                                mListener.removeLoan(loan.getLoan_id());
                                return true;
                            case R.id.pay_loan_item:
                                mListener.payLoan(loan.getLoan_id());
                                return true;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return loanList.size();
    }

    public void setData(List<LoanList> list){
        loanList.clear();
        loanList.addAll(list);
        notifyDataSetChanged();
    }


    class MyHolder extends RecyclerView.ViewHolder {
        TextView loanTitle, loanCount, loanCost, loanRemindCount;
        CircleProgressBar progressView;
        ImageButton more_btn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            loanTitle = itemView.findViewById(R.id.loan_title_list_txt);
            loanCost = itemView.findViewById(R.id.loan_cost_list_txt);
            loanCount = itemView.findViewById(R.id.loan_count_list_txt);
            loanRemindCount = itemView.findViewById(R.id.loan_count_remind_list_txt);
            progressView = itemView.findViewById(R.id.progress_view);
            progressView.setProgressFormatter(new CircleProgressBar.ProgressFormatter() {
                @Override
                public CharSequence format(int progress, int max) {
                    return mContext.getResources().getString(R.string.percent_persian_symbol).concat(PersianDigitConverter.PersianNumber
                            (PersianDigitConverter.NumberFormat(progress)));
                }
            });
            more_btn = itemView.findViewById(R.id.loan_more_imb);
        }
    }

    public interface MoreListener{
        void updateLoan(int id);
        void removeLoan(int id);
        void payLoan (int id);
    }
}
