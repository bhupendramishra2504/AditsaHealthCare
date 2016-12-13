package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.firebase.client.Firebase;
import android.app.Activity;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Weekly_sales_report extends Activity {

    private Button submit,wsrmon,wsrweek;
    private String userlogged1;
    private TextView user;
    private EditText emphq,dcrter,pro,mcal,mfol,ihb,calone,bioeli,ovitsa,dcrrd,comment;
    private Firebase ref;
    String separator = System.getProperty("line.separator");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_sales_report);
        user=(TextView)findViewById(R.id.dcrtv0);
        emphq=(EditText)findViewById(R.id.dcrhq);
        pro=(EditText)findViewById(R.id.psr);
        mcal=(EditText)findViewById(R.id.mcal);
        mfol=(EditText)findViewById(R.id.mfol);
        ihb=(EditText)findViewById(R.id.ihb);
        calone=(EditText)findViewById(R.id.calone);
        bioeli=(EditText)findViewById(R.id.bioeli);
        ovitsa=(EditText)findViewById(R.id.ovitsa);
        dcrter=(EditText)findViewById(R.id.dcrter);
        comment=(EditText)findViewById(R.id.comment);

        submit=(Button)findViewById(R.id.dcrsubmit);
        wsrmon=(Button)findViewById(R.id.wsrmon);
        wsrweek=(Button)findViewById(R.id.wsrweek);

        ref = new Firebase(Global.FIREBASE_URL);
        Intent i = getIntent();
        userlogged1= i.getStringExtra("user");
        user.setText("User logged as : "+userlogged1);

        wsrmon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu


                PopupMenu popup = new PopupMenu(Weekly_sales_report.this, wsrmon);


                popup.getMenu().add("JANUARY");
                popup.getMenu().add("FEBRUARY");
                popup.getMenu().add("MARCH");
                popup.getMenu().add("MARCH");
                popup.getMenu().add("APRIL");
                popup.getMenu().add("MAY");
                popup.getMenu().add("JUNE");
                popup.getMenu().add("JULY");
                popup.getMenu().add("AUGUST");
                popup.getMenu().add("SEPTEMBER");
                popup.getMenu().add("OCTOBER");
                popup.getMenu().add("NOVEMBER");
                popup.getMenu().add("DECEMBER");


                popup.getMenuInflater().inflate(R.menu.menu_weekly_sales_report, popup.getMenu());
                //popup.setOutsideTouchable(true);
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //s_report = (String) item.getTitle();
                        wsrmon.setText(item.getTitle());
                        //Toast.makeText(Admin_view_report.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        //tracked_user.setText("User Selected for trackin: " + (String) item.getTitle());
                        //find_me();
                        return true;
                    }
                });

                popup.show();
            }
        });

        wsrweek.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu


                PopupMenu popup = new PopupMenu(Weekly_sales_report.this, wsrweek);


                popup.getMenu().add("(1st Week)");
                popup.getMenu().add("(2nd Week)");
                popup.getMenu().add("(3rd Week)");
                popup.getMenu().add("(4th Week)");
                popup.getMenu().add("(5th Week)");





                popup.getMenuInflater().inflate(R.menu.menu_weekly_sales_report, popup.getMenu());
                //popup.setOutsideTouchable(true);
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //s_report = (String) item.getTitle();
                        wsrweek.setText(item.getTitle());
                        //Toast.makeText(Admin_view_report.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        //tracked_user.setText("User Selected for trackin: " + (String) item.getTitle());
                        //find_me();
                        return true;
                    }
                });

                popup.show();
            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( emphq.getText().toString()!=null | dcrter.getText().toString()!=null | !wsrmon.getText().toString().equalsIgnoreCase("Select month") | !wsrweek.getText().toString().equalsIgnoreCase("Select week")) {
                    String data = "Weekly Sales Report" + separator + separator + "Employee Name: " + userlogged1 + separator + "Reporting Week : " + wsrmon.getText().toString()+"  "+wsrweek.getText().toString() + separator + "Employee Headquarter : " + emphq.getText().toString() + separator + "TERRITORY" + dcrter.getText().toString() + separator + "PROGEMMOM SR (QTY) : " + pro.getText().toString() + separator + "MOMMI CAL (QTY) : " + mcal.getText().toString() + separator + "MOMMI FOL (QTY) : " + mfol.getText().toString() + separator + "IRROW HB (QTY) : " + ihb.getText().toString() + separator + "CALITSA ONE (QTY) : " + calone.getText().toString() + separator + "BIO ELIXSA : " + bioeli.getText().toString() + separator + "OVITSA : " + ovitsa.getText().toString() + separator;
                    //String data="Daily Call Report" + separator + separator + "Employee Name: " + userlogged1 + separator + "Date of Submission : " + dcrrd.getText().toString()+ separator + "Employee Headquarter : " + emphq.getText().toString() + separator + "TERRITORY" + dcrter.getText().toString() + separator;
                    String remark = "";
                    if (comment.getText() != null) {
                        remark = comment.getText().toString();
                    }

                    add_weekly_sales_report_user(data + separator + "Comment/Reamrk : " + remark);
                    Log.d("Tour Report", data);
                }
                else
                    Toast.makeText(Weekly_sales_report.this,"Data Incomplete or not Correct",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void add_weekly_sales_report_user(String tour_report)
    {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        String formattedDate = df.format(c.getTime());
        Firebase tourref = ref.child("users").child(userlogged1).child("WEEKLY SALES REPORT");
        Map<String, Object> nickname = new HashMap<String, Object>();
        nickname.put(formattedDate, tour_report);
        tourref.updateChildren(nickname);
        add_weekly_sales_report_database();
        Toast.makeText(Weekly_sales_report.this, "Weekly Sales Report Submitted Successfully", Toast.LENGTH_LONG).show();
    }

    private void add_weekly_sales_report_database()
    {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        String formattedDate = df.format(c.getTime());
        Firebase tourref = ref.child("users").child(userlogged1).child("WSR_DATABASE").child(wsrmon.getText().toString()).child(wsrweek.getText().toString());
        Map<String, Object> nickname = new HashMap<String, Object>();
        nickname.put("procal", pro.getText().toString());
        tourref.updateChildren(nickname);
        nickname.put("mcal", mcal.getText().toString());
        tourref.updateChildren(nickname);
        nickname.put("mfol", mfol.getText().toString());
        tourref.updateChildren(nickname);
        nickname.put("ihb", ihb.getText().toString());
        tourref.updateChildren(nickname);
        nickname.put("calone", calone.getText().toString());
        tourref.updateChildren(nickname);
        nickname.put("bioeli", bioeli.getText().toString());
        tourref.updateChildren(nickname);
        nickname.put("ovitsa",ovitsa.getText().toString());
        tourref.updateChildren(nickname);
        //Toast.makeText(Weekly_sales_report.this, "Weekly Sales Report Submitted Successfully", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weekly_sales_report, menu);
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
    public void onResume() {
        super.onResume();
        if(!Global.isNetworkAvailable(Weekly_sales_report.this)) {
            Intent intent = new Intent(Weekly_sales_report.this, Signin_Activity.class);
            startActivity(intent);
            finish();
        }
    }
}
