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
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mlmishra.bhupendramishra.aditsahealthcare.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Self_planner extends AppCompatActivity {
    Button sel_ter,sel_spec,submit;
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
    private String userlogged1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_planner);

        ref=new Firebase(Global.FIREBASE_URL);
        Intent i = getIntent();
        userlogged1 = i.getStringExtra("user");

        sel_ter=(Button)findViewById(R.id.mpselter);
        sel_spec=(Button)findViewById(R.id.mpspec);
        submit=(Button)findViewById(R.id.mpsubmit);


        mpvdc=(TextView)findViewById(R.id.mpvdc);
        mpto=(TextView)findViewById(R.id.mpto);
        mpse=(TextView)findViewById(R.id.mpse);

        pudate=(EditText)findViewById(R.id.dcrrd);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        lv = (ListView) findViewById(R.id.list_view11);
        lv.setVisibility(View.GONE);










        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {


                // When user changed the Text
                if (!inputSearch.getText().toString().equalsIgnoreCase("") && !sel_spec.getText().toString().equalsIgnoreCase("SELECT SPECIALITY") && !sel_ter.getText().toString().equalsIgnoreCase("SELECT TERRITORY")) {
                    lv.setVisibility(View.VISIBLE);
                    Self_planner.this.adapter.getFilter().filter(cs);
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


                PopupMenu popup = new PopupMenu(Self_planner.this, sel_spec);




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

        popup_territory = new PopupMenu(Self_planner.this, sel_ter);


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
                new DatePickerDialog(Self_planner.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!userlogged1.equalsIgnoreCase("Select User") && !sel_ter.getText().toString().equalsIgnoreCase("Select territory") && !sel_spec.getText().toString().equalsIgnoreCase("Select Speciality") && !pudate.getText().toString().equalsIgnoreCase("") && !inputSearch.getText().toString().equalsIgnoreCase("")) {
                    Firebase planner_ref = ref.child("users").child(userlogged1).child("planner").child(pudate.getText().toString());
                    Map<String, Object> nickname = new HashMap<String, Object>();
                    nickname.put(inputSearch.getText().toString(), sel_spec.getText().toString());
                    planner_ref.updateChildren(nickname);
                    String data=mpse.getText().toString()+separator+inputSearch.getText().toString()+" : "+sel_spec.getText().toString();
                    mpse.setText(data);
                    Toast.makeText(Self_planner.this, "Event added in User Planner list", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(Self_planner.this, "Please Enter valid data before submitting", Toast.LENGTH_LONG).show();
            }
        });






    }

    private void updateLabel() {

        String myFormat = "ddMMyyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        pudate.setText(sdf.format(myCalendar.getTime()));
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

                    adapter = new ArrayAdapter<String>(Self_planner.this,
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_self_planner, menu);
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
