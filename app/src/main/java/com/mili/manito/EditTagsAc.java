package com.mili.manito;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mili.manito.Model.LabelsEntity;
import com.mili.manito.Model.MyDatabase;
import com.mili.manito.Thread.AppExecutors;
import com.mili.manito.ViewModel.EditTagVM;
import com.mili.manito.ViewModel.EditTagVMF;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class EditTagsAc extends AppCompatActivity {
        private final static String CHECK_SYMBOLE = "\u2713";
        private final static String INSTANCE_COLOR_ID = "color_id";
        private final static int DEFAULT_ID = -1;
        private  TextView activeTxt;
        private EditText tagTitle;
        private int mTagId= DEFAULT_ID;
        private MyDatabase mDb;
        private final static HashMap<String,String> COLOR_CODE = new HashMap<String, String>(){
            {
                        put("blue","#2196F3");
                        put("red","#F44336");
                        put("green","#4CAF50");
                        put("purple","#9C27B0");
                        put("lime","#CDDC39");
                        put("pink","#E91E63");
                        put("orange","#FF5722");
                        put("brown","#795548");
            }
        };
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
      String mTagColor ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tags);
        Toolbar toolbar = findViewById(R.id.tag_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        tagTitle = findViewById(R.id.tag_title_etxt);
        TextView blueTag = findViewById(R.id.tag_blue_txt);
        TextView redTag = findViewById(R.id.tag_red_txt);
        TextView orangeTag = findViewById(R.id.tag_orange_txt);
        TextView limeTag = findViewById(R.id.tag_lime_txt);
        TextView greenTag = findViewById(R.id.tag_green_txt);
        TextView pinkTag = findViewById(R.id.tag_pink_txt);
        TextView purpleTag = findViewById(R.id.tag_purple_txt);
        TextView brownTag = findViewById(R.id.tag_brown_txt);

        blueTag.setOnClickListener(onClickListener);
        redTag.setOnClickListener(onClickListener);
        limeTag.setOnClickListener(onClickListener);
        greenTag.setOnClickListener(onClickListener);
        orangeTag.setOnClickListener(onClickListener);
        brownTag.setOnClickListener(onClickListener);
        pinkTag.setOnClickListener(onClickListener);
        purpleTag.setOnClickListener(onClickListener);

        mDb  = MyDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_COLOR_ID)) {
            activeTxt =
                    findViewById(savedInstanceState.getInt(INSTANCE_COLOR_ID));
            setColor(activeTxt.getId());
        } else {
            setColor(blueTag.getId());
        }
        if (savedInstanceState!= null &&
                savedInstanceState.containsKey(TagsActivity.INSTANCE_TAG_ID) )
        {
            mTagId = savedInstanceState.getInt(TagsActivity.INSTANCE_TAG_ID,DEFAULT_ID);
        }

        if (mTagId == DEFAULT_ID){
           Bundle b =  getIntent().getExtras();
           if (b!=null && b.containsKey(TagsActivity.INSTANCE_TAG_ID)) {
             mTagId = b.getInt(TagsActivity.INSTANCE_TAG_ID, DEFAULT_ID);
               EditTagVMF factory = new EditTagVMF(mDb,mTagId);
               EditTagVM viewModel =
                       ViewModelProviders.of(this,factory).get(EditTagVM.class);

               viewModel.getLabel().observe(this, new Observer<LabelsEntity>() {
                   @Override
                   public void onChanged(LabelsEntity labelsEntity) {
                       viewModel.getLabel().removeObserver(this);
                       tagTitle.setText(labelsEntity.getLabel_name());
                       int image = BG_IMAGE.get(labelsEntity.getLabel_color());
                       switch (image){
                           case R.drawable.label_bl:
                               setColor(blueTag.getId());
                               break;
                           case R.drawable.label_rd:
                               setColor(redTag.getId());
                               break;
                           case R.drawable.label_br:
                               setColor(brownTag.getId());
                               break;
                           case R.drawable.label_gr:
                               setColor(greenTag.getId());
                               break;
                           case R.drawable.label_or:
                               setColor(orangeTag.getId());
                               break;
                           case R.drawable.label_li:
                               setColor(limeTag.getId());
                               break;
                           case R.drawable.label_pi:
                               setColor(pinkTag.getId());
                               break;
                           case R.drawable.label_pr:
                               setColor(purpleTag.getId());
                               break;
                       }
                   }
               });
           }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_budget_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                finish();
                return true;
            case R.id.done:
                insertTag();
                return true;
        }
        return false;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          TextView t  =  (TextView)v;
          setColor(t.getId());
        }
    } ;

    private void setColor(int colorTxt){
        if (activeTxt !=null ){
            activeTxt.setText("");
        }
        switch (colorTxt){
            case R.id.tag_blue_txt:
                mTagColor = COLOR_CODE.get("blue");
                activeTxt = findViewById(R.id.tag_blue_txt);
                activeTxt.setText(CHECK_SYMBOLE);
                break;
            case R.id.tag_red_txt:
                mTagColor = COLOR_CODE.get("red");
                activeTxt = findViewById(R.id.tag_red_txt);
                activeTxt.setText(CHECK_SYMBOLE);
                break;
            case R.id.tag_brown_txt:
                mTagColor = COLOR_CODE.get("brown");
                activeTxt = findViewById(R.id.tag_brown_txt);
                activeTxt.setText(CHECK_SYMBOLE);
                break;
            case R.id.tag_orange_txt:
                mTagColor = COLOR_CODE.get("orange");
                activeTxt = findViewById(R.id.tag_orange_txt);
                activeTxt.setText(CHECK_SYMBOLE);
                break;
            case R.id.tag_green_txt:
                mTagColor = COLOR_CODE.get("green");
                activeTxt = findViewById(R.id.tag_green_txt);
                activeTxt.setText(CHECK_SYMBOLE);
                break;
            case R.id.tag_lime_txt:
                mTagColor = COLOR_CODE.get("lime");
                activeTxt = findViewById(R.id.tag_lime_txt);
                activeTxt.setText(CHECK_SYMBOLE);
                break;
            case R.id.tag_pink_txt:
                mTagColor = COLOR_CODE.get("pink");
                activeTxt = findViewById(R.id.tag_pink_txt);
                activeTxt.setText(CHECK_SYMBOLE);
                break;
            case R.id.tag_purple_txt:
                mTagColor = COLOR_CODE.get("purple");
                activeTxt = findViewById(R.id.tag_purple_txt);
                activeTxt.setText(CHECK_SYMBOLE);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INSTANCE_COLOR_ID,activeTxt.getId());
        outState.putInt(TagsActivity.INSTANCE_TAG_ID,mTagId);
    }

    private void insertTag(){
        if (checkInput()){
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    String tagName = tagTitle.getText().toString();
                    LabelsEntity labelsEntity = new LabelsEntity(tagName,1,mTagColor);
                    if (mTagId == DEFAULT_ID) {
                        mDb.labelsDao().insert(labelsEntity);
                        finish();
                    }else{
                        labelsEntity.setLabel_id(mTagId);
                        mDb.labelsDao().update(labelsEntity);
                        finish();
                    }
                }
            });
        }
    }

    private boolean checkInput(){
        return (!tagTitle.getText().toString().matches("")
                && mTagColor != null );
    }
}
