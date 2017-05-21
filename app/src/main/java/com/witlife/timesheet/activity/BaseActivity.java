package com.witlife.timesheet.activity;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
