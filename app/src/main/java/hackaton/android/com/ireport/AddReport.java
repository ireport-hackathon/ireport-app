package hackaton.android.com.ireport;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

    private LocationManager locationManager;
    private Bitmap bitmap;
    private ImageView imageView;
    private static final int REQUEST_CODE = 1;

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

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

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

        Button btnSend = (Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Mail m = new Mail("ireport.hackaton@gmail.com", "ireport1");
                Toast.makeText(AddReport.this, "BUSABOS", Toast.LENGTH_LONG).show();
                String[] toArr = {"ireport.hackaton@gmail.com"};
                m.setTo(toArr);
                m.setFrom("ireport.hackaton@gmail.com");
                m.setSubject("This is an email sent using my Mail JavaMail wrapper from an Android device.");
                m.setBody("Email body.");

                try {
//                    m.addAttachment("/sdcard/filelocation");

                    SendMail send = new SendMail(m);
                    send.execute();
                    while(send.getStatus() != AsyncTask.Status.FINISHED){
                        Toast.makeText(AddReport.this, send.getStatus().toString(), Toast.LENGTH_LONG).show();
                    }

                    if (send.getStatus() == AsyncTask.Status.FINISHED) {
                        Toast.makeText(AddReport.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(AddReport.this, "Email was not sent.", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(AddReport.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                    Log.e("MailApp", "Could not send email", e);
                }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        InputStream stream = null;
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getApplicationContext(), photo);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(tempUri));

            System.out.println(finalFile);
            imageView.setImageBitmap(photo);
        }
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public class SendMail extends AsyncTask<String, Integer, Void> {

        private Mail mail;

        public SendMail(Mail m){
            this.mail = m;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                mail.send();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
