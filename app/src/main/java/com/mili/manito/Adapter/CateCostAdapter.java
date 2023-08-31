package com.mili.manito.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mili.manito.Model.CategoryEntity;
import com.mili.manito.R;
import com.mili.manito.Utilities.PersianDigitConverter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CateCostAdapter extends RecyclerView.Adapter<CateCostAdapter.MyHolder> {

    private Context mContext;
    private List<CategoryEntity> mCateCostList;

    public CateCostAdapter(Context mContext, List<CategoryEntity> cateCosts) {
        this.mContext = mContext;
        this.mCateCostList = cateCosts;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).
                    inflate(R.layout.recycler_list_catecost,parent,false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        CategoryEntity cateCost = mCateCostList.get(position);

        Long cost = cateCost.getCategory_cost();
        holder.cate_cost.setText(PersianDigitConverter.PersianNumber(
                PersianDigitConverter.NumberFormat(cost)
        ));

        String cateName = cateCost.getCategory_name();
        int categoryColor = Color.parseColor(cateCost.getCategory_color());
        GradientDrawable bgNameTitle = (GradientDrawable)holder.cate_title.getBackground();
        bgNameTitle.setColor(categoryColor);

        holder.cate_title.setText(cateName.substring(0,1));
        holder.cate_name.setText(cateName);

        holder.cate_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCateCostList.remove(cateCost);
                notifyDataSetChanged();
            }
        });
    }

    public void removeCateCost(int position){
        mCateCostList.remove(position);
        notifyDataSetChanged();
    }

    public int addCateCost(CategoryEntity categoryEntity, Long max)
    {
        if (checkPossible(categoryEntity.getCategory_cost() , max)) {
            mCateCostList.add(categoryEntity);
            notifyDataSetChanged();
            Toast.makeText(mContext,"اضافه شد",Toast.LENGTH_SHORT).show();
            return 1;
        }else{
            return -1;
        }
    }

    public void addCateCost(CategoryEntity categoryEntity){
        mCateCostList.add(categoryEntity);
        notifyDataSetChanged();
    }

    private boolean checkPossible(Long cost, Long max){
        Long sum  =sumOfCosts();
            return  (cost+sum)<=max ;
    }

    public List<CategoryEntity> getmCateCostList() {
        return mCateCostList;
    }

    private Long sumOfCosts(){
        Long sum = 0L ;
        for (CategoryEntity c: mCateCostList){
            sum += c.getCategory_cost();
        }
        return sum;
    }

    @Override
    public int getItemCount() {
        return mCateCostList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView cate_title, cate_name, cate_cost;
        ImageButton cate_clear;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            cate_title = itemView.findViewById(R.id.cate_title_txt);
            cate_name = itemView.findViewById(R.id.cate_name_txt);
            cate_cost = itemView.findViewById(R.id.cate_cost_txt);
            cate_clear = itemView.findViewById(R.id.clear_cateCost_imgBtn);
        }
    }
}
