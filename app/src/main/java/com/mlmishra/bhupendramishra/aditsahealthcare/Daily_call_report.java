package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import android.app.Dialog;
import java.util.Locale;
import android.widget.AutoCompleteTextView;
import java.util.ArrayList;
import java.util.List;
import android.widget.ArrayAdapter;
import android.view.MotionEvent;

public class Daily_call_report extends Activity {

    private Button addC,submit,ehq,select_speciality,select_territory,ffup;
    private String userlogged1;
    private TextView user,preview,dcrtv3;
    private EditText emphq,dcrrd,pro,mcal,mfol,ihb,calone,bioeli,ovitsa,comment,dcrcalls,inputSearch;
    private Firebase ref;
    private boolean first_time=false,first_time_popup=false,first_time_popup_dr=false;
    String separator = System.getProperty("line.separator");
    private CheckBox onleave;
    private int no_of_calls=0;
    String heading="";
    String content="";
    private DatePicker dpResult;
    private int year;
    private int month,month2;
    private int day;
    int year1,month1,day1;
    List<String> listOfString;
    public ArrayAdapter<String> adapter;
    private int count=0;

    static final int DATE_DIALOG_ID = 999;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;

    PopupMenu popup_territory,popup_dr;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_call_report);
        user=(TextView)findViewById(R.id.dcrtv0);
        dcrtv3=(TextView)findViewById(R.id.dcrtv3);
        preview=(TextView)findViewById(R.id.trtv1);
        emphq=(EditText)findViewById(R.id.dcrhq);
        pro=(EditText)findViewById(R.id.psr);
        mcal=(EditText)findViewById(R.id.mcal);
        mfol=(EditText)findViewById(R.id.mfol);
        ihb=(EditText)findViewById(R.id.ihb);
        calone=(EditText)findViewById(R.id.calone);
        bioeli=(EditText)findViewById(R.id.bioeli);
        ovitsa=(EditText)findViewById(R.id.ovitsa);
        dcrrd=(EditText)findViewById(R.id.dcrrd);

        lv = (ListView) findViewById(R.id.list_view11);
        inputSearch = (EditText) findViewById(R.id.inputSearch);

        comment=(EditText)findViewById(R.id.comment);
        dcrcalls=(EditText)findViewById(R.id.dcrcalls);


        onleave=(CheckBox)findViewById(R.id.dcrcb);

        addC=(Button)findViewById(R.id.dcrcall);
        submit=(Button)findViewById(R.id.dcrsubmit);
        ehq=(Button)findViewById(R.id.ehq);

        ffup=(Button)findViewById(R.id.ffup);

        select_speciality=(Button)findViewById(R.id.selspeciality);
        select_territory=(Button)findViewById(R.id.selter);




        myCalendar = Calendar.getInstance();
        lv.setVisibility(View.GONE);


        inputSearch.setVisibility(View.GONE);
        lv.setVisibility(View.GONE);
        select_speciality.setVisibility(View.GONE);
        select_territory.setVisibility(View.GONE);
        pro.setVisibility(View.GONE);
        mcal.setVisibility(View.GONE);
        mfol.setVisibility(View.GONE);
        ihb.setVisibility(View.GONE);
        calone.setVisibility(View.GONE);
        bioeli.setVisibility(View.GONE);
        ovitsa.setVisibility(View.GONE);
        comment.setVisibility(View.VISIBLE);
        addC.setVisibility(View.VISIBLE);
        dcrtv3.setVisibility(View.GONE);
        ehq.setVisibility(View.VISIBLE);
        ffup.setVisibility(View.GONE);
        submit.setEnabled(false);
        //submit.setVisibility(View.GONE);








        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {


                // When user changed the Text
                if(!inputSearch.getText().toString().equalsIgnoreCase("") && !select_speciality.getText().toString().equalsIgnoreCase("SELECT SPECIALITY") && !select_territory.getText().toString().equalsIgnoreCase("SELECT TERRITORY")) {
                    lv.setVisibility(View.VISIBLE);
                    Daily_call_report.this.adapter.getFilter().filter(cs);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });



        lv.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });




        popup_territory = new PopupMenu(Daily_call_report.this, select_territory);


        select_speciality.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu


                PopupMenu popup = new PopupMenu(Daily_call_report.this, select_speciality);

                for( int i=0;i<Global.speciality.length;i++)
                {
                    popup.getMenu().add(Global.speciality[i]);
                }

              /*  popup.getMenu().add("PHYSICIAN");
                popup.getMenu().add("DERMATOLOGIST");
                popup.getMenu().add("GYNECOLOGIST");
                popup.getMenu().add("ORTHOPEDIC");
                popup.getMenu().add("GENERAL PRACTITIONER");
                popup.getMenu().add("DIABETOLOGIST");*/


                //first_time_popup_dr = false;
                //popup_dr = new PopupMenu(Daily_call_report.this, select_docter);

                popup.getMenuInflater().inflate(R.menu.menu_update__dr_master__list, popup.getMenu());
                //popup.setOutsideTouchable(true);
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //s_report = (String) item.getTitle();
                        select_speciality.setText(item.getTitle());
                        //Toast.makeText(Admin_view_report.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        //tracked_user.setText("User Selected for trackin: " + (String) item.getTitle());
                        //find_me();
                        list_populate();
                        return true;
                    }
                });

                popup.show();
            }
        });


        ffup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu


                PopupMenu popup = new PopupMenu(Daily_call_report.this, ffup);


                popup.getMenu().add("FIRST TIME VISIT");
                popup.getMenu().add("FOLLOW UP");


                //first_time_popup_dr = false;
                //popup_dr = new PopupMenu(Daily_call_report.this, select_docter);

                popup.getMenuInflater().inflate(R.menu.menu_update__dr_master__list, popup.getMenu());
                //popup.setOutsideTouchable(true);
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //s_report = (String) item.getTitle();
                        ffup.setText(item.getTitle());
                        //Toast.makeText(Admin_view_report.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        //tracked_user.setText("User Selected for trackin: " + (String) item.getTitle());
                        //find_me();

                        return true;
                    }
                });

                popup.show();
            }
        });


        select_territory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu

               // select_docter.setText("Select Docter");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!isFinishing()) {
                            if (!first_time_popup) {
                                Firebase u_ref = ref.child("territory_list");
                                u_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    //PopupMenu popup = new PopupMenu(Add_user.this, seluser);

                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {

                                        for (DataSnapshot child : snapshot.getChildren()) {
                                            if (child.getKey() != null) {
                                                popup_territory.getMenu().add(child.getKey().toString());
                                                Log.d("item", "");
                                            }
                                        }

                                        //first_time_popup_dr = false;
                                       // popup_dr = new PopupMenu(Daily_call_report.this, select_docter);
                                        popup_territory.getMenuInflater().inflate(R.menu.menu_update__dr_master__list, popup_territory.getMenu());
                                        //popup.setOutsideTouchable(true);
                                        //registering popup with OnMenuItemClickListener
                                        popup_territory.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                            public boolean onMenuItemClick(MenuItem item) {
                                                //asm_user = (String) item.getTitle();
                                                select_territory.setText(item.getTitle());
                                                list_populate();
                                                //Toast.makeText(Tracking_enable_self.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                                //tracked_user.setText("User Selected for trackin: " + item.getTitle());
                                                //find_me();
                                                return true;
                                            }
                                        });

                                        popup_territory.show();
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {
                                        // System.out.println("The read failed: ");
                                    }


                                });
                                first_time_popup = true;
                            } else
                                popup_territory.show();

                        } else
                            popup_territory.dismiss();
                    }
                });


            }
        });



        ehq.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu


                PopupMenu popup = new PopupMenu(Daily_call_report.this, ehq);


                popup.getMenu().add("HEADQUARTER");
                popup.getMenu().add("EX STATION");
                popup.getMenu().add("OUTSTATION");



                popup.getMenuInflater().inflate(R.menu.menu_daily_call_report, popup.getMenu());
                //popup.setOutsideTouchable(true);
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //s_report = (String) item.getTitle();
                        ehq.setText(item.getTitle());
                        //Toast.makeText(Admin_view_report.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        //tracked_user.setText("User Selected for trackin: " + (String) item.getTitle());
                        //find_me();
                        return true;
                    }
                });

                popup.show();
            }
        });


      /*  select_docter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Creating the instance of PopupMenu
                if(!(select_territory.getText().toString().equalsIgnoreCase("Select TERRITORY")|select_speciality.getText().toString().equalsIgnoreCase("SELECT SPECIALITY"))) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (!isFinishing()) {
                                if (!first_time_popup_dr) {
                                    Firebase u_ref = ref.child("drlist").child(select_territory.getText().toString()).child(select_speciality.getText().toString());

                                    u_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        //PopupMenu popup = new PopupMenu(Add_user.this, seluser);

                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {

                                            for (DataSnapshot child : snapshot.getChildren()) {
                                                if (child.getKey() != null) {
                                                    popup_dr.getMenu().add(child.getKey().toString());
                                                    Log.d("item", "");
                                                }
                                            }


                                            popup_dr.getMenuInflater().inflate(R.menu.menu_update__dr_master__list, popup_dr.getMenu());
                                            //popup.setOutsideTouchable(true);
                                            //registering popup with OnMenuItemClickListener
                                            popup_dr.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                public boolean onMenuItemClick(MenuItem item) {
                                                    //asm_user = (String) item.getTitle();
                                                    select_docter.setText(item.getTitle());
                                                    //Toast.makeText(Tracking_enable_self.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                                    //tracked_user.setText("User Selected for trackin: " + item.getTitle());
                                                    //find_me();
                                                    return true;
                                                }
                                            });

                                            popup_dr.show();
                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {
                                            // System.out.println("The read failed: ");
                                        }


                                    });
                                    first_time_popup_dr = true;
                                } else
                                    popup_dr.show();

                            } else
                                popup_dr.dismiss();
                        }
                    });
                }
                else
                    Toast.makeText(Daily_call_report.this,"Select correct territory and Speciality to Proceed",Toast.LENGTH_LONG).show();


            }
        });*/




        ref = new Firebase(Global.FIREBASE_URL);
        Intent i = getIntent();
        userlogged1= i.getStringExtra("user");
        user.setText("User logged as : " + userlogged1);

        onleave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                               @Override
                                               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                   if (isChecked) {
                                                       user.setVisibility(View.GONE);
                                                       preview.setVisibility(View.GONE);
                                                       emphq.setVisibility(View.GONE);

                                                       inputSearch.setVisibility(View.GONE);
                                                       lv.setVisibility(View.GONE);

                                                       select_speciality.setVisibility(View.GONE);
                                                       select_territory.setVisibility(View.GONE);

                                                       pro.setVisibility(View.GONE);
                                                       mcal.setVisibility(View.GONE);
                                                       mfol.setVisibility(View.GONE);
                                                       ihb.setVisibility(View.GONE);
                                                       calone.setVisibility(View.GONE);
                                                       bioeli.setVisibility(View.GONE);
                                                       ovitsa.setVisibility(View.GONE);
                                                       comment.setVisibility(View.VISIBLE);
                                                       addC.setVisibility(View.GONE);
                                                       dcrtv3.setVisibility(View.GONE);
                                                       ehq.setVisibility(View.GONE);
                                                       dcrcalls.setVisibility(View.GONE);
                                                       ffup.setVisibility(View.GONE);
                                                       submit.setEnabled(true);

                                                   } else {
                                                       if(!first_time)
                                                       {
                                                           user.setVisibility(View.VISIBLE);
                                                           preview.setVisibility(View.VISIBLE);
                                                           emphq.setVisibility(View.VISIBLE);
                                                           inputSearch.setVisibility(View.GONE);
                                                           lv.setVisibility(View.GONE);
                                                           select_speciality.setVisibility(View.GONE);
                                                           select_territory.setVisibility(View.GONE);
                                                           pro.setVisibility(View.GONE);
                                                           mcal.setVisibility(View.GONE);
                                                           mfol.setVisibility(View.GONE);
                                                           ihb.setVisibility(View.GONE);
                                                           calone.setVisibility(View.GONE);
                                                           bioeli.setVisibility(View.GONE);
                                                           ovitsa.setVisibility(View.GONE);
                                                           comment.setVisibility(View.VISIBLE);
                                                           addC.setVisibility(View.VISIBLE);
                                                           dcrtv3.setVisibility(View.GONE);
                                                           ehq.setVisibility(View.VISIBLE);
                                                           dcrcalls.setVisibility(View.GONE);
                                                           ffup.setVisibility(View.GONE);
                                                           dcrrd.setVisibility(View.VISIBLE);
                                                           comment.setVisibility(View.VISIBLE);
                                                           dcrcalls.setVisibility(View.VISIBLE);
                                                           submit.setEnabled(false);
                                                       }
                                                       else {


                                                           inputSearch.setVisibility(View.VISIBLE);
                                                           lv.setVisibility(View.GONE);

                                                           select_speciality.setVisibility(View.VISIBLE);
                                                           select_territory.setVisibility(View.VISIBLE);
                                                           preview.setVisibility(View.VISIBLE);
                                                           pro.setVisibility(View.VISIBLE);
                                                           mcal.setVisibility(View.VISIBLE);
                                                           mfol.setVisibility(View.VISIBLE);
                                                           ihb.setVisibility(View.VISIBLE);
                                                           calone.setVisibility(View.VISIBLE);
                                                           bioeli.setVisibility(View.VISIBLE);
                                                           ovitsa.setVisibility(View.VISIBLE);
                                                           comment.setVisibility(View.VISIBLE);
                                                           addC.setVisibility(View.VISIBLE);
                                                           dcrtv3.setVisibility(View.VISIBLE);
                                                           ehq.setVisibility(View.VISIBLE);
                                                           dcrcalls.setVisibility(View.VISIBLE);
                                                           ffup.setVisibility(View.VISIBLE);
                                                           dcrcalls.setVisibility(View.VISIBLE);
                                                           submit.setEnabled(false);
                                                       }
                                                   }
                                               }
                                           }
        );

        addC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!first_time) {
                    if (emphq.getText().toString() != null  && !ehq.getText().toString().equalsIgnoreCase("Select station") && dcrrd.getText().toString()!=null && dcrcalls.getText().toString()!=null ) {
                        heading = "Daily Call Report" + separator + separator + "Employee Name: " + userlogged1 + separator + "Date of Submission : "+dcrrd.getText().toString()+ separator + "Employee Headquarter : " + emphq.getText().toString() + separator + "Employee Posting " + ehq.getText().toString()  + separator;
                        first_time = true;
                        emphq.setFocusable(false);
                        preview.setText(heading);

                        user.setVisibility(View.GONE);
                        emphq.setVisibility(View.GONE);
                        dcrrd.setVisibility(View.GONE);

                        inputSearch.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);

                        select_speciality.setVisibility(View.VISIBLE);
                        select_territory.setVisibility(View.VISIBLE);

                        pro.setVisibility(View.VISIBLE);
                        mcal.setVisibility(View.VISIBLE);
                        mfol.setVisibility(View.VISIBLE);
                        ihb.setVisibility(View.VISIBLE);
                        calone.setVisibility(View.VISIBLE);
                        bioeli.setVisibility(View.VISIBLE);
                        ovitsa.setVisibility(View.VISIBLE);
                        comment.setVisibility(View.VISIBLE);
                        addC.setVisibility(View.VISIBLE);
                        dcrtv3.setVisibility(View.VISIBLE);
                        ehq.setVisibility(View.GONE);
                        dcrcalls.setVisibility(View.VISIBLE);
                        ffup.setVisibility(View.VISIBLE);
                        preview.setVisibility(View.VISIBLE);

                    } else
                        Toast.makeText(Daily_call_report.this, "Enter valid data", Toast.LENGTH_LONG).show();
                } else {

                    if (no_of_calls <= Integer.parseInt(dcrcalls.getText().toString()) && !select_territory.getText().toString().equalsIgnoreCase("SELECT TERRITORY") && !select_speciality.getText().toString().equalsIgnoreCase("SELECT SPECIALITY") && !ffup.getText().toString().equalsIgnoreCase("INTRODUCTION") && inputSearch.getText().toString()!=null && pro.getText().toString()!=null && mcal.getText().toString()!=null && mfol.getText().toString()!=null && ihb.getText().toString()!=null && calone.getText().toString()!=null && bioeli.getText().toString()!=null && ovitsa.getText().toString()!=null) {
                        content = content + separator + separator + "VISIT TYPE : " + ffup.getText().toString() + separator + "Doctor's Name : " + inputSearch.getText().toString() + separator + "Territory : " + select_territory.getText().toString() + separator + "Doctor's speciality : " + select_speciality.getText().toString() + separator + "PROGEMMOM SR (QTY) : " + pro.getText().toString() + separator + "MOMMI CAL (QTY) : " + mcal.getText().toString() + separator + "MOMMI FOL (QTY) : " + mfol.getText().toString() + separator + "IRROW HB (QTY) : " + ihb.getText().toString() + separator + "CALITSA ONE (QTY) : " + calone.getText().toString() + separator + "BIO ELIXSA : " + bioeli.getText().toString() + separator + "OVITSA : " + ovitsa.getText().toString() + separator;
                        no_of_calls++;
                        preview.setText(heading + separator + "Number of calls : " + no_of_calls + separator + content);

                    }
                    else
                        Toast.makeText(Daily_call_report.this, "Enter valid data", Toast.LENGTH_LONG).show();

                    if (no_of_calls == Integer.parseInt(dcrcalls.getText().toString()))
                        submit.setEnabled(true);


                }

                select_speciality.setText("SELECT SPECIALITY");
                select_territory.setText("SELECT TERRITORY");
                ffup.setText("INTRODUCTION");
                inputSearch.setText("");
                pro.setText("");
                mcal.setText("");
                mfol.setText("");
                ihb.setText("");
                calone.setText("");
                bioeli.setText("");
                ovitsa.setText("");



            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean checkedonleave = ((CheckBox) findViewById(R.id.dcrcb)).isChecked();



                String comment1="";
                String dcrrd1="";
                if(comment.getText()!=null)
                {
                    comment1 =comment.getText().toString();
                }

                if(!checkedonleave) {
                    if(no_of_calls==Integer.parseInt(dcrcalls.getText().toString()))
                    add_daily_report_user(preview.getText().toString() + separator + "Comments/Remarks : " + comment1);
                    else
                        Toast.makeText(Daily_call_report.this,"Data call Report incomplete, check Number of calls/total calls data entered", Toast.LENGTH_LONG).show();
                }
                else
                {
                    add_daily_report_user("Employee Name : " + userlogged1 + separator + "NO DCR Submitted because employee is on leave" + separator + "Date of Submission : " + dcrrd.getText().toString() + separator + "Comments/Remarks : " + comment1);
                }
                Log.d("Tour Report", preview.getText().toString());
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
                updateLabel();
            }

        };


        dcrrd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Daily_call_report.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });





    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_daily_call_report, menu);
        return true;
    }

    private void add_daily_report_user(String tour_report)
    {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        String formattedDate = df.format(c.getTime());
        Firebase tourref = ref.child("users").child(userlogged1).child("DAILY CALL REPORT");
        Map<String, Object> nickname = new HashMap<String, Object>();
        nickname.put(formattedDate, tour_report);
        tourref.updateChildren(nickname);
        Toast.makeText(Daily_call_report.this, "Daily Call Report Submitted Successfully", Toast.LENGTH_LONG).show();
        Global.user_log(ref, userlogged1, "DAILY CALL REPORT : SUBMITTED");
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
        if(!Global.isNetworkAvailable(Daily_call_report.this)) {
            Intent intent = new Intent(Daily_call_report.this, Signin_Activity.class);
            startActivity(intent);
            finish();
        }
    }

    private void updateLabel() {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dcrrd.setText(sdf.format(myCalendar.getTime()));
    }


    public void list_populate()
    {
        if (!(select_speciality.getText().toString().equalsIgnoreCase("select speciality") | select_territory.getText().toString().equalsIgnoreCase("select territory"))) {
            // aseldoc.setFocusable(true);
            lv.setVisibility(View.VISIBLE);
            Firebase u_ref = ref.child("drlist").child(select_territory.getText().toString()).child(select_speciality.getText().toString());

            u_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                //PopupMenu popup = new PopupMenu(Add_user.this, seluser);

                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    listOfString = new ArrayList<String>();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        if (child.getKey() != null) {
                            listOfString.add(child.getKey().toString());
                            Log.d("item", "");
                        }
                    }

                    adapter = new ArrayAdapter<String>(Daily_call_report.this,
                            R.layout.list, listOfString);
                    adapter.notifyDataSetChanged();
                    lv.setAdapter(adapter);
                    registerForContextMenu(lv);

                    lv.invalidateViews();
                    // listening to single list item on click
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {

                            // selected item
                            String product = ((TextView) view).getText().toString();
                            inputSearch.setText(product);
                            lv.setVisibility(View.GONE);



                        }
                    });

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    // System.out.println("The read failed: ");
                }


            });
        } else {
            //aseldoc.setFocusable(false);
            //Toast.makeText(Daily_call_report.this, "Select valid territory and speciality selection", Toast.LENGTH_LONG).show();
        }

    }


}






