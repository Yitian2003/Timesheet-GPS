package com.witlife.timesheet.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.witlife.timesheet.R;
import com.witlife.timesheet.model.JobModel;
import com.witlife.timesheet.util.DateUtil;
import com.witlife.timesheet.util.SPUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.TreeMap;

public class AddJobActivity extends BaseActivity {

    Button btnGetDate;
    Button btnInputDate;
    Button btnGetStartTime;
    Button btnGetFinishTime;
    Button btnInputStartTime;
    Button btnInputFinishTime;
    Button btnGetLocation;
    Button btnInputLocation;
    EditText editDate;
    EditText editStartTime;
    EditText editFinishTime;
    EditText editLunchMin;
    EditText editServiceHours;
    EditText editJobCode;
    EditText editLocation;
    TextView tvTotal;
    TextView tvContract;
    ProgressBar progressBar;
    MenuItem btnSave;
    MenuItem btnDelete;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Boolean flag = false;
    private JobModel jobModel;
    private int position = -1;
    private List<JobModel> jobs;
    private TreeMap<Date, List<JobModel>> jobMap;
    private boolean isHide = false;
    public static String SAVE_JOB_MODEL = "Job Model";
    public static String EDIT_JOB = "EDIT_JOB";
    public static String POSITION = "POSITION";
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        bindView();

