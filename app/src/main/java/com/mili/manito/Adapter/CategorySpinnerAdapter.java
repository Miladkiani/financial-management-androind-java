package com.mili.manito.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mili.manito.Model.CategoryEntity;
import com.mili.manito.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CategorySpinnerAdapter extends ArrayAdapter<CategoryEntity> {
    private LayoutInflater flater;
    private Context context;
   // private final int[] arrayColor;

    private List<CategoryEntity> categoryEntities;
    public CategorySpinnerAdapter(@NonNull Activity context, int resource, @NonNull List<CategoryEntity> objects) {
        super(context, resource, objects);
        flater = context.getLayoutInflater();
        this.context = context;
        this.categoryEntities = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CategoryEntity categoryEntity = categoryEntities.get(position);

        if(convertView == null) {
            convertView = flater.inflate(R.layout.spn_list_cate,parent,false);
        }

        TextView txtNameTitle = convertView.findViewById(R.id.cate_name_title_spn_txt);
        TextView txtName = convertView.findViewById(R.id.cate_name_spn_txt);

        int categoryColor = Color.parseColor(categoryEntity.getCategory_color());
        GradientDrawable bgNameTitle = (GradientDrawable)txtNameTitle.getBackground();
        bgNameTitle.setColor(categoryColor);

        txtNameTitle.setText(categoryEntity.getCategory_name().substring(0,1));
        txtName.setText(categoryEntity.getCategory_name());

        return convertView;
    }

    public void setData(List<CategoryEntity> list){
        categoryEntities.clear();
        categoryEntities.addAll(list);
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return categoryEntities.size();
    }

    @Override
    public long getItemId(int position) {
        return categoryEntities.get(position).getCategory_id();
    }



    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        CategoryEntity categoryEntity = categoryEntities.get(position);
        if(convertView == null) {
            convertView = flater.inflate(R.layout.spn_list_cate,parent,false);
        }

        TextView txtNameTitle = convertView.findViewById(R.id.cate_name_title_spn_txt);
        TextView txtName = convertView.findViewById(R.id.cate_name_spn_txt);

        int categoryColor = Color.parseColor(categoryEntity.getCategory_color());
        GradientDrawable bgNameTitle = (GradientDrawable)txtNameTitle.getBackground();
        bgNameTitle.setColor(categoryColor);

        txtNameTitle.setText(categoryEntity.getCategory_name().substring(0,1));
        txtName.setText(categoryEntity.getCategory_name());

        return convertView;

    }
}
