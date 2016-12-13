package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.widget.RadioGroup;
import android.os.Handler;
//import android.widget.CompoundButton.OnCheckedChangeListener;

public class Add_user extends Activity {

    EditText username,email,password,confirm_password,hqed;
    String s_user,s_email,s_password,s_cpassword,asm_user,hq;
    Button signup,seluser;
    private static final String FIREBASE_URL = "https://aditsa-healthcare.firebaseio.com";
    boolean director,rsm,asm,admin,se;
    PopupMenu popup;
    boolean first_time_popup=false;

    /* A dialog that is presented until the Firebase authentication finished. */
    private ProgressDialog mAuthProgressDialog;

    /* A reference to the Firebase */
    private Firebase mFirebaseRef;

    /* Data from the authenticated user */
    private AuthData mAuthData;

    /* Listener for Firebase session changes */
    private Firebase.AuthStateListener mAuthStateListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        //getSupportActionBar().hide();
        username=(EditText)findViewById(R.id.auuser);
        password=(EditText)findViewById(R.id.aucpassword);
        hqed=(EditText)findViewById(R.id.auhq);
        confirm_password=(EditText)findViewById(R.id.aucpassword);
        signup=(Button)findViewById(R.id.ausignup);
        seluser=(Button)findViewById(R.id.auseluser);


        //final PopupMenu popup = new PopupMenu(Add_user.this, seluser);

        popup = new PopupMenu(Add_user.this, seluser);

        seluser.setVisibility(View.GONE);

        mFirebaseRef = new Firebase(FIREBASE_URL);

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */

        mAuthProgressDialog = new ProgressDialog(Add_user.this);
        mAuthProgressDialog.setTitle("Loading");
        mAuthProgressDialog.setMessage("Authenticating with Server...");
        mAuthProgressDialog.setCancelable(false);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int chkid) {
                se = ((RadioButton) findViewById(R.id.I14)).isChecked();
                if (se)
                    seluser.setVisibility(View.VISIBLE);
                else
                    seluser.setVisibility(View.GONE);
            }
        });




        seluser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu




                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!isFinishing()) {
                      if(!first_time_popup) {
                          Firebase u_ref = mFirebaseRef.child("asm");
                          u_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                              //PopupMenu popup = new PopupMenu(Add_user.this, seluser);

                              @Override
                              public void onDataChange(DataSnapshot snapshot) {

                                  for (DataSnapshot child : snapshot.getChildren()) {
                                      if (child.getKey() != null) {
                                          popup.getMenu().add(child.getKey().toString());
                                          Log.d("item", "");
                                      }
                                  }

                                  popup.getMenu().add("NONE");

                                  popup.getMenuInflater().inflate(R.menu.menu_tracking_enable_self, popup.getMenu());
                                  //popup.setOutsideTouchable(true);
                                  //registering popup with OnMenuItemClickListener
                                  popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                      public boolean onMenuItemClick(MenuItem item) {
                                          asm_user = (String) item.getTitle();
                                          seluser.setText(item.getTitle());
                                          //Toast.makeText(Tracking_enable_self.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                          //tracked_user.setText("User Selected for trackin: " + item.getTitle());
                                          //find_me();
                                          return true;
                                      }
                                  });

                                  popup.show();
                              }

                              @Override
                              public void onCancelled(FirebaseError firebaseError) {
                                  // System.out.println("The read failed: ");
                              }


                          });
                          first_time_popup=true;
                      }
                            else
                          popup.show();

                        }
                        else
                            popup.dismiss();
                    }
                });





            }
        });



        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                s_user = username.getText().toString()+"@ahc.com";
                s_password = password.getText().toString();
                s_cpassword = confirm_password.getText().toString();
                hq=hqed.getText().toString();
                director = ((RadioButton) findViewById(R.id.I11)).isChecked();
                rsm = ((RadioButton) findViewById(R.id.I12)).isChecked();
                asm = ((RadioButton) findViewById(R.id.I13)).isChecked();
                se = ((RadioButton) findViewById(R.id.I14)).isChecked();
                admin = ((RadioButton) findViewById(R.id.I15)).isChecked();

                Log.d("username", s_user);


                if (!Global.isvalid_string(s_user)) {
                    Toast.makeText(Add_user.this, "Invalid username", Toast.LENGTH_LONG).show();
                } else {
                    if (s_password.equals(s_cpassword)) {

                        signup();

                    } else {
                        Toast.makeText(Add_user.this, "Password dosent matches", Toast.LENGTH_LONG).show();
                    }
                }


            }

        });

    }

    public void signup() {
        mAuthProgressDialog.show();
        mFirebaseRef.createUser(s_user, s_password, new ResultHandler());

    }




    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_user, menu);
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

    private class ResultHandler implements Firebase.ResultHandler {

        //private final String provider;

        public ResultHandler() {

        }

        @Override
        public void onSuccess() {
            mAuthProgressDialog.hide();
            mAuthProgressDialog.dismiss();
            //Toast.makeText(Add_user.this, "USER CREATED SUCCESSFULLY", Toast.LENGTH_LONG).show();
            add_userdata(username.getText().toString(),s_password);
            popup.dismiss();
            seluser.setText("Select user");
            finish();



        }

        @Override
        public void onError(FirebaseError firebaseError) {
            mAuthProgressDialog.hide();
            showErrorDialog(firebaseError.toString());

        }
    }

    private void add_userdata(String user, String password)
    {
        String type="";
        if(!director) {
            Firebase tourref = mFirebaseRef.child("userlist");
            //Toast.makeText(Add_user.this,"passwird is : "+password,Toast.LENGTH_LONG).show();
            Map<String, Object> user_datas = new HashMap<String, Object>();
            user_datas.put(user, password);
            tourref.updateChildren(user_datas);
        }

        if(director)
           type="director";
        else if(rsm)
            type="rsm";
        else if(asm)
           type="asm";
        else if(se) {
            type = "se";

        }
        else if(admin)
            type="admin";
        Firebase usertype=mFirebaseRef.child(user).child("type");
        usertype.setValue(type);

        Firebase fhq=mFirebaseRef.child(hq);
        Map<String,Object> addhq_data = new HashMap<String,Object>();
        addhq_data.put(user, "");
        fhq.updateChildren(addhq_data);


        if(se) {
            if (!asm_user.equals("NONE")) {
                Firebase boss = mFirebaseRef.child(asm_user).child("Subordinate");
                if(boss!=null) {
                    Map<String, Object> addso_data = new HashMap<String, Object>();
                    addso_data.put(user, "");
                    boss.updateChildren(addso_data);
                }
                else
                    Toast.makeText(Add_user.this,"Selected ASM do not Exist, Sales Executive is not added under any ASM",Toast.LENGTH_LONG).show();

            }
        }



        Firebase typeuserlist=mFirebaseRef.child(type);
        Map<String, Object> usertype_data = new HashMap<String, Object>();
        usertype_data.put(user, password);
        typeuserlist.updateChildren(usertype_data);


        Toast.makeText(Add_user.this,"USER CREATED SUCCESSFULLY",Toast.LENGTH_LONG).show();

    }




    @Override
    public void onResume() {
        super.onResume();
        if(!Global.isNetworkAvailable(Add_user.this)) {
            Intent intent = new Intent(Add_user.this, Signin_Activity.class);
            startActivity(intent);
            finish();
        }
    }

}
