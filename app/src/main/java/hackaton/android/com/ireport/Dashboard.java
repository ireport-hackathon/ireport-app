package hackaton.android.com.ireport;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.title_dashboard);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final RadioGroup govAgencyGroup = (RadioGroup) findViewById(R.id.radio_group_agency);
        final RadioGroup actionGroup = (RadioGroup) findViewById(R.id.radio_group_action);


        Button proceed_button = (Button) findViewById(R.id.button_proceed);
        proceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton selectedAgency = (RadioButton)findViewById(govAgencyGroup.getCheckedRadioButtonId());
                String agency = selectedAgency.getText().toString();
                RadioButton selectedAction = (RadioButton)findViewById(actionGroup.getCheckedRadioButtonId());
                String action = selectedAction.getText().toString();

                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), AddReport.class);
                intent.putExtra("Agency", agency);
                intent.putExtra("Action", action);
                startActivity(intent);
            }
        });
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
            intent.setClass(this, Dashboard.class);
        } else if (id == R.id.nav_view_report) {
            // Start ViewReport activity
            intent.setClass(this, ViewReports.class);
        } else if (id == R.id.nav_view_instruction) {
            // Start Instruction activity
            intent.setClass(this, Instructions.class);
        } else if (id == R.id.nav_view_profile) {
            // Start Profile activity
            intent.setClass(this, Profile.class);
        } else if (id == R.id.nav_logout) {

        }

        startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
