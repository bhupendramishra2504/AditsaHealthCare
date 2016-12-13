package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import android.widget.Button;
import android.view.View;
import android.app.ProgressDialog;
import android.app.AlertDialog;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.util.Log;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.view.WindowManager;



public class Signin_Activity extends Activity {
    private Button login,offline;
    private TextView user,password,ver,change_log;
    private ProgressDialog mAuthProgressDialog;
    private Firebase mFirebaseRef;
    private AuthData mAuthData;
    private Firebase.AuthStateListener mAuthStateListener;
    private static final String FIREBASE_URL = "https://aditsa-healthcare.firebaseio.com";
    private AlertDialog ad;
    boolean remember;
    CheckBox cb;
    String deviceId,versionName;
    int versionCode;
    String separator = System.getProperty("line.separator");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Firebase.setAndroidContext(this);
        login = (Button) findViewById(R.id.signin);
        offline=(Button)findViewById(R.id.offline);
        user=(TextView)findViewById(R.id.user);
        password=(TextView)findViewById(R.id.password);
        ver=(TextView)findViewById(R.id.ver);
        change_log=(TextView)findViewById(R.id.chnlog);
        cb=(CheckBox)findViewById(R.id.checkbox);


        deviceId = Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);

        mFirebaseRef = new Firebase(FIREBASE_URL);
        restore_previous_state();



        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

        }
        catch(PackageManager.NameNotFoundException we)
        {
            versionName="1.1";
            versionCode=1;
        }


        ver.setText("Version: v "+versionName+"("+versionCode+")");

        change_log.setText(separator+separator+"CHANGE LOG FOR Version: v "+versionName+"("+versionCode+")"+separator+separator+"1. Self Planner for all users"+separator+"2. DCR Report Anaysis improved"+separator+"3. Bug fixes and improvements"+separator+"4. User Logs improved "+separator);


        if(isAirplaneModeOn(Signin_Activity.this))
        {
           Toast.makeText(Signin_Activity.this,"Airplane mode Activated,,"+"device id : "+deviceId,Toast.LENGTH_LONG).show();
            Global.user_log(mFirebaseRef,"DEVICES","Airplane mode Activated by device, check device id for user details : Device ID : "+deviceId);
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithPassword();
            }
        });

        offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                offline_eligibility();
            }
        });

        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Connecting with server");
        mAuthProgressDialog.setMessage("Authenticating with Aditsa HealthCare...");
        mAuthProgressDialog.setCancelable(false);

        if(isNetworkAvailable())
        {
            login.setVisibility(View.VISIBLE);
            offline.setVisibility(View.GONE);
            user.setVisibility(View.VISIBLE);
            password.setVisibility(View.VISIBLE);
            cb.setVisibility(View.VISIBLE);
        }
        else
        {
            login.setVisibility(View.GONE);
            offline.setVisibility(View.VISIBLE);
            user.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            cb.setVisibility(View.GONE);
            Toast.makeText(Signin_Activity.this, "Network Not Availble, Check your eligibility for offline access, in case of failure try for online login or contact for the Adminintrator", Toast.LENGTH_LONG);

        }



    }

    private void setAuthenticatedUser(AuthData authData) {
        if (authData != null) {
            /* Hide all the login buttons */
        }


        this.mAuthData = authData;
        /* invalidate options menu to hide/show the logout button */
        //supportInvalidateOptionsMenu();
    }


    public void loginWithPassword() {
        if(user.getText().toString()!=null && password.getText().toString()!=null && !user.getText().toString().matches(".*\\s+.*") && !password.getText().toString().matches(".*\\s+.*")) {
            mAuthProgressDialog.show();
            mFirebaseRef.authWithPassword(user.getText().toString() + "@ahc.com", password.getText().toString(), new AuthResultHandler("password"));
        }
        else
            Toast.makeText(Signin_Activity.this,"Invalid username or password",Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signin_, menu);
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


    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private class AuthResultHandler implements Firebase.AuthResultHandler {

        private final String provider;

        public AuthResultHandler(String provider) {
            this.provider = provider;
        }

        @Override
        public void onAuthenticated(AuthData authData) {
            mAuthProgressDialog.hide();
            mAuthProgressDialog.dismiss();
            //Log.i(TAG, provider + " auth successful");
            setAuthenticatedUser(authData);
            last_online_timeStamp();
            remember = ((CheckBox) findViewById(R.id.checkbox)).isChecked();
            save_username_password(remember);
            get_track_duration();
            Toast.makeText(Signin_Activity.this,"Signed in successfully as : "+user.getText().toString(), Toast.LENGTH_LONG).show();
            Global.user_log(mFirebaseRef,user.getText().toString(),"APPLICATION STARTED LOGIN : Logged-in with device ID : "+deviceId+separator+"S/w V"+versionName+"("+versionCode+")");
            Intent intent = new Intent(Signin_Activity.this, Main_menu.class);
            intent.putExtra("user", user.getText().toString());
            startActivity(intent);
            finish();

        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            mAuthProgressDialog.hide();
            showErrorDialog(firebaseError.toString());
        }
    }




    private void last_online_timeStamp()
    {

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        SharedPreferences.Editor editor = getSharedPreferences("AditsaHC", MODE_PRIVATE).edit();
        editor.putString("time", formattedDate);
        editor.commit();
    }

    private void save_username_password(boolean check)
    {
        if(check)
        {
            SharedPreferences.Editor editor = getSharedPreferences("AditsaHC", MODE_PRIVATE).edit();
            editor.putString("user", user.getText().toString());
            editor.putString("password", password.getText().toString());
            editor.putBoolean("remember", true);
            editor.commit();
        }
        else
        {
            SharedPreferences.Editor editor = getSharedPreferences("AditsaHC", MODE_PRIVATE).edit();
            editor.putString("user", "");
            editor.putString("password", "");
            editor.putBoolean("remember",false);
            editor.commit();
        }
    }

    private void offline_eligibility()
    {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        boolean eligible =false;

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        SharedPreferences prefs = getSharedPreferences("AditsaHC", MODE_PRIVATE);
        String restoredText = prefs.getString("time", null);
        if (restoredText != null) {

            Log.d("Stamped time", formattedDate);
            Log.d("Current time", restoredText);
            if(!restoredText.equals(formattedDate))
            {
                Toast.makeText(Signin_Activity.this,"You are not eligible for offline access. If you want to login enable your mobile data connection or wifi connection and try to login. IF the problem persists please contact your ADMINISTRATOR",Toast.LENGTH_LONG).show();
            }
            else
            {
                Intent intent = new Intent(Signin_Activity.this, Main_menu.class);
                intent.putExtra("user", "offline");
                startActivity(intent);
                finish();
            }
        }
        else
        {
            Toast.makeText(Signin_Activity.this,"You are not eligible for offline access. If you want to login enable your mobile data connection or wifi connection and try to login. IF the problem persists please contact your ADMINISTRATOR",Toast.LENGTH_LONG).show();
        }


    }

    private void get_track_duration()
    {
        Firebase asmdel = mFirebaseRef.child("track_time");
        asmdel.addListenerForSingleValueEvent(new ValueEventListener() {
            String type;

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.getValue() != null) {

                    Global.track_time = Float.parseFloat(snapshot.getValue().toString());

                    //Toast.makeText(Delete_user.this, "USER DELETED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                    //Toast.makeText(Main_menu.this, type, Toast.LENGTH_LONG).show();
                    //Log.d("User type", type);

                }


            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


    }

    private static boolean isAirplaneModeOn(Context context) {
        return Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
    }

    private void restore_previous_state()
    {
        SharedPreferences prefs = getSharedPreferences("AditsaHC", MODE_PRIVATE);
        String username = prefs.getString("user", null);
        String password1 =prefs.getString("password", null);
        boolean remember=prefs.getBoolean("remember",false);
        if(remember)
        {
            if(username != null && password1 != null)
            {
                user.setText(username);
                password.setText(password1);
                ((CheckBox) findViewById(R.id.checkbox)).setChecked(true);

            }
        }
    }

  

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
