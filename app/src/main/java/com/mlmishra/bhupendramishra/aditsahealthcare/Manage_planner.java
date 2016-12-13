package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mlmishra.bhupendramishra.aditsahealthcare.R;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.widget.EditText;

public class Manage_planner extends Activity {

    Button upload_planner,sel_user,sel_ter,sel_spec,submit,submitr,mpfdate,mptdate,mpsave;
    EditText pudate,inputSearch;
    Firebase ref;
    boolean bulkentry,singleentry,seeplanner;
    PopupMenu popup_territory;
    private boolean first_time=false,first_time_popup=false;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date,date1,date2;
    List<String> listOfString;
    public ArrayAdapter<String> adapter;
    private int count=0;
    ListView lv;
    TextView mpvdc,mpto,mpse;
    String separator = System.getProperty("line.separator");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_planner);
        ref=new Firebase(Global.FIREBASE_URL);
        upload_planner=(Button)findViewById(R.id.mpup);
        sel_user=(Button)findViewById(R.id.mpuser);
        sel_ter=(Button)findViewById(R.id.mpselter);
        sel_spec=(Button)findViewById(R.id.mpspec);
        submit=(Button)findViewById(R.id.mpsubmit);
        submitr=(Button)findViewById(R.id.mpsubmitr);
        mpfdate=(Button)findViewById(R.id.mpfdate);
        mptdate=(Button)findViewById(R.id.mptdate);
        mpsave=(Button)findViewById(R.id.mpsave);
        mpvdc=(TextView)findViewById(R.id.mpvdc);
        mpto=(TextView)findViewById(R.id.mpto);
        mpse=(TextView)findViewById(R.id.mpse);

        pudate=(EditText)findViewById(R.id.dcrrd);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        lv = (ListView) findViewById(R.id.list_view11);
        lv.setVisibility(View.GONE);

        bulkentry = ((RadioButton) findViewById(R.id.U11)).isChecked();
        singleentry = ((RadioButton) findViewById(R.id.U12)).isChecked();
        seeplanner = ((RadioButton) findViewById(R.id.U13)).isChecked();

        if (bulkentry) {

            upload_planner.setVisibility(View.VISIBLE);
            sel_user.setVisibility(View.GONE);
            sel_spec.setVisibility(View.GONE);
            sel_ter.setVisibility(View.GONE);
            inputSearch.setVisibility(View.GONE);
            pudate.setVisibility(View.GONE);
            lv.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
            submitr.setVisibility(View.GONE);
            mpsave.setVisibility(View.GONE);
            mpfdate.setVisibility(View.GONE);
            mptdate.setVisibility(View.GONE);
            mpvdc.setVisibility(View.GONE);
            mpto.setVisibility(View.GONE);
            mpse.setVisibility(View.GONE);



        } else if (singleentry) {
            upload_planner.setVisibility(View.GONE);
            sel_user.setVisibility(View.VISIBLE);
            sel_spec.setVisibility(View.VISIBLE);
            sel_ter.setVisibility(View.VISIBLE);
            inputSearch.setVisibility(View.VISIBLE);
            pudate.setVisibility(View.VISIBLE);
            lv.setVisibility(View.VISIBLE);
            submit.setVisibility(View.VISIBLE);
            submitr.setVisibility(View.GONE);
            mpsave.setVisibility(View.GONE);
            mpfdate.setVisibility(View.GONE);
            mptdate.setVisibility(View.GONE);
            mpvdc.setVisibility(View.GONE);
            mpto.setVisibility(View.GONE);
            mpse.setVisibility(View.VISIBLE);
        }
        else if (seeplanner) {
            upload_planner.setVisibility(View.GONE);
            sel_user.setVisibility(View.VISIBLE);
            sel_spec.setVisibility(View.GONE);
            sel_ter.setVisibility(View.GONE);
            inputSearch.setVisibility(View.GONE);
            pudate.setVisibility(View.GONE);
            lv.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
            submitr.setVisibility(View.VISIBLE);
            mpsave.setVisibility(View.VISIBLE);
            mpfdate.setVisibility(View.VISIBLE);
            mptdate.setVisibility(View.VISIBLE);
            mpvdc.setVisibility(View.VISIBLE);
            mpto.setVisibility(View.VISIBLE);
            mpse.setVisibility(View.GONE);
        }


        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int chkid) {
                bulkentry = ((RadioButton) findViewById(R.id.U11)).isChecked();
                singleentry = ((RadioButton) findViewById(R.id.U12)).isChecked();
                seeplanner = ((RadioButton) findViewById(R.id.U13)).isChecked();

                if (bulkentry) {

                    upload_planner.setVisibility(View.VISIBLE);
                    sel_user.setVisibility(View.GONE);
                    sel_spec.setVisibility(View.GONE);
                    sel_ter.setVisibility(View.GONE);
                    inputSearch.setVisibility(View.GONE);
                    pudate.setVisibility(View.GONE);
                    lv.setVisibility(View.GONE);
                    submit.setVisibility(View.GONE);
                    submitr.setVisibility(View.GONE);
                    mpsave.setVisibility(View.GONE);
                    mpfdate.setVisibility(View.GONE);
                    mptdate.setVisibility(View.GONE);
                    mpvdc.setVisibility(View.GONE);
                    mpto.setVisibility(View.GONE);
                    mpse.setVisibility(View.GONE);

                } else if (singleentry) {
                    upload_planner.setVisibility(View.GONE);
                    sel_user.setVisibility(View.VISIBLE);
                    sel_spec.setVisibility(View.VISIBLE);
                    sel_ter.setVisibility(View.VISIBLE);
                    inputSearch.setVisibility(View.VISIBLE);
                    pudate.setVisibility(View.VISIBLE);
                    //lv.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    submitr.setVisibility(View.GONE);
                    mpsave.setVisibility(View.GONE);
                    mpfdate.setVisibility(View.GONE);
                    mptdate.setVisibility(View.GONE);
                    mpvdc.setVisibility(View.GONE);
                    mpto.setVisibility(View.GONE);
                    mpse.setVisibility(View.VISIBLE);
                }
                else if (seeplanner) {
                    upload_planner.setVisibility(View.GONE);
                    sel_user.setVisibility(View.VISIBLE);
                    sel_spec.setVisibility(View.GONE);
                    sel_ter.setVisibility(View.GONE);
                    inputSearch.setVisibility(View.GONE);
                    pudate.setVisibility(View.GONE);
                    lv.setVisibility(View.GONE);
                    submit.setVisibility(View.GONE);
                    submitr.setVisibility(View.VISIBLE);
                    mpsave.setVisibility(View.VISIBLE);
                    mpfdate.setVisibility(View.VISIBLE);
                    mptdate.setVisibility(View.VISIBLE);
                    mpvdc.setVisibility(View.VISIBLE);
                    mpto.setVisibility(View.VISIBLE);
                    mpse.setVisibility(View.GONE);
                }


            }
        });

        date1 = new DatePickerDialog.OnDateSetListener() {

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

        date2 = new DatePickerDialog.OnDateSetListener() {

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


        mpfdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mpvdc.setText("");
                // TODO Auto-generated method stub
                new DatePickerDialog(Manage_planner.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mptdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mpvdc.setText("");
                // TODO Auto-generated method stub
                new DatePickerDialog(Manage_planner.this, date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });




        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {


                // When user changed the Text
                if (!inputSearch.getText().toString().equalsIgnoreCase("") && !sel_spec.getText().toString().equalsIgnoreCase("SELECT SPECIALITY") && !sel_ter.getText().toString().equalsIgnoreCase("SELECT TERRITORY")) {
                    lv.setVisibility(View.VISIBLE);
                    Manage_planner.this.adapter.getFilter().filter(cs);
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


        sel_spec.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu


                PopupMenu popup = new PopupMenu(Manage_planner.this, sel_spec);




                for( int i=0;i<Global.speciality.length;i++)
                {
                    popup.getMenu().add(Global.speciality[i]);
                }



                //first_time_popup_dr = false;
                //popup_dr = new PopupMenu(Daily_call_report.this, select_docter);

                popup.getMenuInflater().inflate(R.menu.menu_update__dr_master__list, popup.getMenu());
                //popup.setOutsideTouchable(true);
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //s_report = (String) item.getTitle();
                        sel_spec.setText(item.getTitle());
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

        popup_territory = new PopupMenu(Manage_planner.this, sel_ter);


        sel_ter.setOnClickListener(new View.OnClickListener() {

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
                                                sel_ter.setText(item.getTitle());
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


        myCalendar = Calendar.getInstance();

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


        pudate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Manage_planner.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sel_user.getText().toString().equalsIgnoreCase("Select User") && !sel_ter.getText().toString().equalsIgnoreCase("Select territory") && !sel_spec.getText().toString().equalsIgnoreCase("Select Speciality") && !pudate.getText().toString().equalsIgnoreCase("") && !inputSearch.getText().toString().equalsIgnoreCase("")) {
                    Firebase planner_ref = ref.child("users").child(sel_user.getText().toString()).child("planner").child(pudate.getText().toString());
                    Map<String, Object> nickname = new HashMap<String, Object>();
                    nickname.put(inputSearch.getText().toString(), sel_spec.getText().toString());
                    planner_ref.updateChildren(nickname);
                    String data=mpse.getText().toString()+separator+inputSearch.getText().toString()+" : "+sel_spec.getText().toString();
                    mpse.setText(data);
                    Toast.makeText(Manage_planner.this, "Event added in User Planner list", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(Manage_planner.this, "Please Enter valid data before submitting", Toast.LENGTH_LONG).show();
            }
        });



        sel_user.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFinishing()) {

                            Firebase u_ref = ref.child("userlist");
                            u_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                PopupMenu popup = new PopupMenu(Manage_planner.this, sel_user);

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

                                            sel_user.setText(item.getTitle());
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
            }
        });

        submitr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //mAuthProgressDialog.show();
               submitr.setEnabled(false);
                    submit.setText("Please Wait...");
                    mpvdc.setText("Please Wait while we load data from the server ....");
                if(Global.validftdate(mpfdate.getText().toString(), mptdate.getText().toString()))
                {

                    Toast.makeText(Manage_planner.this,"Date Range are valid", Toast.LENGTH_LONG).show();
                    Firebase viewtourref = ref.child("users").child(sel_user.getText().toString()).child("planner");

                    viewtourref.addValueEventListener(new ValueEventListener() {
                      String data,drdata="";
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot child : snapshot.getChildren()) {

                                if (child != null && Global.date_validation1(child.getKey().toString(), mpfdate.getText().toString(), mptdate.getText().toString())){
                                    drdata="";
                                       // && Global.date_validation1(child.getKey().toString(), mpfdate.getText().toString(), mptdate.getText().toString())) {
                                    //Toast.makeText(Manage_planner.this,"you reached here", Toast.LENGTH_LONG).show();
                                    Map<String, Object> map = (Map<String, Object>) child.getValue();
                                    for (Map.Entry<String, Object> entry : map.entrySet())
                                    {
                                        drdata=entry.getKey()+" : "+entry.getValue()+separator+drdata;
                                    }

                                        data = child.getKey().toString() +separator+ separator+drdata +separator + separator + separator + data+separator;



                                }


                            }
                            mpvdc.setText(data);
                            submitr.setEnabled(true);
                        }

                            @Override
                            public void onCancelled (FirebaseError firebaseError){
                                System.out.println("The read failed: " + firebaseError.getMessage());
                            }
                        }

                        );

                    }
                else
                    Toast.makeText(Manage_planner.this,"Invalid Data Range Selected",Toast.LENGTH_LONG).show();
                            //mAuthProgressDialog.dismiss();




                }});


        int updatecount=Global.upload_planner(ref,Manage_planner.this);

        if(updatecount>0)
        {

            Toast.makeText(Manage_planner.this, "Record Updated : " + updatecount, Toast.LENGTH_LONG).show();
        }
    }

    private void updateLabel() {

        String myFormat = "ddMMyyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        pudate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_planner, menu);
        return true;
    }

    public void list_populate()
    {
        if (!(sel_spec.getText().toString().equalsIgnoreCase("select speciality") | sel_ter.getText().toString().equalsIgnoreCase("select territory"))) {
            // aseldoc.setFocusable(true);
            lv.setVisibility(View.VISIBLE);
            Firebase u_ref = ref.child("drlist").child(sel_ter.getText().toString()).child(sel_spec.getText().toString());

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

                    adapter = new ArrayAdapter<String>(Manage_planner.this,
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

    private void updateLabel1() {

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mpfdate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel2() {

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mptdate.setText(sdf.format(myCalendar.getTime()));
    }



}
