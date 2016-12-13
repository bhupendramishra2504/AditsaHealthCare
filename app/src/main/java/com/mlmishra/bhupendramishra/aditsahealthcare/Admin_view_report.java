package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import android.widget.TextView;
import android.app.Activity;
import android.location.Geocoder;
import android.location.Address;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Admin_view_report extends Activity {
    String s_user,s_report,s_email,s_password,s_cpassword,userlogged;
    Button delete_user,user_list,report,submit,save,ater,aspec,fromdate,todate;
    TextView vra,to;
    private static final String FIREBASE_URL = "https://aditsa-healthcare.firebaseio.com";
    private ProgressDialog mAuthProgressDialog;
    String separator = System.getProperty("line.separator");
    /* A reference to the Firebase */
    private Firebase mFirebaseRef;
    String user_password="";
    boolean first_time_popup=false;
    PopupMenu popup_ter;
    String drdetails="";
    Handler threadHandler;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog.OnDateSetListener date1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_report);

        Intent i = getIntent();
        userlogged= i.getStringExtra("user");

        delete_user=(Button)findViewById(R.id.dusignup);
        user_list=(Button)findViewById(R.id.auser);
        report=(Button)findViewById(R.id.areport);
        submit=(Button)findViewById(R.id.asubmit);
        save=(Button)findViewById(R.id.asave);
        ater=(Button)findViewById(R.id.ater);
        aspec=(Button)findViewById(R.id.aspec);
        fromdate=(Button)findViewById(R.id.afdate);
        todate=(Button)findViewById(R.id.atdate);

        ater.setVisibility(View.GONE);
        aspec.setVisibility(View.GONE);


        vra=(TextView)findViewById(R.id.avdc);
        to=(TextView)findViewById(R.id.to);

        popup_ter = new PopupMenu(Admin_view_report.this, ater);

        mAuthProgressDialog = new ProgressDialog(Admin_view_report.this);
        mAuthProgressDialog.setTitle("Please Wait....");
        mAuthProgressDialog.setMessage("Fetching report data with server .....");
        mAuthProgressDialog.setCancelable(false);

        mFirebaseRef = new Firebase(FIREBASE_URL);
        myCalendar = Calendar.getInstance();
        Firebase usertype=mFirebaseRef.child(userlogged).child("type");
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



        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }

        };

        date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel2();
            }

        };


        fromdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                vra.setText("");
                // TODO Auto-generated method stub
                new DatePickerDialog(Admin_view_report.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        todate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                vra.setText("");
                // TODO Auto-generated method stub
                new DatePickerDialog(Admin_view_report.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        user_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                Firebase u_ref;
                Log.d("user type", Global.user_type);
                //Toast.makeText(Admin_view_report.this,"user type"+Global.user_type,Toast.LENGTH_LONG).show();
                if (Global.user_type.equals("asm")) {

                    u_ref = mFirebaseRef.child(userlogged).child("Subordinate");
                    u_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        PopupMenu popup = new PopupMenu(Admin_view_report.this, user_list);

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
                                    user_list.setText(item.getTitle());
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


                } else {
                    u_ref = mFirebaseRef.child("userlist");
                    u_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        PopupMenu popup = new PopupMenu(Admin_view_report.this, user_list);

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
                                    user_list.setText(item.getTitle());
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
            }
        });



        report.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PopupMenu popup = new PopupMenu(Admin_view_report.this, report);


                        popup.getMenu().add("TOUR REPORT");
                        popup.getMenu().add("DAILY CALL REPORT");
                        popup.getMenu().add("WEEKLY SALES REPORT");
                        popup.getMenu().add("USER TRACKING REPORT");
                        popup.getMenu().add("DOCTOR MASTER LIST");
                        popup.getMenu().add("USER LOG REPORT");
                        popup.getMenu().add("PLANNER LOCATION REPORT");



                        popup.getMenuInflater().inflate(R.menu.menu_admin_view_report, popup.getMenu());
                        //popup.setOutsideTouchable(true);
                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                s_report = (String) item.getTitle();
                                report.setText(item.getTitle());
                                if(s_report.equalsIgnoreCase("DOCTOR MASTER LIST"))
                                {
                                    ater.setVisibility(View.VISIBLE);
                                    aspec.setVisibility(View.VISIBLE);
                                    user_list.setVisibility(View.GONE);
                                    fromdate.setVisibility(View.GONE);
                                    todate.setVisibility(View.GONE);
                                    to.setVisibility(View.GONE);
                                }
                                else
                                {
                                    ater.setVisibility(View.GONE);
                                    aspec.setVisibility(View.GONE);
                                    user_list.setVisibility(View.VISIBLE);
                                    fromdate.setVisibility(View.VISIBLE);
                                    todate.setVisibility(View.VISIBLE);
                                    to.setVisibility(View.VISIBLE);
                                }

                                //Toast.makeText(Admin_view_report.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                //tracked_user.setText("User Selected for trackin: " + (String) item.getTitle());
                                //find_me();
                                return true;
                            }
                        });

                        popup.show();


                    }
                });
            }

        });


        aspec.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu


                PopupMenu popup = new PopupMenu(Admin_view_report.this, aspec);


                for( int i=0;i<Global.speciality.length;i++)
                {
                    popup.getMenu().add(Global.speciality[i]);
                }



                popup.getMenuInflater().inflate(R.menu.menu_update__dr_master__list, popup.getMenu());
                //popup.setOutsideTouchable(true);
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //s_report = (String) item.getTitle();
                        aspec.setText(item.getTitle());
                        //Toast.makeText(Admin_view_report.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        //tracked_user.setText("User Selected for trackin: " + (String) item.getTitle());
                        //find_me();
                        return true;
                    }
                });

                popup.show();

            }


            });



        ater.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!isFinishing()) {
                            if (!first_time_popup) {
                                Firebase u_ref = mFirebaseRef.child("territory_list");
                                u_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    //PopupMenu popup = new PopupMenu(Add_user.this, seluser);

                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {

                                        for (DataSnapshot child : snapshot.getChildren()) {
                                            if (child.getKey() != null) {
                                                popup_ter.getMenu().add(child.getKey().toString());
                                                Log.d("item", "");
                                            }
                                        }


                                        popup_ter.getMenuInflater().inflate(R.menu.menu_update__dr_master__list, popup_ter.getMenu());
                                        //popup.setOutsideTouchable(true);
                                        //registering popup with OnMenuItemClickListener
                                        popup_ter.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                            public boolean onMenuItemClick(MenuItem item) {
                                                //asm_user = (String) item.getTitle();
                                                ater.setText(item.getTitle());
                                                //Toast.makeText(Tracking_enable_self.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                                //tracked_user.setText("User Selected for trackin: " + item.getTitle());
                                                //find_me();
                                                return true;
                                            }
                                        });

                                        popup_ter.show();
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {
                                        // System.out.println("The read failed: ");
                                    }


                                });
                                first_time_popup = true;
                            } else
                                popup_ter.show();

                        } else
                            popup_ter.dismiss();
                    }
                });


            }
        });






        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //mAuthProgressDialog.show();
                if (Global.validftdate(fromdate.getText().toString(), todate.getText().toString())|s_report.equalsIgnoreCase("DOCTOR MASTER LIST")) {
                    //Toast.makeText(Admin_view_report.this,"Valid Dates Selected",Toast.LENGTH_LONG).show();
                    submit.setEnabled(false);
                    submit.setText("Please Wait...");
                    vra.setText("Please Wait while we load data from the server ....");

                    threadHandler = new Handler();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            threadHandler.post(new Runnable() {
                                @Override
                                public void run() {


                                    if (s_report.equalsIgnoreCase("USER TRACKING REPORT")) {

                                        submit.setEnabled(true);
                                        //Toast.makeText(Admin_view_report.this, "you reached here", Toast.LENGTH_LONG).show();
                                        report_user_tracking();
                                        //mAuthProgressDialog.dismiss();

                                    } else if (s_report.equalsIgnoreCase("DOCTOR MASTER LIST")) {

                                        if (!(ater.getText().toString().equalsIgnoreCase("select territory") | aspec.getText().toString().equalsIgnoreCase("select speciality"))) {

                                            Firebase dlist = mFirebaseRef.child("drlist").child(ater.getText().toString()).child(aspec.getText().toString());

                                            dlist.addListenerForSingleValueEvent(new ValueEventListener() {
                                                //PopupMenu popup = new PopupMenu(Add_user.this, seluser);
                                                String drdetails1 = "";
                                                String draddress = "";
                                                String draptime = "";
                                                String drreport = "";

                                                @Override
                                                public void onDataChange(DataSnapshot snapshot) {
                                                    int i = 0;
                                                    for (DataSnapshot child : snapshot.getChildren()) {
                                                        if (child.getKey() != null) {
                                                            i++;
                                                            drdetails1 = drdetails1 + separator + i + "." + child.getKey().toString();
                                                            vra.setText(drdetails1);

                                                        }
                                                    }
                                                    submit.setEnabled(true);
                                                    // mAuthProgressDialog.dismiss();
                                                }


                                                @Override
                                                public void onCancelled(FirebaseError firebaseError) {
                                                    // System.out.println("The read failed: ");
                                                }


                                            });


                                        } else
                                            Toast.makeText(Admin_view_report.this, "Invalid Selection - No Report Generated", Toast.LENGTH_LONG).show();
                                    } else if (s_report.equals("USER LOG REPORT")) {

                                        report_user_log();
                                        //mAuthProgressDialog.dismiss();

                                    } else if (s_report.equals("PLANNER LOCATION REPORT")) {

                                        planner_location_report();
                                        //mAuthProgressDialog.dismiss();

                                    }

                                    else if(s_report.equals("DAILY CALL REPORT"))
                                    {
                                        Firebase viewtourref = mFirebaseRef.child("users").child(s_user).child(s_report);

                                        viewtourref.addValueEventListener(new ValueEventListener() {
                                            String data;

                                            @Override
                                            public void onDataChange(DataSnapshot snapshot) {
                                                for (DataSnapshot child : snapshot.getChildren()) {
                                                    if (child.getValue().toString().split(separator).length >= 3) {
                                                        if (child != null && Global.date_validation2(child.getValue().toString().split(separator)[3].split(":")[1].trim(), fromdate.getText().toString(), todate.getText().toString()))
                                                            data = child.getKey().toString() + separator + child.getValue().toString() + separator + separator + data;
                                                        // Toast.makeText(Admin_view_report.this, "No report found", Toast.LENGTH_LONG).show();
                                                    }
                                                }

                                                if (data != "") {
                                                    vra.setText(data);
                                                    submit.setEnabled(true);
                                                }

                                                // mAuthProgressDialog.dismiss();
                                            }

                                            @Override
                                            public void onCancelled(FirebaseError firebaseError) {
                                                System.out.println("The read failed: " + firebaseError.getMessage());
                                            }
                                        });
                                    }

                                    else {
                                        Firebase viewtourref = mFirebaseRef.child("users").child(s_user).child(s_report);

                                        viewtourref.addValueEventListener(new ValueEventListener() {
                                            String data;

                                            @Override
                                            public void onDataChange(DataSnapshot snapshot) {
                                                for (DataSnapshot child : snapshot.getChildren()) {

                                                    if (child != null && Global.date_validation(child.getKey().toString().substring(0,11),fromdate.getText().toString(),todate.getText().toString()))
                                                        data = child.getKey().toString() + separator + child.getValue().toString() + separator + separator + data;
                                                   // Toast.makeText(Admin_view_report.this, "No report found", Toast.LENGTH_LONG).show();
                                                }

                                                if (data != "") {
                                                    vra.setText(data);
                                                    submit.setEnabled(true);
                                                }

                                                // mAuthProgressDialog.dismiss();
                                            }

                                            @Override
                                            public void onCancelled(FirebaseError firebaseError) {
                                                System.out.println("The read failed: " + firebaseError.getMessage());
                                            }
                                        });

                                    }
                                    //mAuthProgressDialog.dismiss();
                                    //submit.setEnabled(true);
                                }


                            });
                        }
                    }).start();
                    submit.setEnabled(true);
                    submit.setText("SUBMIT");

                }
                else
                    Toast.makeText(Admin_view_report.this,"Select Valid Dates",Toast.LENGTH_LONG).show();
            }

        });

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                Global.write_file("Admin" + s_user + s_report, vra.getText().toString(), Admin_view_report.this);

            }

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_view_report, menu);
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

    public void report_user_tracking()
    {
        try {
            // Loading map

        } catch (Exception e) {
            e.printStackTrace();
        }


        submit.setEnabled(false);

       // Toast.makeText(Tracking_enable_self.this,"Showing location for user : "+userlogged1,Toast.LENGTH_LONG).show();
        Firebase trackref = mFirebaseRef.child("users").child(s_user).child("LOCATIONS");
        Log.d("user",s_user);


       // mAuthProgressDialog.show();
        trackref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //mAuthProgressDialog.show();
                String datatoshow = "";
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child != null && Global.date_validation(child.getKey().toString().substring(0, 11), fromdate.getText().toString(), todate.getText().toString())) {
                        String data = child.getKey().toString();
                        String[] logdata = child.getValue().toString().split(",");
                        Log.d("date", data);
                        Log.d("long", logdata[0] + logdata[1]);
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(Admin_view_report.this, Locale.getDefault());

                        try {

                            if (logdata[0] != null && logdata[1] != null) {
                                addresses = geocoder.getFromLocation(Double.valueOf(logdata[0]).doubleValue(), Double.valueOf(logdata[1]).doubleValue(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city = addresses.get(0).getLocality();
                                String postalCode = addresses.get(0).getPostalCode();
                                datatoshow = separator + data + separator + "Longitute : " + logdata[1] + separator + "Latitute : " + logdata[0] + separator + "Address : " + address + separator + "City : " + city + separator + "Pin Code: " + postalCode + separator + separator + datatoshow;
                                //Toast.makeText(Admin_view_report.this, "you reached inside", Toast.LENGTH_LONG).show();

                            }
                        } catch (IOException io) {

                        }


                        // place_marker(Double.valueOf(logdata[1]).doubleValue(), Double.valueOf(logdata[0]).doubleValue(), data);

                    }
                }
                vra.setText(datatoshow);
                submit.setEnabled(true);
                //mAuthProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
                //mAuthProgressDialog.dismiss();
            }
        });



    }

    private void updateLabel1() {

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        fromdate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel2() {

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        todate.setText(sdf.format(myCalendar.getTime()));
    }


    public void planner_location_report()
    {
        Firebase trackref = mFirebaseRef.child("users").child(s_user).child("PLANNER LOCATIONS");
        Log.d("user",s_user);


        // mAuthProgressDialog.show();
        trackref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //mAuthProgressDialog.show();
                String datatoshow = "";
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child != null && Global.date_validation(child.getKey().toString().substring(0,11),fromdate.getText().toString(),todate.getText().toString())) {
                        String data = child.getKey().toString();
                        String[] drdate=child.getKey().toString().split(";");
                        String[] logdata = child.getValue().toString().split(",");
                        Log.d("date", data);
                        Log.d("long", logdata[0] + logdata[1]);
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(Admin_view_report.this, Locale.getDefault());

                        try {

                            if(logdata[0]!=null && logdata[1]!=null) {
                                addresses = geocoder.getFromLocation(Double.valueOf(logdata[0]).doubleValue(), Double.valueOf(logdata[1]).doubleValue(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city = addresses.get(0).getLocality();
                                String postalCode = addresses.get(0).getPostalCode();
                                datatoshow=separator+drdate[0]+separator+drdate[1]+separator+ "Longitute : " + logdata[1] + separator + "Latitute : " + logdata[0] + separator + "Address : " + address + separator + "City : " + city + separator + "Pin Code: " + postalCode + separator + separator+datatoshow;
                                //Toast.makeText(Admin_view_report.this, "you reached inside", Toast.LENGTH_LONG).show();

                            }
                        } catch (IOException io) {

                        }


                        // place_marker(Double.valueOf(logdata[1]).doubleValue(), Double.valueOf(logdata[0]).doubleValue(), data);

                    }
                }
                vra.setText(datatoshow);
                submit.setEnabled(true);
                //mAuthProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
                //mAuthProgressDialog.dismiss();
            }
        });


    }


    public void report_user_log()
    {

        submit.setEnabled(false);

        // Toast.makeText(Tracking_enable_self.this,"Showing location for user : "+userlogged1,Toast.LENGTH_LONG).show();
        Firebase trackref = mFirebaseRef.child("users").child(s_user).child("LOG");


        // mAuthProgressDialog.show();
        trackref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //mAuthProgressDialog.show();
                String datatoshow = "";
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child != null && Global.date_validation(child.getKey().toString().substring(0,11),fromdate.getText().toString(),todate.getText().toString())) {
                        String data = child.getKey().toString();
                        datatoshow=datatoshow+separator+data+" : "+child.getValue().toString()+separator;
                        vra.setText(datatoshow);

                        // place_marker(Double.valueOf(logdata[1]).doubleValue(), Double.valueOf(logdata[0]).doubleValue(), data);

                    }
                }

                submit.setEnabled(true);
                //mAuthProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
                //mAuthProgressDialog.dismiss();
            }
        });



    }




    @Override
    public void onResume() {
        super.onResume();
        if(!Global.isNetworkAvailable(Admin_view_report.this)) {
            Intent intent = new Intent(Admin_view_report.this, Signin_Activity.class);
            startActivity(intent);
            finish();
        }
    }
}
