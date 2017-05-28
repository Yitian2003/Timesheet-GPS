package com.witlife.timesheet.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.witlife.timesheet.R;
import com.witlife.timesheet.adapter.ViewPagerAdapter;
import com.witlife.timesheet.model.JobModel;
import com.witlife.timesheet.model.UserProfileModel;
import com.witlife.timesheet.util.DateUtil;
import com.witlife.timesheet.util.SPUtil;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity {

    TabLayout tlUserProfileTabs;
    TreeMap<Date, List<JobModel>> jobMap;
    TextView tvHoursWeek;
    TextView tvHoursToday;
    TextView tvUsername;
    TextView tvEmail;
    TextView tvRole;
    CircleImageView ivProfilePhoto;
    Button btnEditProfile;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    FloatingActionButton btnAdd;

    private int PICK_IMAGE_REQUEST = 68;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialView();
        setOnListener();
    }

    private void initialView() {
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        tvHoursToday = (TextView) findViewById(R.id.tvHoursToday);
        tvHoursWeek = (TextView) findViewById(R.id.tvHoursWeek);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvEmail = (TextView)findViewById(R.id.tvEmail);
        tvRole = (TextView)findViewById(R.id.tvRole);
        btnEditProfile = (Button) findViewById(R.id.btnEditProfile);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tlUserProfileTabs = (TabLayout) findViewById(R.id.tlUserProfileTabs);
        ivProfilePhoto = (CircleImageView) findViewById(R.id.ivProfilePhoto);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tlUserProfileTabs.setupWithViewPager(viewPager);
        setupTabs();
        setupProfile();

        jobMap = SPUtil.readFromSharePreferences(getApplicationContext());

        tvHoursToday.setText(String.format("%.2f", calculateTodayHours()));
        // set weekly total hours
        tvHoursWeek.setText(String.format("%.2f", calculateWeekHours()));
    }

    private void setOnListener() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(MainActivity.this, AddJobActivity.class);
                startActivity(intent);
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        ivProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Select Profile Photo"), PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                ivProfilePhoto.setImageBitmap(bitmap);
                UserProfileModel userModel = SPUtil.readUserModel(this);
                userModel.setImageString(uri.toString());
                SPUtil.writeUserModel(this, userModel);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupProfile(){
        UserProfileModel userModel = SPUtil.readUserModel(this);
        if (userModel != null) {
            tvUsername.setText(userModel.getFirstName() + " " + userModel.getLastName());
            tvEmail.setText(userModel.getEmail());
            tvRole.setText(userModel.getRole());
            if (userModel.getImageString() != null){

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),
                            Uri.parse(userModel.getImageString()));

                    ivProfilePhoto.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setupTabs() {
        tlUserProfileTabs.getTabAt(0).setIcon(R.drawable.ic_date_range_white_24dp);
        tlUserProfileTabs.getTabAt(1).setIcon(R.drawable.ic_list_white_24dp);
        tlUserProfileTabs.getTabAt(2).setIcon(R.drawable.ic_location_on_white_24dp);
        tlUserProfileTabs.getTabAt(3).setIcon(R.drawable.ic_send_white_24dp);
    }

    private double calculateTodayHours(){
        Date currentTime = Calendar.getInstance().getTime();
        double total = 0.0;
        for (Date date: jobMap.keySet()) {
            if (DateUtil.isSameDay(currentTime, date)) {
                List<JobModel> hoursList = jobMap.get(date);

                for (int i = 0; i < hoursList.size(); i++) {
                    if (!TextUtils.isEmpty(hoursList.get(i).getTotalHours())) {
                        total += Double.parseDouble(hoursList.get(i).getContractHours());
                    }
                }
            }
        }
        return total;
    }

    private double calculateWeekHours(){
        Date currentDate = Calendar.getInstance().getTime();
        double total = 0.0;
        for (Date date: jobMap.keySet()){
            if(DateUtil.isSameWeek(currentDate, date)){
                List<JobModel> hoursList = jobMap.get(date);

                for (int i = 0;i < hoursList.size(); i++){
                    if (!TextUtils.isEmpty((hoursList.get(i).getTotalHours()))) {
                        total += Double.parseDouble(hoursList.get(i).getContractHours());
                    }
                }
            }
        }
        return total;
    }

}
