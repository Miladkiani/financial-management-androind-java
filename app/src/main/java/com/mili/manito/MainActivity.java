package com.mili.manito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements  BottomNavigationView.OnNavigationItemSelectedListener{


    public final static String INSTANCE_USER_ID = "user_id";
    private final static String INSTANCE_FRAG_ID = "frag_id";


    private  BottomNavigationView  bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);*/

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState!= null && savedInstanceState.containsKey(INSTANCE_FRAG_ID))
        {
            bottomNavigation.setSelectedItemId(savedInstanceState.getInt(INSTANCE_FRAG_ID));
        }else{
            bottomNavigation.setSelectedItemId(R.id.nav_trans);
        }

    }


    private boolean loadFragment(Fragment fragment){
        if (fragment!=null){
           FragmentManager fragmentManager =  getSupportFragmentManager();
               FragmentTransaction ft =  fragmentManager.beginTransaction();
                    ft.replace(R.id.main_container,fragment).commit();
            return true;

        }
        return false;
    }








    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INSTANCE_FRAG_ID,bottomNavigation.getSelectedItemId());

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.nav_trans:
                fragment = new TranFrag();
               // getSupportActionBar().setTitle(getResources().getString(R.string.nav_trans));
                break;
            case R.id.nav_account:
                fragment = new AccFrag();
                //getSupportActionBar().setTitle(getResources().getString(R.string.nav_account));
                break;
            case R.id.nav_report:
                fragment = new ReportFrag();
                //getSupportActionBar().setTitle(getResources().getString(R.string.nav_report));
                break;
            case  R.id.nav_loan:
                fragment = new LoanFrag();
                //getSupportActionBar().setTitle(getResources().getString(R.string.nav_loan));
                break;
            case R.id.nav_budget:
                fragment = new BudgetFrag();
                //getSupportActionBar().setTitle(getResources().getString(R.string.nav_budget));
        }
        return loadFragment(fragment);
    }



    @Override
    public void onBackPressed() {
        MenuItem homeItem = bottomNavigation.getMenu().getItem(2);

        if (bottomNavigation.getSelectedItemId() != homeItem.getItemId()) {

            loadFragment(new TagFrag());

            // Select home item
            bottomNavigation.setSelectedItemId(homeItem.getItemId());
        } else {
            super.onBackPressed();
            moveTaskToBack(true);

        }
    }


}
