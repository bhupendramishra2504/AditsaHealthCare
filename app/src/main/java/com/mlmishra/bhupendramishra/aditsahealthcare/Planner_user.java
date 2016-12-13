package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mlmishra.bhupendramishra.aditsahealthcare.R;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.widget.Button;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class Planner_user extends Activity {

    private String u_logged;
    private TextView userinfo,pudate;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private List<String> listOfString;
    private ArrayAdapter<String> adapter;
    private int count=0;
    private ListView lv;
    private Firebase ref;
    private Button showplan;
    double lat;
    double lng;
    LocationManager lm;
    Location location;
    Criteria criteria;
    GPSTracker gps;
    private TextView sndloc;
    Geocoder geocoder;
    String bestProvider;
    String separator = System.getProperty("line.separator");
    private String drdata;
    private Context context;
    String product;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_user);

        init();
        context=this;
        Intent i = getIntent();
        u_logged= i.getStringExtra("user");
        userinfo=(TextView)findViewById(R.id.putv0);
        pudate=(TextView)findViewById(R.id.pudate);
        lv = (ListView) findViewById(R.id.list_viewp);
        showplan=(Button)findViewById(R.id.pushow);
        ref=new Firebase(Global.FIREBASE_URL);

        userinfo.setText("Logged in as : " + u_logged);

        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        pudate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Planner_user.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        showplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               list_populate();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_planner_user, menu);
        return true;
    }


    private void updateLabel() {

        String myFormat = "ddMMyyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        pudate.setText(sdf.format(myCalendar.getTime()));
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

    public void list_populate()
    {

            Firebase u_ref = ref.child("users").child(u_logged).child("planner").child(pudate.getText().toString());

            u_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                //PopupMenu popup = new PopupMenu(Add_user.this, seluser);

                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    listOfString = new ArrayList<String>();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        if (child.getKey() != null) {
                            drdata=child.getKey().toString()+"-"+child.getValue().toString();
                            listOfString.add(child.getKey().toString()+"-"+child.getValue().toString());
                            Log.d("item", "");
                        }
                    }

                    adapter = new ArrayAdapter<String>(Planner_user.this,
                            R.layout.list, listOfString);
                    adapter.notifyDataSetChanged();
                    lv.setAdapter(adapter);
                    registerForContextMenu(lv);

                    lv.invalidateViews();
                    // listening to single list item on click
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            product = ((TextView) view).getText().toString();
                            // selected item
                            new AlertDialog.Builder(context)
                                    .setTitle("Log Location ??")
                                    .setMessage("Are you sure you visited the docter and want to register his/her location")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {





                                            gps = new GPSTracker(Planner_user.this);

                                            // check if GPS enabled
                                            if (Global.track_time != 0) {
                                                //send_loc_period(Global.track_time);
                                                // Toast.makeText(Track_Start_User.this, "track time " + String.valueOf(Global.track_time), Toast.LENGTH_LONG).show();
                                            } else{
                                                // send_loc_period(0.5f);
                                                // Toast.makeText(Track_Start_User.this,"No track found in database-default set",Toast.LENGTH_LONG).show();
                                            }



                                            if(gps.canGetLocation()){

                                                double latitude = gps.getLatitude();
                                                double longitude = gps.getLongitude();

                                                // \n is for new line
                                                if(latitude!=0.0 && longitude !=0.0) {
                                                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                                                    // sndloc.setText("LOCATION SEND TO SERVER " + separator + " Latitute : " + String.valueOf(latitude) + separator + "Longitute : " + String.valueOf(longitude) + separator + separator + "Press back button to leave this screen");
                                                    send_user_location(String.valueOf(latitude) + "," + String.valueOf(longitude));
                                                }
                                            }else{
                                                //Toast.makeText(Track_Start_User.this, "Location could not be sent",Toast.LENGTH_LONG).show();
                                                //Global.user_log("LOCATION : GPS SERVICE NOT ENABLED");
                                            }




                                            // continue with delete
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();



                            //lv.setVisibility(View.GONE);



                        }
                    });

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    // System.out.println("The read failed: ");
                }


            });


    }

    private void send_user_location(String location_data)
    {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String formattedDate = df.format(c.getTime());
        Firebase tourref = ref.child("users").child(u_logged).child("PLANNER LOCATIONS");
        Map<String, Object> nickname = new HashMap<String, Object>();
        nickname.put(formattedDate+";"+product, location_data);
        tourref.updateChildren(nickname);
        //Toast.makeText(Track_Start_User.this, "USER LOCATION SEND Successfully", Toast.LENGTH_LONG).show();
    }

    public void init()
    {
        if ( Build.VERSION.SDK_INT >= 23  && ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
            lm = (LocationManager) Planner_user.this.getSystemService(Context.LOCATION_SERVICE);

            criteria = new Criteria();
            bestProvider = lm.getBestProvider(criteria, false);
            location = lm.getLastKnownLocation(bestProvider);
            return  ;
        }
    }
}
