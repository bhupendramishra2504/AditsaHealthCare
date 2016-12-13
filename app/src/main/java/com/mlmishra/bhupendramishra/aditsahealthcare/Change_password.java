package com.mlmishra.bhupendramishra.aditsahealthcare;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;


import android.app.Activity;

public class Change_password extends Activity {
    EditText email,new_password,confirm_password;
    String s_user,s_email,o_password,n_password,cn_password;
    Button cpu,cuser_list;

    private static final String FIREBASE_URL = "https://aditsa-healthcare.firebaseio.com";

    /* A dialog that is presented until the Firebase authentication finished. */
    private ProgressDialog mAuthProgressDialog;

    /* A reference to the Firebase */
    private Firebase mFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        //getSupportActionBar().hide();

        new_password=(EditText)findViewById(R.id.cnpassword);
        confirm_password=(EditText)findViewById(R.id.ccnpassword);
        cpu=(Button)findViewById(R.id.cupsignup);
        cuser_list=(Button)findViewById(R.id.chgbutton);

        mFirebaseRef = new Firebase(FIREBASE_URL);

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */

        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading");
        mAuthProgressDialog.setMessage("Authenticating with Server...");
        mAuthProgressDialog.setCancelable(false);


        cuser_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu


                Firebase u_ref = mFirebaseRef.child("userlist");
                u_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    PopupMenu popup = new PopupMenu(Change_password.this, cuser_list);

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        for (DataSnapshot child : snapshot.getChildren()) {
                            if (child.getKey() != null) {
                                popup.getMenu().add(child.getKey().toString());
                                Log.d("item", "");
                            }
                        }

                        popup.getMenuInflater().inflate(R.menu.menu_tracking_enable_self, popup.getMenu());
                        //popup.setOutsideTouchable(true);
                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                s_user = (String) item.getTitle();
                                cuser_list.setText(item.getTitle());
                                Toast.makeText(Change_password.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
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




        cpu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                n_password = new_password.getText().toString();
                cn_password = confirm_password.getText().toString();
                Log.d("username", s_user);


                if (s_user.equals("")) {
                    Toast.makeText(Change_password.this, "Invalid username", Toast.LENGTH_LONG).show();
                } else {
                    if (n_password.equals(cn_password)) {

                        final Firebase viewtourref = mFirebaseRef.child("userlist").child(s_user);

                        viewtourref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                if (snapshot.getValue() != null) {
                                    o_password = snapshot.getValue().toString();
                                    viewtourref.setValue(cn_password);
                                    //Toast.makeText(Change_password.this, "Password" + o_password, Toast.LENGTH_LONG).show();
                                    change_user_password();

                                } else
                                    Toast.makeText(Change_password.this, "User does not exist", Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                System.out.println("The read failed: " + firebaseError.getMessage());
                            }
                        });




                    } else {
                        Toast.makeText(Change_password.this, "Password dosent matches", Toast.LENGTH_LONG).show();
                    }
                }


            }

        });

    }

    public void change_user_password() {
        mAuthProgressDialog.show();
        mFirebaseRef.changePassword(s_user+"@ahc.com", o_password, n_password, new ResultHandler());
    }




    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private class ResultHandler implements Firebase.ResultHandler {

        //private final String provider;

        public ResultHandler() {

        }

        @Override
        public void onSuccess() {
            mAuthProgressDialog.hide();
            mAuthProgressDialog.dismiss();
            Toast.makeText(Change_password.this, "PASSWORD CHANGED SUCCESSFULLY", Toast.LENGTH_LONG).show();


        }

        @Override
        public void onError(FirebaseError firebaseError) {
            mAuthProgressDialog.hide();
            showErrorDialog(firebaseError.toString());

        }
    }


    @Override
    public void onBackPressed() {

        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_password, menu);
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
        if(!Global.isNetworkAvailable(Change_password.this)) {
            Intent intent = new Intent(Change_password.this, Signin_Activity.class);
            startActivity(intent);
            finish();
        }
    }
}
