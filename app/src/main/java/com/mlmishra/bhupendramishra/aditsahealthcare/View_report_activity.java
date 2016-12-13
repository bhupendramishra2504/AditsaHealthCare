package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.DataSnapshot;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.text.method.ScrollingMovementMethod;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONException;
import android.util.Log;
import android.app.Activity;
import android.widget.Button;

public class View_report_activity extends Activity {
    private static final String FIREBASE_URL = "https://aditsa-healthcare.firebaseio.com";
    private Firebase ref;
    private TextView vra;
    private String userlogged1;
    private String data="";
    String separator = System.getProperty("line.separator");
    private String filename;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report_activity);
       // getSupportActionBar().hide();

        Intent i = getIntent();
        userlogged1= i.getStringExtra("user");

        vra=(TextView)findViewById(R.id.vra);
        vra.setMovementMethod(new ScrollingMovementMethod());
        vra.setTextIsSelectable(true);
        save=(Button)findViewById(R.id.vtsave);

        ref = new Firebase(FIREBASE_URL);
        Firebase viewtourref = ref.child("users").child(userlogged1).child("TOUR REPORT");

        viewtourref.addValueEventListener(new ValueEventListener() {
             @Override
            public void onDataChange(DataSnapshot snapshot) {
                 for (DataSnapshot child : snapshot.getChildren()) {

                     data=child.getKey().toString()+separator+child.getValue().toString()+separator+separator+data;
                 }

                 vra.setText(data);

                 }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
                }
             });

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                        Global.write_file("tour report", vra.getText().toString(), View_report_activity.this);

            }

        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_report_activity, menu);
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
        if(!Global.isNetworkAvailable(View_report_activity.this)) {
            Intent intent = new Intent(View_report_activity.this, Signin_Activity.class);
            startActivity(intent);
            finish();
        }
    }
}
