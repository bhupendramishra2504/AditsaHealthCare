package com.mlmishra.bhupendramishra.aditsahealthcare;

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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import android.app.Activity;


public class Delete_user extends Activity {


    String s_user="",s_email,s_password,s_cpassword;
    Button delete_user,user_list;
    private static final String FIREBASE_URL = "https://aditsa-healthcare.firebaseio.com";

    /* A dialog that is presented until the Firebase authentication finished. */
    private ProgressDialog mAuthProgressDialog;

    /* A reference to the Firebase */
    private Firebase mFirebaseRef;
    String user_password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);
        //getSupportActionBar().hide();

        delete_user=(Button)findViewById(R.id.dusignup);
        user_list=(Button)findViewById(R.id.delbutton);

        mFirebaseRef = new Firebase(FIREBASE_URL);

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */

        mAuthProgressDialog = new ProgressDialog(Delete_user.this);
        mAuthProgressDialog.setTitle("Loading");
        mAuthProgressDialog.setMessage("Authenticating with Server...");
        mAuthProgressDialog.setCancelable(false);

        delete_user.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //s_user = username.getText().toString() + "@ahc.com";


                //Log.d("username", s_user);


                if (s_user.equals("")|s_user.equals("SELECT USER")) {
                    Toast.makeText(Delete_user.this, "Invalid username", Toast.LENGTH_LONG).show();
                } else {

                    Firebase viewtourref = mFirebaseRef.child("userlist").child(s_user);

                    viewtourref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.getValue() != null) {
                                user_password = snapshot.getValue().toString();
                               // Toast.makeText(Delete_user.this, "Password" + user_password, Toast.LENGTH_LONG).show();
                                delete_user();
                            } else
                                Toast.makeText(Delete_user.this, "User does not exist", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            System.out.println("The read failed: " + firebaseError.getMessage());
                        }
                    });

                    Firebase asmdel = mFirebaseRef.child(s_user).child("type");
                    asmdel.addListenerForSingleValueEvent(new ValueEventListener() {
                        String type;

                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            if (snapshot.getValue() != null) {

                                Global.user_type_del_user = snapshot.getValue().toString();
                                Firebase delref = mFirebaseRef.child("userlist").child(s_user);

                                if(Global.user_type_del_user.equals("asm"))
                                {
                                    Firebase delasm = mFirebaseRef.child("asm").child(s_user);
                                    delasm.removeValue();
                                }

                                delref.removeValue();
                                Toast.makeText(Delete_user.this, "USER DELETED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                                //Toast.makeText(Main_menu.this, type, Toast.LENGTH_LONG).show();
                                //Log.d("User type", type);




                            } else {
                                Toast.makeText(Delete_user.this, "Database corrupted or tempered", Toast.LENGTH_LONG).show();
                            }


                        }


                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            System.out.println("The read failed: " + firebaseError.getMessage());
                        }
                    });


                }


            }

        });


        user_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFinishing()) {

                            Firebase u_ref = mFirebaseRef.child("userlist");
                            u_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                PopupMenu popup = new PopupMenu(Delete_user.this, user_list);

                                @Override
                                public void onDataChange(DataSnapshot snapshot) {

                                    for (DataSnapshot child : snapshot.getChildren()) {
                                        if (child.getKey() != null) {
                                            popup.getMenu().add(child.getKey().toString());
                                            //Log.d("item", "");
                                        }
                                    }

                                    popup.getMenuInflater().inflate(R.menu.menu_delete_user, popup.getMenu());
                                    //popup.setOutsideTouchable(true);
                                    //registering popup with OnMenuItemClickListener
                                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                        public boolean onMenuItemClick(MenuItem item) {
                                            s_user = (String) item.getTitle();
                                            user_list.setText(item.getTitle());
                                            //Toast.makeText(Delete_user.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
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
                    }
                });
                /*Firebase u_ref = mFirebaseRef.child("userlist");
                u_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    PopupMenu popup = new PopupMenu(Delete_user.this, user_list);

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        for (DataSnapshot child : snapshot.getChildren()) {
                            if (child.getKey() != null) {
                                popup.getMenu().add(child.getKey().toString());
                                //Log.d("item", "");
                            }
                        }

                        popup.getMenuInflater().inflate(R.menu.menu_delete_user, popup.getMenu());
                        //popup.setOutsideTouchable(true);
                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                s_user = (String) item.getTitle();
                                user_list.setText(item.getTitle());
                                //Toast.makeText(Delete_user.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
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


                });*/


            }
        });






    }

    public void delete_user() {
        mAuthProgressDialog.show();
        mFirebaseRef.removeUser(s_user+"@ahc.com", user_password, new ResultHandler());


    }

    public void getPassword()
    {
        Firebase viewtourref = mFirebaseRef.child("userlist").child(s_user);

        viewtourref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot != null) {
                    user_password = snapshot.getValue().toString();
                    //Toast.makeText(Delete_user.this, "Password" + user_password, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });



    }

    private class ResultHandler implements Firebase.ResultHandler {

        //private final String provider;

        public ResultHandler() {

        }

        @Override
        public void onSuccess() {
                mAuthProgressDialog.hide();
                mAuthProgressDialog.dismiss();
                user_list.setText("Select user");

               // finish();

        }




        @Override
        public void onError(FirebaseError firebaseError) {
            mAuthProgressDialog.hide();
            showErrorDialog(firebaseError.toString());

        }
    }

    private void showErrorDialog(String message) {
        /*new AlertDialog.Builder(Delete_user.this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();*/
    }


    @Override
    public void onBackPressed() {

        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delete_user, menu);
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
        if(!Global.isNetworkAvailable(Delete_user.this)) {
            Intent intent = new Intent(Delete_user.this, Signin_Activity.class);
            startActivity(intent);
            finish();
        }
    }
}
