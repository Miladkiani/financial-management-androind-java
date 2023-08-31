package com.mili.manito;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.mili.manito.Adapter.LoanListAdapter;
import com.mili.manito.Model.LoanList;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Thread.AppExecutors;
import com.mili.manito.Utilities.MyItemDecoration;
import com.mili.manito.Utilities.PrefManager;
import com.mili.manito.ViewModel.LoanVM;
import com.mili.manito.ViewModel.LoanVMF;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoanFrag extends Fragment {

    private FloatingActionButton fab;
    private MyDatabase sDb;
    TextView emptyText;
    private LoanListAdapter adapter;
    private RecyclerView recyclerView;
     final static String INSTANCE_LOAN_ID = "mLoan_id";

    final static String INSTANCE_REMIND_COUNT = "mRemindCount";

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


    public LoanFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =
                inflater.inflate(R.layout.fr_loan, container, false);
            fab = rootView.findViewById(R.id.loan_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arg = new Bundle();
                arg.putInt(MainActivity.INSTANCE_USER_ID, 1);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("loanDialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                DialogFragment dialogFragment = new Add_Loan_Frd();
                dialogFragment.setArguments(arg);
                dialogFragment.show(ft, "loanDialog");
            }
        });

         emptyText  = rootView.findViewById(R.id.loan_empty_txt);
        recyclerView = rootView.findViewById(R.id.loan_recycler_view);
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).setTitle(R.string.nav_loan);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        sDb = MyDatabase.getInstance(getActivity().getApplicationContext());
        adapter = new LoanListAdapter(getContext(), new ArrayList<>(), new LoanListAdapter.MoreListener() {
            @Override
            public void updateLoan(int id) {
                Bundle arg = new Bundle();
                arg.putInt(MainActivity.INSTANCE_USER_ID, 1);
                arg.putInt(INSTANCE_LOAN_ID,id);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("loanDialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                DialogFragment dialogFragment = new Add_Loan_Frd();
                dialogFragment.setArguments(arg);
                dialogFragment.show(ft, "loanDialog");
            }

            @Override
            public void removeLoan(int id) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        sDb.loanDao().deleteById(id);
                    }
                });
            }

            @Override
            public void payLoan(int id) {
                        Bundle arg = new Bundle();
                        arg.putInt(MainActivity.INSTANCE_USER_ID, 1);
                        arg.putInt(INSTANCE_LOAN_ID,id);
                        FragmentTransaction ft = getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction();
                        Fragment prev = getActivity()
                                .getSupportFragmentManager()
                                .findFragmentByTag("payLoanDialog");
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        ft.addToBackStack(null);
                        DialogFragment dialogFragment = new Loan_Pay_Frd();
                        dialogFragment.setArguments(arg);
                        dialogFragment.show(ft, "payLoanDialog");
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

    private void setupViewModel() {

        LoanVMF viewModelFactory = new LoanVMF
                (sDb, 1);
        LoanVM viewModel = ViewModelProviders.of(this, viewModelFactory).get
                (LoanVM.class);
        viewModel.getLoans().observe(this, new Observer<List<LoanList>>() {
            @Override
            public void onChanged(List<LoanList> loanLists) {
                adapter.setData(loanLists);
                if (loanLists.size()!=0){
                    emptyText.setVisibility(View.GONE);
                }else{
                    emptyText.setVisibility(View.VISIBLE);
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
