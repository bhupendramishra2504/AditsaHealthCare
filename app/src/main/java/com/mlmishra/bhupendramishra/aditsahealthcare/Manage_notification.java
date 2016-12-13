package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class Manage_notification extends Activity {
    Button set_noti,del_noti,view_noti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_notification);
        set_noti=(Button)findViewById(R.id.snsn);
        del_noti=(Button)findViewById(R.id.sndn);
        view_noti=(Button)findViewById(R.id.snvn);
        set_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Manage_notification.this, Set_notification.class);

                startActivity(intent);


            }
        });

        view_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Manage_notification.this, View_notification.class);

                startActivity(intent);


            }
        });

        del_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Firebase u_ref;
                //  Log.d("user type", Global.user_type);
                //Toast.makeText(Admin_view_report.this,"user type"+Global.user_type,Toast.LENGTH_LONG).show();

                u_ref = new Firebase(Global.FIREBASE_URL).child("userlist");
                u_ref.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        for (DataSnapshot child : snapshot.getChildren()) {
                            if (child.getKey() != null) {
                                Firebase u_ref1 = new Firebase(Global.FIREBASE_URL).child("users").child(child.getKey().toString()).child("notifications");
                                if(u_ref1!=null)
                                    u_ref1.removeValue();
                            }
                        }

                        Toast.makeText(Manage_notification.this, "Notification deleted till this time", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        // System.out.println("The read failed: ");
                    }


                });

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_notification, menu);
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
