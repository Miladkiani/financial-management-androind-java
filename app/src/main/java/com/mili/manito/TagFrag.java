package com.mili.manito;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mili.manito.Adapter.BottomSheetTagListAdapter;
import com.mili.manito.Model.LabelsEntity;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Utilities.MyItemDecoration;
import com.mili.manito.ViewModel.TagVM;


import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TagFrag extends BottomSheetDialogFragment {

    private MyDatabase sDb;
    private RecyclerView recyclerView;
    private BottomSheetTagListAdapter adapter;
    private ArrayList<LabelsEntity> list;
    private Listener mListener;

    public static TagFrag newInstance() {
        return new TagFrag();
    }

    public void onButtonPressed(Listener listener) {
        this.mListener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=
                inflater.inflate(R.layout.fr_tag, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view_tag);
        TextView add_tag = rootView.findViewById(R.id.add_tag_txt);
        add_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),EditTagsAc.class);
                startActivityForResult(intent,0);
            }
        });
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sDb = MyDatabase.getInstance(getActivity().getApplicationContext());
        list = new ArrayList<>();



        adapter = new BottomSheetTagListAdapter(getContext(), list, new BottomSheetTagListAdapter.Listener() {
            @Override
            public void onItemClick(LabelsEntity labelsEntity) {
                if(mListener != null ){
                    mListener.onItemClick(labelsEntity);
                }
                dismiss();
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new
                LinearLayoutManager(getActivity().getApplicationContext());

        recyclerView.addItemDecoration(new MyItemDecoration(10));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setAdapter(adapter);
        ;
        TagVM viewModel = ViewModelProviders.of(this).get(TagVM.class);
        viewModel.getLabels().observe(this, new Observer<List<LabelsEntity>>() {
            @Override
            public void onChanged(List<LabelsEntity> labelsEntities) {
                list.clear();
                list.addAll(labelsEntities);
                adapter.notifyDataSetChanged();
            }
        });
    }


    public interface Listener{
        void onItemClick(LabelsEntity labelsEntity);
    }

}
