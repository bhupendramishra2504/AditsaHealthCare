package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.app.Activity;

public class Reporting_activity extends Activity {

    private Button tourreport,viewreport,dcrreport,viewdcrreport,wsreport,vwsreport;
    private String userlogged1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporting_activity);
        //getSupportActionBar().hide();
        Intent i = getIntent();
        userlogged1= i.getStringExtra("user");

        tourreport=(Button)findViewById(R.id.tourreport);
        viewreport=(Button)findViewById(R.id.vpreport);
        dcrreport=(Button)findViewById(R.id.dcrreport);
        viewdcrreport=(Button)findViewById(R.id.vdcrreport);
        wsreport=(Button)findViewById(R.id.wsreport);
        vwsreport=(Button)findViewById(R.id.vwsreport);

        tourreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Reporting_activity.this, Tour_report_activity.class);
                intent.putExtra("user", userlogged1);
                startActivity(intent);


            }
        });

        viewreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Reporting_activity.this, View_report_activity.class);
                intent.putExtra("user", userlogged1);
                startActivity(intent);


            }
        });

        dcrreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Reporting_activity.this, Daily_call_report.class);
                intent.putExtra("user", userlogged1);
                startActivity(intent);


            }
        });

        wsreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Reporting_activity.this, Weekly_sales_report.class);
                intent.putExtra("user", userlogged1);
                startActivity(intent);


            }
        });

        viewdcrreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Reporting_activity.this, View_dcr_report.class);
                intent.putExtra("user", userlogged1);
                startActivity(intent);


            }
        });

        vwsreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Reporting_activity.this, View_wsr_report.class);
                intent.putExtra("user", userlogged1);
                startActivity(intent);


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reporting_activity, menu);
        return true;
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

    @Override
    public void onBackPressed() {


        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!Global.isNetworkAvailable(Reporting_activity.this)) {
            Intent intent = new Intent(Reporting_activity.this, Signin_Activity.class);
            startActivity(intent);
            finish();
        }
    }


}