        initialView();
        setListener();
    }

    private void bindView() {
        btnGetDate = (Button) findViewById(R.id.btnGetDate);
        btnGetStartTime = (Button) findViewById(R.id.btnGetStartTime);
        btnGetFinishTime = (Button) findViewById(R.id.btnGetFinishTime);
        btnGetLocation = (Button) findViewById(R.id.btnGetLocation);
        btnInputDate = (Button) findViewById(R.id.btnInputDate);
        btnInputStartTime = (Button) findViewById(R.id.btnInputStartTime);
        btnInputFinishTime = (Button) findViewById(R.id.btnInputFinishTime);
        btnInputLocation = (Button) findViewById(R.id.btnInputLocation);

        editDate = (EditText) findViewById(R.id.editDate);
        editStartTime = (EditText) findViewById(R.id.editStart);
        editFinishTime = (EditText) findViewById(R.id.editFinish);
        editLocation = (EditText) findViewById(R.id.editLocation);
        editServiceHours = (EditText) findViewById(R.id.editService);
        editLunchMin = (EditText) findViewById(R.id.editLunch);
        editJobCode = (EditText) findViewById(R.id.editJobCode);

        tvTotal = (TextView) findViewById(R.id.tvTotal);
        tvContract = (TextView) findViewById(R.id.tvContract);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
    }

    private void initialView() {

        progressBar.setVisibility(View.INVISIBLE);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        jobModel = (JobModel) getIntent().getSerializableExtra(EDIT_JOB);

        if (jobModel == null) {
            jobModel = new JobModel();

            Date currentDate = Calendar.getInstance().getTime();
            editDate.setText(DateUtil.getDate(currentDate));
            editStartTime.setText(DateUtil.getTime(currentDate));

            jobModel.setDateString(DateUtil.getDate(currentDate));
            jobModel.setDayInWeek(DateUtil.getDayOfWeek(currentDate));
            jobModel.setStartTime(DateUtil.getTime(currentDate));

            isHide = true;
            invalidateOptionsMenu();
            initialAppBar("Add Record");
        } else {
            position = getIntent().getIntExtra(POSITION, -1);

            editDate.setText(jobModel.getDateString());
            editStartTime.setText(jobModel.getStartTime());
            if (!TextUtils.isEmpty(jobModel.getFinishTime())) {
                editFinishTime.setText(jobModel.getFinishTime());
            }
            if (!TextUtils.isEmpty(jobModel.getLunch())) {
                editLunchMin.setText(jobModel.getLunch());
            }
            if (!TextUtils.isEmpty(jobModel.getTotalHours())) {
                tvTotal.setText(jobModel.getTotalHours());
            }
            if (!TextUtils.isEmpty(jobModel.getContractHours())) {
                tvContract.setText(jobModel.getContractHours());
            }
            if (!TextUtils.isEmpty(jobModel.getServiceHours())) {
                editServiceHours.setText(jobModel.getServiceHours());
            }
            if (!TextUtils.isEmpty(jobModel.getJobCode())) {
                editJobCode.setText(jobModel.getJobCode());
            }
            if (!TextUtils.isEmpty(jobModel.getLocation())) {
                editLocation.setText(jobModel.getLocation());
            }
            initialAppBar("Edit Record");
        }
        jobMap = SPUtil.readFromSharePreferences(getApplicationContext());
    }

    private void initialAppBar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(title);
        }
    }

    private void setListener() {
        btnGetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentDate = Calendar.getInstance().getTime();
                editDate.setText(DateUtil.getDate(currentDate));
                jobModel.setDateString(DateUtil.getDate(currentDate));
                jobModel.setDayInWeek(DateUtil.getDayOfWeek(currentDate));
            }
        });

        btnInputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(999);

            }
        });

        btnGetStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentDate = Calendar.getInstance().getTime();
                editStartTime.setText(DateUtil.getTime(currentDate));
            }
        });

        btnGetFinishTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentDate = Calendar.getInstance().getTime();
                editFinishTime.setText(DateUtil.getTime(currentDate));
            }
        });

        btnInputStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddJobActivity.this, android.R.style.Theme_Material_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        editStartTime.setText(selectedHour + ":" + selectedMinute + ":00");
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        btnInputFinishTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddJobActivity.this, android.R.style.Theme_Material_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        editFinishTime.setText(selectedHour + ":" + selectedMinute + ":00");
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = displayGpsStatus();
                if (flag) {
                    editLocation.setText("Getting Location...");
                    progressBar.setVisibility(View.VISIBLE);
                    locationListener = new MyLocationListener();

                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(AddJobActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                MY_PERMISSION_ACCESS_COARSE_LOCATION);
                    }

                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AddJobActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSION_ACCESS_FINE_LOCATION);
                    }

                    locationManager.requestLocationUpdates(LocationManager
                            .GPS_PROVIDER, 5000, 10, locationListener);

                } else {
                    alertbox("Gps Status!!", "Your GPS is: OFF");
                }
            }
        });

        btnInputLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editLocation.setEnabled(true);
                editLocation.requestFocus();
                editLocation.setSelection(editLocation.getText().length());
            }
        });

        editStartTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setTvTotal();
                setTvContract();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editFinishTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setTvTotal();
                setTvContract();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editLunchMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setTvContract();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        btnSave = menu.findItem(R.id.action_save);
        btnDelete = menu.findItem(R.id.action_delete);
        if (isHide) {
            btnDelete.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                jobModel.setStartTime(editStartTime.getText().toString());
                jobModel.setFinishTime(editFinishTime.getText().toString());
                jobModel.setLunch(editLunchMin.getText().toString());
                jobModel.setTotalHours(tvTotal.getText().toString());
                jobModel.setContractHours(tvContract.getText().toString());
                jobModel.setJobCode(editJobCode.getText().toString());
                jobModel.setLocation(editLocation.getText().toString());
                jobModel.setDate(DateUtil.stringToDate(jobModel.getDayInWeek() + ", " + jobModel.getDateString()));

                editAndCreateJob();

                SPUtil.writeToSharePreferences(getApplicationContext(), jobMap);
                finish();
                Intent intent = new Intent(AddJobActivity.this, MainActivity.class);
                startActivity(intent);

                return true;

            case R.id.action_delete:

                new AlertDialog.Builder(this)
                        .setTitle(R.string.delete_confirm)
                        .setMessage(R.string.delete_message)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(jobMap != null && jobMap.size() > 0) {
                                    List<JobModel> oldJobs = jobMap.get(getKeyInList());
                                    if (oldJobs.size() == 1) {
                                        jobMap.remove(getKeyInList());
                                    } else {
                                        oldJobs.remove(getPositionInList());
                                    }

                                    SPUtil.writeToSharePreferences(getApplicationContext(), jobMap);
                                    finish();
                                    Intent intent = new Intent(AddJobActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(AddJobActivity.this, "There is NO record to delete", Toast.LENGTH_SHORT);
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(R.drawable.ic_delete_alert)
                        .show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int month = Calendar.getInstance().get(Calendar.MONTH);
            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(this,
                    datePickerListener, year, month, day);
        }
        return null;
    }

    private void editAndCreateJob() {
        if (!jobMap.containsKey(jobModel.getDate())) {
            if (position != -1) {
                // edit job to new date key
                List<JobModel> oldJobs = jobMap.get(getKeyInList());
                if (oldJobs.size() == 1) {
                    jobMap.remove(getKeyInList());
                } else {
                    oldJobs.remove(getPositionInList());
                }

                jobs = new ArrayList<>();
                jobs.add(jobModel);
                jobMap.put(jobModel.getDate(), jobs);
            } else {
                // new job under new date key
                jobs = new ArrayList<>();
                jobs.add(jobModel);
                jobMap.put(jobModel.getDate(), jobs);
            }
        } else {
            if (position != -1) {
                // edit job under same exist date key
                if (getKeyInList().equals(jobModel.getDate())) {
                    jobs = jobMap.get(jobModel.getDate());
                    jobs.remove(getPositionInList());
                } else {
                    // edit job to other exist date key
                    List<JobModel> oldJobs = jobMap.get(getKeyInList());
                    if (oldJobs.size() == 1) {
                        jobMap.remove(getKeyInList());
                    } else {
                        oldJobs.remove(getPositionInList());
                    }
                    jobs = jobMap.get(jobModel.getDate());
                }

                jobs.add(jobModel);
                sortList();
                jobMap.put(jobModel.getDate(), jobs);
            } else {
                // new job under exist date key
                jobs = jobMap.get(jobModel.getDate());
                jobs.add(jobModel);
                sortList();
                jobMap.put(jobModel.getDate(), jobs);
            }
        }
    }

    private int getPositionInList() {
        int i = -1;
        int j;
        for (Entry<Date, List<JobModel>> en : jobMap.entrySet()) {
            i++;
            j = -1;
            for (JobModel obj : en.getValue()) {
                i++;
                j++;
                if (i == position) {
                    return j;
                }
            }
        }
        return -1;
    }

    private Date getKeyInList() {
        int i = -1;
        int j;
        for (Entry<Date, List<JobModel>> en : jobMap.entrySet()) {
            i++;
            j = -1;
            for (JobModel obj : en.getValue()) {
                i++;
                j++;
                if (i == position) {
                    return en.getKey();
                }
            }
        }
        return null;
    }

    private void setTvTotal() {
        if (TextUtils.isEmpty(editFinishTime.getText().toString())) {
            return;
        }
        int difference = DateUtil.compareTime(editFinishTime.getText().toString(),
                editStartTime.getText().toString());

        if (difference > 0) {
            double timeDifference = DateUtil.timeDifferenceInHours(editFinishTime.getText().toString(),
                    editStartTime.getText().toString());
            tvTotal.setText(String.format("%.1f", timeDifference));
        } else {
            editFinishTime.setText("");
            tvTotal.setText("");
            tvContract.setText("");
            Toast.makeText(AddJobActivity.this, "Finish time is earlier than start time!", Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void setTvContract() {
        if (TextUtils.isEmpty(editFinishTime.getText().toString())) {
            return;
        }

        if (TextUtils.isEmpty(editLunchMin.getText().toString())) {
            tvContract.setText(tvTotal.getText().toString());
        } else {
            double lunch = Double.parseDouble(editLunchMin.getText().toString()) / 60;
            double total = Double.parseDouble(tvTotal.getText().toString());
            tvContract.setText(String.format("%.1f", total - lunch));
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = DateUtil.convertMonth(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);
            editDate.setText(month1 + " " + day1 + ", " + year1);
            jobModel.setDateString(month1 + " " + day1 + ", " + year1);

            Date date = new Date(selectedYear, selectedMonth, selectedDay - 1);
            String dayOfTheWeek = DateUtil.getDayOfWeek(date);
            jobModel.setDayInWeek(dayOfTheWeek);
        }
    };

    private void sortList() {
        Collections.sort(jobs, new Comparator<JobModel>() {
            @Override
            public int compare(JobModel job1, JobModel job2) {
                return DateUtil.compareTime(job1.getStartTime(), job2.getStartTime());
            }
        });
    }

    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }

    protected void alertbox(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Device's GPS is Disable")
                .setCancelable(false)
                .setTitle("Gps Status")
                .setPositiveButton("Gps On",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {

            editLocation.setText("");
            progressBar.setVisibility(View.INVISIBLE);

            String locationName = null;
            Geocoder gcd = new Geocoder(getBaseContext(),
                    Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(), loc
                        .getLongitude(), 1);
                locationName = addresses.get(0).getAddressLine(0);
            } catch (IOException e) {
                e.printStackTrace();
            }

            editLocation.setText(locationName);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {
        }
    }
}

