package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.app.ProgressDialog;
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
import android.os.Bundle;
import android.util.Log;
import android.util.Log;
import android.os.Handler;




public class Backup_activity extends Activity {

    TextView data;
    Button save;
    Firebase ref;
    private ProgressDialog mAuthProgressDialog;
    Handler threadHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_activity);
        data=(TextView)findViewById(R.id.backup);
        save=(Button)findViewById(R.id.bkupsave);
        data.setMovementMethod(new ScrollingMovementMethod());
        data.setTextIsSelectable(true);

        mAuthProgressDialog = new ProgressDialog(Backup_activity.this);
        mAuthProgressDialog.setTitle("Loading");
        mAuthProgressDialog.setMessage("Saving Data...");
        mAuthProgressDialog.setCancelable(false);

        ref = new Firebase(Global.FIREBASE_URL);
       // Firebase viewtourref = ref.child("users").child(userlogged1).child("DAILY CALL REPORT");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                data.setText(snapshot.getValue().toString());

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });



        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                save.setEnabled(false);
                save.setText("Please Wait...");

                threadHandler = new Handler();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        threadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Global.write_file("BACKUP", data.getText().toString(), Backup_activity.this);
                                Global.user_log(ref, "Director", "BACKUP TAKEN");
                                save.setEnabled(true);
                                save.setText("SAVE");
                            }
                        });
                    }
                }).start();


            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_backup_activity, menu);
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
        if(!Global.isNetworkAvailable(Backup_activity.this)) {
            Intent intent = new Intent(Backup_activity.this, Signin_Activity.class);
            startActivity(intent);
            finish();
        }
    }
}
