package com.mili.manito.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.mili.manito.Model.LabelsEntity;
import com.mili.manito.R;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TagListAdapter extends RecyclerView.Adapter<TagListAdapter.MyHolder> {
    private Context mContext;
    private List<LabelsEntity> labelsEntities;
    private Listener mListener;
    private static final HashMap<String,Integer> BG_IMAGE = new HashMap<String, Integer>() {
        {
            put("#2196F3", R.drawable.label_bl);
            put("#F44336", R.drawable.label_rd);
            put("#4CAF50", R.drawable.label_gr);
            put("#FF5722", R.drawable.label_or);
            put("#9C27B0", R.drawable.label_pr);
            put("#E91E63", R.drawable.label_pi);
            put("#CDDC39", R.drawable.label_li);
            put("#795548", R.drawable.label_br);
        }
    };

    public TagListAdapter(Context mContext, List<LabelsEntity> labelsEntities, Listener mListener) {
        this.mContext = mContext;
        this.labelsEntities = labelsEntities;
        this.mListener = mListener;
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView tagTitle;
        ImageButton tagMore;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tagTitle = itemView.findViewById(R.id.tag_title_txt);
            tagMore = itemView.findViewById(R.id.tag_more_imgBtn);
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext)
                        .inflate(R.layout.recycler_list_tag,parent,false);
        return new MyHolder(rootView);
    }

    public void setList(List<LabelsEntity> list){
        labelsEntities.clear();
        labelsEntities.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        LabelsEntity entity = labelsEntities.get(position);
        String title = entity.getLabel_name();
        String color = entity.getLabel_color();
        holder.tagTitle.setText(title);
        holder.tagTitle.setBackgroundResource(BG_IMAGE.get(color));
        holder.tagMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext,holder.tagMore);
                popupMenu.getMenuInflater().inflate(R.menu.tran_more_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        switch (itemId ){
                            case R.id.remove_item:
                                mListener.onRemoveLabel(entity.getLabel_id());
                                return true;
                            case R.id.update_item:
                                mListener.onUpdateLabel(entity.getLabel_id());
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
        return labelsEntities.size();
    }


    public interface Listener{
        void onRemoveLabel(int id);
        void onUpdateLabel(int id);
    }
}
