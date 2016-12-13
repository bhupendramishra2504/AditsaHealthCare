package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import android.app.Activity;

public class View_wsr_report extends Activity {
    private static final String FIREBASE_URL = "https://aditsa-healthcare.firebaseio.com";
    private Firebase ref;
    private TextView vrdrc;
    private String userlogged1;
    private String data="";
    private Button save;
    String separator = System.getProperty("line.separator");
    String filename;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wsr_report);
        Intent i = getIntent();
        userlogged1= i.getStringExtra("user");

        vrdrc=(TextView)findViewById(R.id.vdc);
        save=(Button)findViewById(R.id.vdcrsave);
        vrdrc.setMovementMethod(new ScrollingMovementMethod());
        vrdrc.setTextIsSelectable(true);

        ref = new Firebase(FIREBASE_URL);
        Firebase viewtourref = ref.child("users").child(userlogged1).child("WEEKLY SALES REPORT");

        viewtourref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {

                    data = child.getKey().toString() + separator + child.getValue().toString() + separator + separator + data;
                }

                vrdrc.setText(data);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });



        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                Global.write_file("WSR", vrdrc.getText().toString(), View_wsr_report.this);

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_wsr_report, menu);
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
        if(!Global.isNetworkAvailable(View_wsr_report.this)) {
            Intent intent = new Intent(View_wsr_report.this, Signin_Activity.class);
            startActivity(intent);
            finish();
        }
    }
}
