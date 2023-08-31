package com.mili.manito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mili.manito.Adapter.TagListAdapter;
import com.mili.manito.Model.LabelsEntity;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Thread.AppExecutors;
import com.mili.manito.ViewModel.TagVM;

import java.util.ArrayList;
import java.util.List;

public class TagsActivity extends AppCompatActivity {

    public static final String INSTANCE_TAG_ID = "mTag_id";
    private  TagListAdapter adapter;
    private MyDatabase mDb;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        Toolbar toolbar = findViewById(R.id.tag_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDb = MyDatabase.getInstance(getApplicationContext());
         fab = findViewById(R.id.tag_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(TagsActivity.this,EditTagsAc.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.tag_recycler_view);
        adapter = new TagListAdapter(this, new ArrayList<>(), new TagListAdapter.Listener() {
            @Override
            public void onRemoveLabel(int id) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.labelsDao().deleteLabel(id);
                    }
                });

            }

            @Override
            public void onUpdateLabel(int id) {
                Intent intent = new Intent(TagsActivity.this,EditTagsAc.class);
                intent.putExtra(INSTANCE_TAG_ID,id);
                startActivity(intent);
            }
        });
                RecyclerView.LayoutManager mLayoutManager = new
                LinearLayoutManager(this.getApplicationContext());

        DividerItemDecoration itemDecor = new DividerItemDecoration
                (this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
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
    }

    private void setupViewModel(){
        TagVM viewModel =
                ViewModelProviders.of(this).get(TagVM.class);
        viewModel.getLabels().observe(this, new Observer<List<LabelsEntity>>() {
            @Override
            public void onChanged(List<LabelsEntity> labelsEntities) {
                adapter.setList(labelsEntities);
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
