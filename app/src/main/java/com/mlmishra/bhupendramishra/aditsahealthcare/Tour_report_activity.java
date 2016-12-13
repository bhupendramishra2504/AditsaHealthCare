package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import com.firebase.client.Firebase;
import java.util.Map;
import java.util.HashMap;
import android.util.Log;
import android.app.Activity;

public class Tour_report_activity extends Activity {

    private Button addC,submit,tourdesc;
    private String userlogged1;
    private TextView user,preview;
    private EditText tourplace,tourduration,addconv,comment,dates;
    private Firebase ref;
    private boolean first_time=false;
    String separator = System.getProperty("line.separator");
    private static final String FIREBASE_URL = "https://aditsa-healthcare.firebaseio.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_report_activity);
        //getSupportActionBar().hide();
        Intent i = getIntent();
        userlogged1= i.getStringExtra("user");

        addC=(Button)findViewById(R.id.trac);
        submit=(Button)findViewById(R.id.trsubmit);

        user=(TextView)findViewById(R.id.trtv0);
        preview=(TextView)findViewById(R.id.trtv1);
        tourdesc=(Button)findViewById(R.id.tpdesc);
        tourplace=(EditText)findViewById(R.id.tpplace);
        tourduration=(EditText)findViewById(R.id.tpduration);
        addconv=(EditText)findViewById(R.id.tplcon);
        comment=(EditText)findViewById(R.id.comment);
        dates=(EditText)findViewById(R.id.dates);

        ref = new Firebase(FIREBASE_URL);

        tourdesc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu


                PopupMenu popup = new PopupMenu(Tour_report_activity.this, tourdesc);


                popup.getMenu().add("EX STATION");
                popup.getMenu().add("OUT STATION");



                popup.getMenuInflater().inflate(R.menu.menu_daily_call_report, popup.getMenu());
                //popup.setOutsideTouchable(true);
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //s_report = (String) item.getTitle();
                        tourdesc.setText(item.getTitle());
                        //Toast.makeText(Admin_view_report.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        //tracked_user.setText("User Selected for trackin: " + (String) item.getTitle());
                        //find_me();
                        return true;
                    }
                });

                popup.show();
            }
        });


        user.setText("Logged as :  "+userlogged1);

        addC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!first_time) {
                    if (tourdesc.getText().toString() != "" && tourplace.getText().toString() != "" && tourduration.getText().toString() != "") {
                        preview.setText("Tour Description : "+ tourdesc.getText().toString() + separator + "Tour Place Covered : "+tourplace.getText().toString() + separator + "TOUR DURARTION : "+tourduration.getText().toString()+separator+"TOUR START DATE : "+dates.getText().toString() + separator + "ADD TRAVEL VOUCHER : "+separator+addconv.getText().toString());
                        first_time = true;
                    } else
                        Toast.makeText(Tour_report_activity.this, "Enter valid tour description/tour place/tour duration", Toast.LENGTH_LONG).show();
                } else {
                    if(addconv.getText().toString()!=null) {
                        String preview_text = preview.getText().toString();
                        preview.setText(separator + preview_text + separator + addconv.getText().toString() + separator);
                    }
                }

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String remark="";
                if(comment.getText()!=null)
                {
                    remark=comment.getText().toString();
                }

                add_tour_report_user(preview.getText().toString()+separator+"Comment/Remark : "+remark);
                Log.d("Tour Report",preview.getText().toString());
            }
        });


    }



    private void add_tour_report_user(String tour_report)
    {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        String formattedDate = df.format(c.getTime());
        Firebase tourref = ref.child("users").child(userlogged1).child("TOUR REPORT");
        Map<String, Object> nickname = new HashMap<String, Object>();
        nickname.put(formattedDate, tour_report);
        tourref.updateChildren(nickname);
        Toast.makeText(Tour_report_activity.this,"Tour Report Submitted Successfully",Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tour_report_activity, menu);
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
        if(!Global.isNetworkAvailable(Tour_report_activity.this)) {
            Intent intent = new Intent(Tour_report_activity.this, Signin_Activity.class);
            startActivity(intent);
            finish();
        }
    }
}
