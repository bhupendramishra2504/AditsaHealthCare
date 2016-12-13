package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import com.firebase.client.Firebase;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Settings_admin extends Activity {
    Firebase ref,tracktime;
    Button track_time_save;
    EditText time_value;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_admin);
        time_value=(EditText)findViewById(R.id.st1);
        track_time_save=(Button)findViewById(R.id.stts);
        ref=new Firebase(Global.FIREBASE_URL);
        tracktime=ref.child("track_time");
        pd = new ProgressDialog(Settings_admin.this);
        pd.setTitle("Saving ...");
        pd.setMessage("Connecting to the server");
        pd.setCancelable(false);

        track_time_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.show();
                tracktime.setValue(time_value.getText().toString());
                Toast.makeText(Settings_admin.this,"Tracking time value saved successfully", Toast.LENGTH_LONG).show();
                time_value.setText("");
                pd.dismiss();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings_admin, menu);
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
}
