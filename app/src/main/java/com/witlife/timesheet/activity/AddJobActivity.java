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
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.witlife.timesheet.R;
import com.witlife.timesheet.util.DateUtil;
import com.witlife.timesheet.util.GPSTracker;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Boolean flag = false;
    GPSTracker gpsTracker;

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
        Date currentDate = Calendar.getInstance().getTime();
        editDate.setText(DateUtil.getDate(currentDate));
        editStartTime.setText(DateUtil.getTime(currentDate));
        progressBar.setVisibility(View.INVISIBLE);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    }

    private void setListener() {
        btnGetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentDate = Calendar.getInstance().getTime();
                editDate.setText(DateUtil.getDate(currentDate));
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
                mTimePicker = new TimePickerDialog(AddJobActivity.this,android.R.style.Theme_Material_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        editStartTime.setText( selectedHour + ":" + selectedMinute + ":00" );
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
                mTimePicker = new TimePickerDialog(AddJobActivity.this,android.R.style.Theme_Material_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        editFinishTime.setText( selectedHour + ":" + selectedMinute + ":00" );
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

                    locationManager.requestLocationUpdates(LocationManager
                            .GPS_PROVIDER, 5000, 10, locationListener);

                } else {
                    alertbox("Gps Status!!", "Your GPS is: OFF");
                }
            }
        });
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

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = DateUtil.convertMonth(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);
            editDate.setText(month1 + " " + day1);
        }
    };

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
                String[] parts = locationName.split(" ");
                locationName = parts[0] + " " + parts[1] + parts[2];
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

