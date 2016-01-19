package hackaton.android.com.ireport;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddReport extends AppCompatActivity {
    TextView txtDate, txtTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        main_actions();
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
    }

    private void setCurrentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
        txtTime = (TextView)findViewById(R.id.txtTime);
        String formattedDate = sdf.format(new Date());
        txtTime.setText(formattedDate);
    }




    private void datepicker(){
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        System.out.println("the selected " + mDay);
        DatePickerDialog dialog;
        dialog = new DatePickerDialog(this, new mDateSetListener(), mYear, mMonth, mDay);
        dialog.show();
    }

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // getCalender();
            int mMonth = monthOfYear+1;
            txtDate = (TextView)findViewById(R.id.txtDate);
            txtDate.setText(String.format("%d/%d/%d", mMonth, dayOfMonth, year));
        }
    }


}
