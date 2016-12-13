package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.os.Build;

import com.firebase.client.Firebase;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import android.location.LocationManager;
import android.location.LocationListener;
import android.content.Context;
import android.location.Location;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import android.location.Geocoder;
import android.location.Criteria;
import android.location.Address;
import java.util.List;
import android.app.Activity;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import java.util.Timer;
import java.util.TimerTask;



public class Track_Start_User extends Activity implements LocationListener{
    private String userlogged1;
    private Firebase ref;
    private String longlatdata;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    private TextView sndloc;
    Geocoder geocoder;
    String bestProvider;
    List<Address> user = null;
    double lat;
    double lng;
    LocationManager lm;
    Location location;
    Criteria criteria;
    GPSTracker gps;
    String separator = System.getProperty("line.separator");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track__start__user);
        // getSupportActionBar().hide();
        Intent i = getIntent();
        userlogged1 = i.getStringExtra("user");
        ref = new Firebase(Global.FIREBASE_URL);
        sndloc = (TextView) findViewById(R.id.tsu1);


        init();


        gps = new GPSTracker(Track_Start_User.this);

        // check if GPS enabled
        if (Global.track_time != 0) {
            send_loc_period(Global.track_time);
            Toast.makeText(Track_Start_User.this,"track time "+String.valueOf(Global.track_time),Toast.LENGTH_LONG).show();
        } else{
            send_loc_period(0.5f);
            Toast.makeText(Track_Start_User.this,"No track found in database-default set",Toast.LENGTH_LONG).show();
          }



        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            if(latitude!=0.0 && longitude !=0.0) {
                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                sndloc.setText("LOCATION SEND TO SERVER " + separator + " Latitute : " + String.valueOf(latitude) + separator + "Longitute : " + String.valueOf(longitude) + separator + separator + "Press back button to leave this screen");
                send_user_location(String.valueOf(latitude) + "," + String.valueOf(longitude));
            }
        }else{
            Toast.makeText(Track_Start_User.this, "Location could not be sent",Toast.LENGTH_LONG).show();
            user_log("LOCATION : GPS SERVICE NOT ENABLED");
        }

    }


    private void send_loc_period(float timer)
    {
        //Toast.makeText(Track_Start_User.this, "Running Timer Task",Toast.LENGTH_LONG).show();

        final Timer timer11 = new Timer();
        timer11.scheduleAtFixedRate( new TimerTask() {
            public void run() {
                int max_loc=0;
                try{
                    if(gps.canGetLocation()){


                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        // \n is for new line
                        if(latitude!=0.0 && longitude !=0.0) {
                           // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                            sndloc.setText("LOCATION SEND TO SERVER " + separator + " Latitute : " + String.valueOf(latitude) + separator + "Longitute : " + String.valueOf(longitude) + separator + separator + "Press back button to leave this screen");
                            user_log("LOCATION : TRACKING SERVICE ACTIVATED, LOCATION OF USER LOGGED");
                            send_user_location(String.valueOf(latitude) + "," + String.valueOf(longitude));
                            max_loc++;
                            if(max_loc>=20)
                                timer11.cancel();
                        }
                    }else{
                        user_log("LOCATION : GPS SERVICE NOT ENABLED");
                        Toast.makeText(Track_Start_User.this, "Location could not be sent",Toast.LENGTH_LONG).show();
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        //gps.showSettingsAlert();
                    }


                }
                catch (Exception e) {
                   //Toast.makeText(Track_Start_User.this, "Error Occured",Toast.LENGTH_LONG).show();
                }

            }
        }, 0, Math.round(timer*3600*1000));



    }

    @Override
    public void onLocationChanged(Location location) {

      //Toast.makeText(Track_Start_User.this,"ff",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {


    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    public void init()
    {
        if ( Build.VERSION.SDK_INT >= 23  && ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
               ) {
            lm = (LocationManager) Track_Start_User.this.getSystemService(Context.LOCATION_SERVICE);

            criteria = new Criteria();
            bestProvider = lm.getBestProvider(criteria, false);
            location = lm.getLastKnownLocation(bestProvider);
            return  ;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    private void send_user_location(String location_data)
    {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        Firebase tourref = ref.child("users").child(userlogged1).child("LOCATIONS");
        Map<String, Object> nickname = new HashMap<String, Object>();
        nickname.put(formattedDate+":::", location_data);
        tourref.updateChildren(nickname);
        Toast.makeText(Track_Start_User.this, "USER LOCATION SEND Successfully", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_track__start__user, menu);
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



        user_log("LOCATION : TRACKING ENDED BY THE USER INTENTIONALLY");
        finish();
    }

    private void user_log(String reason)
    {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String formattedDate = df.format(c.getTime());
        Firebase tourref = ref.child("users").child(userlogged1).child("LOG");
        Map<String, Object> nickname = new HashMap<String, Object>();
        nickname.put(formattedDate, reason);
        tourref.updateChildren(nickname);
        //Toast.makeText(Daily_call_report.this, "Daily Call Report Submitted Successfully", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!Global.isNetworkAvailable(Track_Start_User.this)) {
            user_log("LOCATION : TRACKING disable due to No Internet Connection");
            Intent intent = new Intent(Track_Start_User.this, Signin_Activity.class);
            startActivity(intent);
            finish();
        }
    }


}
