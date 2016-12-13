package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import android.app.Activity;

public class Delete_presentation_link extends Activity {

    String s_user,s_email,s_password,s_cpassword;
    Button delete_link_select,del_link;
    private static final String FIREBASE_URL = "https://aditsa-healthcare.firebaseio.com";

    /* A dialog that is presented until the Firebase authentication finished. */
    private ProgressDialog mAuthProgressDialog;

    /* A reference to the Firebase */
    private Firebase mFirebaseRef;
    String user_password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_presentation_link);
        delete_link_select=(Button)findViewById(R.id.dlbutton);
        del_link=(Button)findViewById(R.id.dellink);

        mFirebaseRef = new Firebase(FIREBASE_URL);

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */

        mAuthProgressDialog = new ProgressDialog(Delete_presentation_link.this);
        mAuthProgressDialog.setTitle("Loading");
        mAuthProgressDialog.setMessage("Authenticating with Server...");
        mAuthProgressDialog.setCancelable(false);

        del_link.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //s_user = username.getText().toString() + "@ahc.com";




            if(s_user!=null) {
                if (s_user.equals("")) {
                    Toast.makeText(Delete_presentation_link.this, "Invalid Link", Toast.LENGTH_LONG).show();
                } else {

                    Firebase linkref = mFirebaseRef.child("plinks").child(s_user);
                    linkref.removeValue();
                    Toast.makeText(Delete_presentation_link.this,"Link deleted successfully",Toast.LENGTH_LONG).show();

                }
            }


            }

        });


        delete_link_select.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu


                Firebase u_ref = mFirebaseRef.child("plinks");
                u_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    PopupMenu popup = new PopupMenu(Delete_presentation_link.this, delete_link_select);

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
                                delete_link_select.setText(item.getTitle());
                                //Toast.makeText(Delete_presentation_link.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
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




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delete_presentation_link, menu);
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
        if(!Global.isNetworkAvailable(Delete_presentation_link.this)) {
            Intent intent = new Intent(Delete_presentation_link.this, Signin_Activity.class);
            startActivity(intent);
            finish();
        }
    }
}
