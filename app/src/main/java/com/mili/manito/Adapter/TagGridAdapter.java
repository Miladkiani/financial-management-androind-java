package com.mili.manito.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mili.manito.Model.LabelsEntity;
import com.mili.manito.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TagGridAdapter extends RecyclerView.Adapter<TagGridAdapter.TagHolder> {
    private Context mContext;
    private List<LabelsEntity> labelsEntities;
    private BottomSheetTagListAdapter.Listener mListener;

    public TagGridAdapter
            (Context mContext, List<LabelsEntity> labelsEntities) {
        this.mContext = mContext;
        this.labelsEntities = labelsEntities;
    }

    class TagHolder extends RecyclerView.ViewHolder{
         TextView tagName;
         ImageButton clearTag;
         LinearLayout linearLayout;
        public RelativeLayout relativeLayout;
        public TagHolder(@NonNull View itemView) {
            super(itemView);
            tagName = itemView.findViewById(R.id.tag_name);
            clearTag = itemView.findViewById(R.id.clear_tag);
            linearLayout = itemView.findViewById(R.id.linear_tag);
        }
    }


    public void setEntity(LabelsEntity labelsEntity ){
        this.labelsEntities.add(labelsEntity);
        notifyDataSetChanged();
    }

        public void setList(List<LabelsEntity> list){
        this.labelsEntities.clear();
        labelsEntities.addAll(list);
        notifyDataSetChanged();
        }

        public List<LabelsEntity> getList(){
        return labelsEntities;
        }

    @Override
    public long getItemId(int position) {
        return
                labelsEntities.get(position).getLabel_id();
    }

    @NonNull
    @Override
    public TagHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView= LayoutInflater.from(mContext).
                inflate(R.layout.grid_list_tag,parent,false);

        return  new TagHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull TagHolder holder, int position) {
        LabelsEntity labelsEntity = labelsEntities.get(position);
        holder.tagName.setText(labelsEntity.getLabel_name());

        GradientDrawable bg_color = (GradientDrawable)holder.linearLayout.getBackground();

        bg_color.setColor(Color.parseColor(
                labelsEntity.getLabel_color()
        ));

        holder.clearTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                labelsEntities.remove(labelsEntity);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return labelsEntities.size();
    }


}
