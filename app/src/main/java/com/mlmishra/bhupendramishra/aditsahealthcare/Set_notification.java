package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mlmishra.bhupendramishra.aditsahealthcare.R;
import android.app.Activity;
import android.widget.PopupMenu;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Set_notification extends Activity {
    private Button userlist,send_n;
    private EditText title,message;
    String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_notification);
        userlist = (Button) findViewById(R.id.snseluser);
        send_n = (Button) findViewById(R.id.snsend);
        title=(EditText)findViewById(R.id.sntitle);
        message=(EditText)findViewById(R.id.snmessage);
        userlist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Creating the instance of PopupMenu
                Firebase u_ref;
                //  Log.d("user type", Global.user_type);
                //Toast.makeText(Admin_view_report.this,"user type"+Global.user_type,Toast.LENGTH_LONG).show();

                u_ref = new Firebase(Global.FIREBASE_URL).child("userlist");
                u_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    PopupMenu popup = new PopupMenu(Set_notification.this, userlist);

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        for (DataSnapshot child : snapshot.getChildren()) {
                            if (child.getKey() != null) {
                                popup.getMenu().add(child.getKey().toString());
                                // Log.d("item", "");
                            }
                        }
                        popup.getMenu().add("All Users");
                        popup.getMenuInflater().inflate(R.menu.menu_tracking_enable_self, popup.getMenu());
                        //popup.setOutsideTouchable(true);
                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {

                                userlist.setText(item.getTitle());
                                //Toast.makeText(Admin_view_report.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                //tracked_user.setText("User Selected for trackin: " + (String) item.getTitle());
                                //find_me();
                                return true;
                            }
                        });

                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                popup.show();
                            }
                        }, 100);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        // System.out.println("The read failed: ");
                    }


                });

            }

        });

        send_n.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                formattedDate = df.format(c.getTime());
                if(!title.getText().toString().equalsIgnoreCase("")&& !title.getText().toString().equalsIgnoreCase(null) && !userlist.getText().toString().equalsIgnoreCase("sel user")) {
                    //Creating the instance of PopupMenu

                    if(userlist.getText().toString().equalsIgnoreCase("All Users"))
                    {
                        Firebase u_ref;
                        //  Log.d("user type", Global.user_type);
                        //Toast.makeText(Admin_view_report.this,"user type"+Global.user_type,Toast.LENGTH_LONG).show();

                        u_ref = new Firebase(Global.FIREBASE_URL).child("userlist");
                        u_ref.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot snapshot) {

                                for (DataSnapshot child : snapshot.getChildren()) {
                                    if (child.getKey() != null) {
                                        Firebase u_ref1 = new Firebase(Global.FIREBASE_URL).child("users").child(child.getKey().toString()).child("notifications").child(formattedDate);
                                        Firebase title1 = u_ref1.child("title");
                                        title1.setValue(title.getText().toString());
                                        Firebase message1 = u_ref1.child("message");
                                        message1.setValue(message.getText().toString());
                                        Firebase read = u_ref1.child("read");
                                        read.setValue("0");
                                    }
                                }

                                Toast.makeText(Set_notification.this, "Notification sent to all users" , Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                // System.out.println("The read failed: ");
                            }


                        });

                    }
                    else {
                        Firebase u_ref;
                        //  Log.d("user type", Global.user_type);
                        //Toast.makeText(Admin_view_report.this,"user type"+Global.user_type,Toast.LENGTH_LONG).show();
                        u_ref = new Firebase(Global.FIREBASE_URL).child("users").child(userlist.getText().toString()).child("notifications").child(formattedDate);
                        Firebase title1 = u_ref.child("title");
                        title1.setValue(title.getText().toString());
                        Firebase message1 = u_ref.child("message");
                        message1.setValue(message.getText().toString());
                        Firebase read = u_ref.child("read");
                        read.setValue("0");
                        Toast.makeText(Set_notification.this, "Notification sent to : " + userlist.getText().toString(), Toast.LENGTH_LONG).show();
                    }
                    write2director();
                }
                else
                    Toast.makeText(Set_notification.this,"Select valid user and/or write valid title and/or write valid message",Toast.LENGTH_LONG).show();

            }});


    }


    public void write2director()
    {
        Firebase u_ref;
        //  Log.d("user type", Global.user_type);
        //Toast.makeText(Admin_view_report.this,"user type"+Global.user_type,Toast.LENGTH_LONG).show();
        u_ref = new Firebase(Global.FIREBASE_URL).child("users").child("saurabh").child("notifications").child(formattedDate);
        Firebase title1 = u_ref.child("title");
        title1.setValue(title.getText().toString());
        Firebase message1 = u_ref.child("message");
        message1.setValue(message.getText().toString());
        Firebase sendto=u_ref.child("SendTo");
        sendto.setValue(userlist.getText().toString());
       // Toast.makeText(Set_notification.this, "Notification sent to : " + userlist.getText().toString(), Toast.LENGTH_LONG).show();

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
