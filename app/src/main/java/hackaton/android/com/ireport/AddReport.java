package hackaton.android.com.ireport;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AddReport extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener{
    TextView txtDate, txtTime, txtLocation;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat sdf;
    GoogleApiClient mGoogleApiClient;


    @Override
    public void onConnectionSuspended(int n){
        Toast.makeText(this, "Location connection suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Double lat = mLastLocation.getLatitude();
            Double lng = mLastLocation.getLongitude();
            getMyLocationAddress(lng,lat);
        }else{
            txtLocation = (TextView)findViewById(R.id.txtLocation);
            txtLocation.setText("Location Not Found.\nClick here to turn Location services");
            txtLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(callGPSSettingIntent, 1);
                }
            });

        }
    }

    public void getMyLocationAddress(Double lng, Double lat) {

        Geocoder geocoder= new Geocoder(this, Locale.ENGLISH);

        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if(addresses != null) {
                Address fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();
                for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
                    strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                }
                txtLocation.setText(strAddress.toString());
            }
            else
                txtLocation.setText(R.string.no_address_found);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Could not get address..!", Toast.LENGTH_LONG).show();
        }
        catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            Toast.makeText(this, illegalArgumentException.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){
        Toast.makeText(this, connectionResult.toString(), Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        main_actions();
        setCurrentDate();
        setCurrentTime();


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void main_actions(){
        txtDate = (TextView)findViewById(R.id.txtDate);
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker();
            }
        });
        txtTime = (TextView)findViewById(R.id.txtTime);
        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timepicker();
            }
        });
        txtLocation = (TextView)findViewById(R.id.txtLocation);
        txtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
            }
        });
    }

    private void setCurrentTime(){
        sdf = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        String formattedTime = sdf.format(new Date());
        txtTime = (TextView)findViewById(R.id.txtTime);
        txtTime.setText(formattedTime);
    }

    private void setCurrentDate(){
        sdf = new SimpleDateFormat("MM-dd-yyy");
        String formattedDate = sdf.format(new Date());
        txtDate = (TextView)findViewById(R.id.txtDate);
        txtDate.setText(formattedDate);
    }

    private void datepicker(){
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        System.out.println("the selected " + mDay);
        DatePickerDialog date_dialog = new DatePickerDialog(this, new myDateSetListener(), mYear, mMonth, mDay);
        date_dialog.show();
    }

    public void timepicker(){
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);
        TimePickerDialog time_dialog = new TimePickerDialog(this, new myTimeSetListener(), mHour, mMinute, false);
        time_dialog.show();
    }

    class myDateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            int myMonth = monthOfYear+1;
            txtDate = (TextView)findViewById(R.id.txtDate);
            txtDate.setText(String.format("%d/%d/%d", myMonth, dayOfMonth, year));
        }
    }

    class myTimeSetListener implements TimePickerDialog.OnTimeSetListener{
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String AM_PM ;
            if(hourOfDay < 12) {
                AM_PM = "AM";
            }
            else{
                AM_PM = "PM";
                hourOfDay-=12;
            }
            txtTime = (TextView)findViewById(R.id.txtTime);
            txtTime.setText(String.format("%d:%d %s", hourOfDay, minute, AM_PM));
        }
    }



}
/* tmp_master */