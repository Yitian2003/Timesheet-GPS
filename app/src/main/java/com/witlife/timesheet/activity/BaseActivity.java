package com.witlife.timesheet.activity;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.witlife.timesheet.R;

/**
 * Created by yitian on 5/05/2017.
 */

public class BaseActivity extends AppCompatActivity {

    @Nullable
    Toolbar toolbar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        bindViews();
        setupToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }

    private void bindViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void setupToolbar(){
        if (toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public Toolbar getToolbar() {return toolbar;}
}
