package com.mili.manito.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mili.manito.Model.BudgetDetailList;
import com.mili.manito.R;
import com.mili.manito.Utilities.PersianDigitConverter;
import com.mili.manito.Utilities.PrefManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BudgetDetailListAdapter extends RecyclerView.Adapter<BudgetDetailListAdapter.MyHolder> {

    private Context mContext;
    private List<BudgetDetailList> budgetDetails;

    public BudgetDetailListAdapter(Context mContext, List<BudgetDetailList> budgetDetails) {
        this.mContext = mContext;
        this.budgetDetails = budgetDetails;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.recycler_list_budget_detail,parent,false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        BudgetDetailList budgetDetail = budgetDetails.get(position);
        int color = Color.parseColor(budgetDetail.getBudCateColor());
        String title = PersianDigitConverter.PersianNumber(budgetDetail.getBudCate());

        Long budget =
               budgetDetail.getBudCateCost();


        Long paid  = budgetDetail.getBudPaid();
        if (PrefManager.getUnitPref().equals(PrefManager.TOOMAN_UNIT_VALUE)){
            budget = budget/10;
            paid = paid/10;
        }

        Long percent= (paid*100)/budget;
        holder.cateTitleBg.setBackgroundColor(color);
        holder.cateTitle.setText(title);
        holder.budCost.setText(PersianDigitConverter.PersianNumber(PersianDigitConverter.NumberFormat(budget)));
        holder.catePaid.setText(PersianDigitConverter.PersianNumber(PersianDigitConverter.NumberFormat(paid)));
        holder.budPercent.setText(
                mContext.getResources().getString(R.string.percent_persian_symbol).concat(
                PersianDigitConverter.PersianNumber(
                        PersianDigitConverter.NumberFormat(percent)
                )
             )
        );
        holder.budPercent.setTextColor(getPercentColor(percent));
    }

    @Override
    public int getItemCount() {
        return budgetDetails.size();
    }

    public List<BudgetDetailList> getBudgetDetails() {
        return budgetDetails;
    }

    public void setBudgetDetails(List<BudgetDetailList> budgetDetails) {
        this.budgetDetails.clear();
        this.budgetDetails.addAll(budgetDetails);
        notifyDataSetChanged();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView cateTitleBg, cateTitle, budCost, catePaid, budPercent;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            cateTitleBg = itemView.findViewById(R.id.bud_cate_bg_txt);
            cateTitle = itemView.findViewById(R.id.bud_catetitle_txt);
            budCost = itemView.findViewById(R.id.bud_budCost_txt);
            catePaid  = itemView.findViewById(R.id.bud_paid_txt);
            budPercent = itemView.findViewById(R.id.bud_percent_txt);
        }
    }

    private static int getPercentColor(final Long percent){
        if (percent>100){
            return Color.parseColor("#F44336");
        }else if (percent<100) {
            return Color.parseColor("#03A9F4");
        }else{
            return Color.parseColor("#4CAF50");
        }
    }


}
