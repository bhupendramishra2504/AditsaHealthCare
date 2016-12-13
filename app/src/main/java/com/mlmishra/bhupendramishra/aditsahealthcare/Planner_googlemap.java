package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;

import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.CameraUpdateFactory;
import android.util.Log;
import android.widget.TextView;
import android.app.Activity;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.view.View;
import android.widget.RelativeLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.media.SoundPool;
import android.media.AudioManager;
import android.media.MediaActionSound;
import android.widget.EditText;
import java.io.OutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Planner_googlemap extends Activity {

    private GoogleMap googleMap;
    private String userlogged1;
    private Firebase ref;
    private TextView tracked_user;
    Button t_user,tsss;
    RadioGroup radioGroup;
    EditText seldate;
    boolean  alldate=true,specdate=false;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;


    Spinner dropdown;
    LinearLayout r1;
    String businessType[] = { "Automobile", "Food", "Computers", "Education",
            "Personal", "Travel" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_googlemap);
        //getSupportActionBar().hide();


        Intent i = getIntent();
        userlogged1= i.getStringExtra("user");
        tracked_user=(TextView)findViewById(R.id.tes);
        t_user=(Button)findViewById(R.id.tsbutton1);
        tsss=(Button)findViewById(R.id.tsss);
        tracked_user.setText("Select user from dropdown to track");
        //tracked_user.setText("Tracking User : "+userlogged1);
        ref = new Firebase(Global.FIREBASE_URL);

        seldate=(EditText)findViewById(R.id.datet);

        myCalendar = Calendar.getInstance();

        seldate.setVisibility(View.GONE);


        radioGroup = (RadioGroup) findViewById(R.id.trg1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int chkid) {
                alldate = ((RadioButton) findViewById(R.id.I1)).isChecked();
                specdate=((RadioButton) findViewById(R.id.I2)).isChecked();
                t_user.setText("SELECT USER to track");

                if (alldate) {
                    seldate.setVisibility(View.GONE);

                }
                else if(specdate){

                    seldate.setVisibility(View.VISIBLE);

                }


            }
        });



        //tracked_user.append("i am here");
        //List<String> data=Global.userlistFirebase(ref.child("users").child("admin").child("userlist"));
        //Firebase u_ref=ref.child("users").child("admin").child("userlist");
        Firebase usertype=ref.child(userlogged1).child("type");
        usertype.addValueEventListener(new ValueEventListener() {
            String type;

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.getValue() != null) {

                    Global.user_type = snapshot.getValue().toString();


                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        tsss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //takeScreenshot();
                captureScreen();

            }
        });



        t_user.setOnClickListener(new View.OnClickListener()

                                  {

                                      @Override
                                      public void onClick(View v) {
                                          Firebase u_ref;
                                          //Creating the instance of PopupMenu
                                          //Toast.makeText(Tracking_enable_self.this,"usertype"+Global.user_type,Toast.LENGTH_LONG).show();
                                          if (Global.user_type != "") {
                                              if (Global.user_type.equals("asm")) {
                                                  u_ref = ref.child(userlogged1).child("Subordinate");
                                                  u_ref.addValueEventListener(new ValueEventListener() {
                                                      PopupMenu popup = new PopupMenu(Planner_googlemap.this, t_user);

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
                                                                  userlogged1 = (String) item.getTitle();
                                                                  t_user.setText(item.getTitle());
                                                                  // Toast.makeText(Tracking_enable_self.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                                                  tracked_user.setText("User Selected for trackin: " + item.getTitle());
                                                                  find_me();
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
                                                          System.out.println("The read failed: ");
                                                      }


                                                  });


                                              } else {
                                                  u_ref = ref.child("userlist");
                                                  u_ref.addValueEventListener(new ValueEventListener() {
                                                      PopupMenu popup = new PopupMenu(Planner_googlemap.this, t_user);

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
                                                                  userlogged1 = (String) item.getTitle();
                                                                  t_user.setText(item.getTitle());
                                                                  //Toast.makeText(Tracking_enable_self.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                                                  tracked_user.setText("User Selected for trackin: " + item.getTitle());
                                                                  find_me();
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


                                          } else
                                              Toast.makeText(Planner_googlemap.this, "No user to track", Toast.LENGTH_SHORT).show();
                                      }

                                  }

        );

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        seldate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Planner_googlemap.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });




    }


    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setZoomGesturesEnabled(true);


            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void updateLabel() {

        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        seldate.setText(sdf.format(myCalendar.getTime()));
    }

    private void place_marker(double longitude, double latitude,String date)
    {
        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(date);
        googleMap.addMarker(marker);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 25));

        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    private void clear_marker()
    {

        googleMap.clear();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tracking_enable_self, menu);
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

    public void find_me()
    {
        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }


        Firebase trackref = ref.child("users").child(userlogged1).child("PLANNER LOCATIONS");
        clear_marker();



        if(alldate) {
            trackref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        if (child != null) {
                            String data = child.getKey().toString();
                            String[] logdata = child.getValue().toString().split(",");
                            Log.d("date", data);
                            Log.d("long", logdata[0] + logdata[1]);


                            place_marker(Double.valueOf(logdata[1]).doubleValue(), Double.valueOf(logdata[0]).doubleValue(), data);
                            //Toast.makeText(Tracking_enable_self.this,"Showing location for user : "+userlogged1,Toast.LENGTH_LONG).show();

                        } else
                            Toast.makeText(Planner_googlemap.this, "No location found for this user", Toast.LENGTH_LONG).show();

                    }


                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read failed: " + firebaseError.getMessage());
                }
            });
        }
        else
        {
            trackref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        if (child != null) {
                            String data = child.getKey().toString();
                            String[] date1=data.split(";");
                            if(date1[0].substring(0,11).equalsIgnoreCase(seldate.getText().toString())) {
                                String[] logdata = child.getValue().toString().split(",");
                                Log.d("date", data);
                                Log.d("long", logdata[0] + logdata[1]);
                                place_marker(Double.valueOf(logdata[1]).doubleValue(), Double.valueOf(logdata[0]).doubleValue(), data);
                                //Toast.makeText(Planner_googlemap.this,"Showing location for user : "+userlogged1,Toast.LENGTH_LONG).show();
                            }

                        } else
                            Toast.makeText(Planner_googlemap.this, "No location found for this user on the specified date", Toast.LENGTH_LONG).show();

                    }


                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read failed: " + firebaseError.getMessage());
                }
            });
        }

    }



    @Override
    public void onResume() {
        super.onResume();
        initilizeMap();
        if(!Global.isNetworkAvailable(Planner_googlemap.this)) {
            Intent intent = new Intent(Planner_googlemap.this, Signin_Activity.class);
            startActivity(intent);
            finish();
        }
    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            MediaActionSound sound = new MediaActionSound();
            sound.play(MediaActionSound.SHUTTER_CLICK);
            Toast.makeText(Planner_googlemap.this,"ScreenShot taken at : "+mPath,Toast.LENGTH_LONG).show();





        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }


    public void captureScreen()
    {
        final Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        SnapshotReadyCallback callback = new SnapshotReadyCallback()
        {

            @Override
            public void onSnapshotReady(Bitmap snapshot)
            {
                // TODO Auto-generated method stub
                Bitmap bitmap = snapshot;

                FileOutputStream fout = null;

                String mPath = Environment.getExternalStorageDirectory().toString() + "/" +tracked_user.getText().toString()+ now + ".jpg";
                try
                {
                    fout = new FileOutputStream(mPath);
                    int quality = 100;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fout);
                    fout.flush();
                    fout.close();
                    MediaActionSound sound = new MediaActionSound();
                    sound.play(MediaActionSound.SHUTTER_CLICK);
                    Toast.makeText(Planner_googlemap.this,"ScreenShot taken at : "+mPath,Toast.LENGTH_LONG).show();


                }
                catch (FileNotFoundException e)
                {
                    // TODO Auto-generated catch block
                    Log.d("ImageCapture", "FileNotFoundException");
                    Log.d("ImageCapture", e.getMessage());

                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    Log.d("ImageCapture", "IOException");
                    Log.d("ImageCapture", e.getMessage());

                }

                //openShareImageDialog(filePath);
            }
        };

        googleMap.snapshot(callback);
    }






}
