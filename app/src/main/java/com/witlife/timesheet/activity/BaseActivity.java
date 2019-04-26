package com.witlife.timesheet.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

    AlertDialog firstAlertDialogWithoutCheck;
    AlertDialog finalAlertDialog;
    private String TAG = BaseActivity.class.getName();
    private final int PERMISSION_REQUEST_CODE = 0;
    private int REQUEST_PERMISSION_SETTING = 1;
    String mPermission = null;

    //** to set permission **//
    public void askPermissionFor(String mPermission) {
        this.mPermission = mPermission;
    }

    //** check permission is granted or not **//
    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(mPermission) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    //** request permossion to user **//
    public void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{mPermission},
                PERMISSION_REQUEST_CODE);
    }


    //** get permission result **//
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted :)

                } else {
                    // permission was not granted
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, mPermission)) {
                        firstAlertDialogWithoutCheck();
                    } else {
                        finalalertDialog();
                    }
                }
                break;
        }
    }

    //** display dialog with warning **//
    public void firstAlertDialogWithoutCheck() {
        firstAlertDialogWithoutCheck = new AlertDialog.Builder(this)
                .setTitle("Permission denied")
                .setCancelable(false)
                .setMessage("Without " + mPermission + " Permission the app will not work. Are you sure you want to deny this permission?")
                .setPositiveButton("RE-TRY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        requestPermission();
                    }
                })
                .setNegativeButton("I'M SURE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    //** Display alertDialog with setting option to user **//
//** Handle "Never ask Again" **//
    public void finalalertDialog() {
        if (firstAlertDialogWithoutCheck != null) {
            firstAlertDialogWithoutCheck.dismiss();
        }
        finalAlertDialog = new AlertDialog.Builder(this)
                .setTitle("Permission denied")
                .setCancelable(false)
                .setMessage("Without " + mPermission + " Permission the app will not work. Click Settings to go to App settings to let you do so.")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finishAffinity();
                    }
                })
                .setNegativeButton("Setting", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
