package hackaton.android.com.ireport;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String selected_agency = null;
    String selected_action = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent = new Intent();

        if (id == R.id.nav_add_report) {
            // Start AddReport activity
            // intent.setClass(this, AddReport.class);
            dialog();
        } else if (id == R.id.nav_view_report) {
            // Start ViewReport activity
            intent.setClass(this, ViewReports.class);
        } else if (id == R.id.nav_view_instruction) {
            // Start Instruction activity
            intent.setClass(this, Instructions.class);
        } else if (id == R.id.nav_view_profile) {
            // Start Profile activity
            intent.setClass(this, ViewReports.class);
        } else if (id == R.id.nav_logout) {

        }

//        startActivity(intent);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void dialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Select Agency and Action");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do something with value!
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        final TextView txtAgency = new TextView(this);
        txtAgency.setText("\n\t\t\t\tSelect government agency");
        txtAgency.setTextColor(Color.BLACK);

        final TextView txtAction = new TextView(this);
        txtAction.setText("\n\t\t\t\tSelect action");
        txtAction.setTextColor(Color.BLACK);

        final Spinner agency_spinner = new Spinner(this);
        String comelec = "COMELEC", dotc = "DOTC", dswd = "DSWD";
        String []agency_items = new String []{comelec, dotc, dswd};
        ArrayAdapter<String> agency_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, agency_items);
        agency_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        agency_spinner.setAdapter(agency_adapter);

        final Spinner action_spinner = new Spinner(this);
        String report_type = "REPORT INCIDENT", feedback_type = "SUBMIT FEEDBACK";
        String []action_items = new String []{report_type, feedback_type};
        ArrayAdapter<String> action_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, action_items);
        action_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        action_spinner.setAdapter(action_adapter);

        agency_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_agency = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        action_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_action = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(txtAgency);
        layout.addView(agency_spinner);
        layout.addView(action_spinner);
        alert.setView(layout);
        alert.show();


    }

    private void reports(){
        startActivity(new Intent(this, AddReport.class));
    }


}
