package com.mili.manito.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;


import com.mili.manito.Model.BudgetEntity;
import com.mili.manito.R;
import com.mili.manito.Utilities.DateUtil;
import com.mili.manito.Utilities.PersianDigitConverter;
import com.mili.manito.Utilities.PrefManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BudgetListAdapter extends RecyclerView.Adapter<BudgetListAdapter.MyHolder> {
    private Context mContext;
    private List<BudgetEntity> budgetEntities;
    private Listener mListener;
    public BudgetListAdapter(Context mContext, List<BudgetEntity> budgetEntities, Listener listener) {
        this.mContext = mContext;
        this.budgetEntities = budgetEntities;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).
                inflate(R.layout.recycler_list_budget,parent,false);
        return new MyHolder(rootView);
    }

    public void setList(List<BudgetEntity> list){
        budgetEntities.clear();
        budgetEntities.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        BudgetEntity budgetEntity = budgetEntities.get(position);



        int budId = budgetEntity.getBudget_id();
        String title = budgetEntity.getBudget_title();
        Long cost = budgetEntity.getBudget_cost();
        String sDate = DateUtil.toStringStandard(budgetEntity.getBudget_start_time().getTimeInMillis());
        String eDate = DateUtil.toStringStandard(budgetEntity.getBudget_end_time().getTimeInMillis());

        holder.titleBudget.setText(
                PersianDigitConverter.PersianNumber(title));

        if (PrefManager.getUnitPref().equals(PrefManager.TOOMAN_UNIT_VALUE)){
            cost = cost/10;
        }

        holder.maxBudget.setText(
                PersianDigitConverter.PersianNumber(PersianDigitConverter.NumberFormat(cost)
                        .concat(" "+PrefManager.getUnitPref())));
        holder.sDateBudget.setText(sDate);
        holder.eDateBudget.setText(eDate);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onShow(budgetEntity.getBudget_id());
            }
        });

        holder.budgetMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext,holder.budgetMore);
                popupMenu.getMenuInflater().inflate(R.menu.bud_more_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        switch (itemId ){
                            case R.id.remove_bud_item:
                                mListener.onRemove(budId);
                                return true;
                            case R.id.update_bud_item:
                                    mListener.onUpdate(budId);
                                return true;
                            case R.id.show_bud_item:
                                mListener.onShow(budId);
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
        return budgetEntities.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView titleBudget,maxBudget,
                sDateBudget,eDateBudget;
        ImageButton budgetMore;
        View view;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            titleBudget = itemView.findViewById(R.id.bud_title_txt);
            maxBudget = itemView.findViewById(R.id.bud_max_txt);
            sDateBudget = itemView.findViewById(R.id.bud_sdate_txt);
            eDateBudget = itemView.findViewById(R.id.bud_edate_txt);
            budgetMore = itemView.findViewById(R.id.bud_more_imgBtn);

        }
    }

    public interface Listener{
        void onShow(int budId);
        void onUpdate(int budId);
        void onRemove(int budId);
    }
}
