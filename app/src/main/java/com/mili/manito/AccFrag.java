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
import com.google.android.material.tabs.TabLayout;
import com.mili.manito.Adapter.AccountListAdapter;
import com.mili.manito.Model.AccountEntity;
import com.mili.manito.Model.AccountList;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Thread.AppExecutors;
import com.mili.manito.Utilities.MyItemDecoration;
import com.mili.manito.Utilities.PersianDigitConverter;
import com.mili.manito.Utilities.PrefManager;
import com.mili.manito.ViewModel.AccVM;
import com.mili.manito.ViewModel.AccVMF;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccFrag extends Fragment {


    private RecyclerView recyclerView;
    private AccountListAdapter accountListAdapter;
    private AccVM viewModel;
    private TabLayout tabLayout;
    private TextView sumTxt , emptyText;
    private MyDatabase sDB;
    private SharedPreferences preferences;
    private static final String INSTANCE_ACCOUNT_ID = "acc_id";
    private FloatingActionButton fab;

    public AccFrag() {
        // Required empty public constructor
    }

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener
            = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            if (key.equals(PrefManager.UNIT_PREF_ITEM)){
                if (tabLayout.getSelectedTabPosition() == 0){
                    viewModel.setCondition(AccountEntity.ACCOUNT_BLOCKED);
                }else if (tabLayout.getSelectedTabPosition() == 1){
                    viewModel.setCondition(AccountEntity.ACCOUNT_ACTIVE);
                }else if (tabLayout.getSelectedTabPosition() == 2){
                    viewModel.setCondition(-1);
                }
            }



        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =
                inflater.inflate(R.layout.fr_account, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view_acc);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        sumTxt = rootView.findViewById(R.id.sum_balance_acc_txt);
        emptyText = rootView.findViewById(R.id.acc_empty_txt);
        fab = rootView.findViewById(R.id.acc_fab);
        TextView  add_acc = rootView.findViewById(R.id.add_transfer_txt);
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).setTitle(R.string.nav_account);
        add_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt(MainActivity.INSTANCE_USER_ID,1);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("transferDialog");
                if (fragment!= null){
                    ft.remove(fragment);
                }
                ft.addToBackStack(null);
                DialogFragment dialogFragment = new Add_Transfer_Frd();
                dialogFragment.setArguments(args);
                dialogFragment.show(ft,"transferDialog");
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt(MainActivity.INSTANCE_USER_ID,1);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("accDialog");
                if (fragment!= null){
                    ft.remove(fragment);
                }
                ft.addToBackStack(null);
                DialogFragment dialogFragment = new Add_Acc_Frd();
                dialogFragment.setArguments(args);
                dialogFragment.show(ft,"accDialog");
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        initAll();
        setupViewModel();
        setupSharedPreferences();
    }

    private void initAll(){


        sDB = MyDatabase.getInstance(getActivity().getApplicationContext());
        setupViewModel();
        accountListAdapter = new AccountListAdapter(getActivity(), new ArrayList<>(), new AccountListAdapter.AccMoreLis() {
            @Override
            public void removeAcc(final int id) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        sDB.accountDao().deleteAccount(id);
                    }
                });
            }

            @Override
            public void updateAcc(int id) {
                Bundle args = new Bundle();
                args.putInt(MainActivity.INSTANCE_USER_ID,1);
                args.putInt(INSTANCE_ACCOUNT_ID,id);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("accDialog");
                if (fragment!= null){
                    ft.remove(fragment);
                }
                ft.addToBackStack(null);
                DialogFragment dialogFragment = new Add_Acc_Frd();
                dialogFragment.setArguments(args);
                dialogFragment.show(ft,"accDialog");
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0){
                    viewModel.setCondition(AccountEntity.ACCOUNT_BLOCKED);
                }else if (tabLayout.getSelectedTabPosition() == 1){
                    viewModel.setCondition(AccountEntity.ACCOUNT_ACTIVE);
                }else if (tabLayout.getSelectedTabPosition() == 2){
                    viewModel.setCondition(-1);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.getTabAt(2).select();
        RecyclerView.LayoutManager mLayoutManager = new
                LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.addItemDecoration(new MyItemDecoration(10));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
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
        recyclerView.setAdapter(accountListAdapter);
    }

    private void setupSharedPreferences(){
        preferences = PrefManager.getSharedPreferences();
        preferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    private void setupViewModel(){
        AccVMF factory = new AccVMF(1,sDB);
        viewModel = ViewModelProviders.of(this,factory).get
                (AccVM.class);
        viewModel.getAccounts().observe(this, new Observer<List<AccountList>>() {
            @Override
            public void onChanged(List<AccountList> accountEntities) {
                if (accountEntities.size()!= 0) {
                    accountListAdapter.setList(accountEntities);
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyText.setVisibility(View.GONE);
                }else{
                    recyclerView.setVisibility(View.GONE);
                    emptyText.setVisibility(View.VISIBLE);
                }

                Long sum = 0L;
                for(AccountList account:accountEntities){
                    sum += account.getAccount_balance();
                }

                if(PrefManager.getUnitPref().equals(PrefManager.TOOMAN_UNIT_VALUE)){
                    sum= sum/10;
                }

                sumTxt.setText(PersianDigitConverter.PersianNumber(
                        PersianDigitConverter.NumberFormat(sum))
                );
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        preferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
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
