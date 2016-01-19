package hackaton.android.com.ireport;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddReport extends AppCompatActivity {
    TextView txtDate, txtTime;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        main_actions();
        setCurrentDate();
        setCurrentTime();
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
