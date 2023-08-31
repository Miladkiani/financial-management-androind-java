package com.mili.manito;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mili.manito.Adapter.BudgetListAdapter;
import com.mili.manito.Model.BudgetEntity;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Thread.AppExecutors;
import com.mili.manito.Utilities.MyItemDecoration;
import com.mili.manito.Utilities.PrefManager;
import com.mili.manito.ViewModel.BudVM;
import com.mili.manito.ViewModel.BudVMF;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BudgetFrag extends Fragment {


    public final static String INSTANCE_BUDGET_ID = "myBud_id";
    private RecyclerView recyclerView;
    private BudgetListAdapter adapter;
    private TextView emptyTxt;
    private MyDatabase mDb;
    private FloatingActionButton fab;
    private SharedPreferences preferences;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener
            = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            if (key.equals(PrefManager.UNIT_PREF_ITEM)){
               setupViewModel();
            }
        }
    };

    public BudgetFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =
                inflater.inflate(R.layout.fr_budget, container, false);
         fab = rootView.findViewById(R.id.budget_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),EditBudgetAc.class);
                startActivity(intent);
            }
        });
        emptyTxt = rootView.findViewById(R.id.budget_empty_txt);
        recyclerView = rootView.findViewById(R.id.budget_recycler_view);
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).setTitle(R.string.nav_budget);
        return  rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        adapter = new BudgetListAdapter(getContext(), new ArrayList<>(), new BudgetListAdapter.Listener() {
            @Override
            public void onShow(int budId) {
                Intent intent = new Intent(getActivity(),DetailBudgetAc.class);
                intent.putExtra(INSTANCE_BUDGET_ID,budId);
                startActivity(intent);
            }

            @Override
            public void onUpdate(int budId) {
                Intent intent = new Intent(getActivity(),EditBudgetAc.class);
                intent.putExtra(INSTANCE_BUDGET_ID,budId);
                startActivity(intent);
            }

            @Override
            public void onRemove(int budId) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.budgetDao().removeBudById(budId);
                    }
                });
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new
                LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.addItemDecoration(new MyItemDecoration(10));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });
        setupViewModel();
        setupSharedPreferences();
    }

    private void setupViewModel(){
        mDb = MyDatabase.getInstance(getActivity().getApplicationContext());
        BudVMF factory = new BudVMF(mDb,1);
        BudVM  viewModel = ViewModelProviders.of(this,factory).get(BudVM.class);
        viewModel.getBudgets().observe(this, new Observer<List<BudgetEntity>>() {
            @Override
            public void onChanged(List<BudgetEntity> budgetEntities) {
                adapter.setList(budgetEntities);
                if (budgetEntities.size()!=0){
                    emptyTxt.setVisibility(View.GONE);
                }else{
                    emptyTxt.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        preferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    private void setupSharedPreferences(){
        preferences = PrefManager.getSharedPreferences();
        preferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.app_bar_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.tags_item_menu:
                Intent intent = new Intent(getActivity(),TagsActivity.class);
                startActivity(intent);
                return true;
            case R.id.settings_item_menu:
                Intent intent2 = new Intent(getActivity(),SettingsActivity.class);
                startActivity(intent2);
                return true;
            case R.id.about_item_menu:
                Intent intent3 = new Intent(getActivity(),AboutActivity.class);
                startActivity(intent3);
                return true;
        }
        return false;
    }
}
