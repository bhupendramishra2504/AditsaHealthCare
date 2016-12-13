package com.mlmishra.bhupendramishra.aditsahealthcare;

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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class View_notification extends Activity {
    TextView data;
    String data2="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notification);
        data=(TextView)findViewById(R.id.vnvra);
        data.setText("please wait .... Downloading Data from the server...");
        Firebase u_ref = new Firebase(Global.FIREBASE_URL).child("users").child("saurabh").child("notifications");

        u_ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int count=1;
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.getKey() != null) {
                        Map<String, Object> map = (Map<String, Object>) child.getValue();
                        String title = map.get("title").toString();
                        String message = map.get("message").toString();
                        String sendto=map.get("SendTo").toString();

                         data2=String.valueOf(count)+" : " +child.getKey().toString()+Global.separator+title+Global.separator+message+Global.separator+"send to : "+sendto+Global.separator+Global.separator+data2;
                        count++;
                    }
                }
                data.setText(data2);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // System.out.println("The read failed: ");
            }


        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_notification, menu);
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
