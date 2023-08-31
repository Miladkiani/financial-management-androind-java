package com.mili.manito.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mili.manito.Model.LabelsEntity;
import com.mili.manito.R;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BottomSheetTagListAdapter extends RecyclerView.Adapter<BottomSheetTagListAdapter.TagHolder> {
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


    public BottomSheetTagListAdapter(Context mContext, List<LabelsEntity> labelsEntities, Listener listener) {
        this.mContext = mContext;
        this.labelsEntities = labelsEntities;
        this.mListener  = listener;

    }

    class TagHolder extends RecyclerView.ViewHolder{
         TextView tagName;
         RelativeLayout relativeLayout;
         TagHolder(@NonNull View itemView) {
            super(itemView);
            tagName = itemView.findViewById(R.id.tag_name_txtView);

            relativeLayout = itemView.findViewById(R.id.relative);
        }
    }


    private static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
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
                inflate(R.layout.bottomsheet_list_tag,parent,false);

        return  new TagHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull TagHolder holder, int position) {
        LabelsEntity labelsEntity = labelsEntities.get(position);
        holder.tagName.setText(labelsEntity.getLabel_name());
            holder.tagName.setBackgroundResource(BG_IMAGE.get(labelsEntity.getLabel_color()));
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(labelsEntity);
            }
        });

    }

    @Override
    public int getItemCount() {
        return labelsEntities.size();
    }

    public interface Listener{
        void onItemClick(LabelsEntity labelsEntity);
    }

}
