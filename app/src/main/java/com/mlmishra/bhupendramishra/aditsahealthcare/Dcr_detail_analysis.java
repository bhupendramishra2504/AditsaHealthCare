package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mlmishra.bhupendramishra.aditsahealthcare.R;
import android.app.Activity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import android.widget.TextView;

public class Dcr_detail_analysis extends Activity {
    Button user_list,fromdate,todate,submit,save;
    TextView vra,desc;
    String s_user;
    Firebase mFirebaseRef;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog.OnDateSetListener date1;
    static final int DATE_DIALOG_ID_1 = 999;
    static final int DATE_DIALOG_ID_2 = 998;
    String separator = System.getProperty("line.separator");
    int total_calls=0;
    int ftv=0,fu=0,no_of_days=0;
    int total_dcr_submitted=0;
    int no_late_submitted_dcr=0;
    int phy=0,der=0,gyn=0,orth=0,gp=0,dia=0;
    int prog=0,mc=0,mf=0,ihb=0,co=0,be=0,ov=0;
    String submitted_dates="";
    String report_type;
    int num_locations=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dcr_detail_analysis);
        user_list=(Button)findViewById(R.id.auser);
        fromdate=(Button)findViewById(R.id.afdate);
        todate=(Button)findViewById(R.id.atdate);
        submit=(Button)findViewById(R.id.asubmit);
        save=(Button)findViewById(R.id.asave);
        vra=(TextView)findViewById(R.id.avdc);
        desc=(TextView)findViewById(R.id.desc);


        Intent i = getIntent();
        report_type= i.getStringExtra("report");
        desc.setText(report_type);

        mFirebaseRef = new Firebase(Global.FIREBASE_URL);
        myCalendar = Calendar.getInstance();

        user_list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                total_calls=0;
                ftv=0;
                fu=0;
                no_of_days=0;
                total_dcr_submitted=0;
                no_late_submitted_dcr=0;
                phy=0;
                der=0;
                gyn=0;
                orth=0;
                gp=0;
                dia=0;
                prog=0;
                mc=0;
                mf=0;
                ihb=0;
                co=0;
                be=0;
                ov=0;
                vra.setText("");
                //Creating the instance of PopupMenu
                Firebase u_ref;
              //  Log.d("user type", Global.user_type);
                //Toast.makeText(Admin_view_report.this,"user type"+Global.user_type,Toast.LENGTH_LONG).show();
                    u_ref = mFirebaseRef.child("userlist");
                    u_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        PopupMenu popup = new PopupMenu(Dcr_detail_analysis.this, user_list);

                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            for (DataSnapshot child : snapshot.getChildren()) {
                                if (child.getKey() != null) {
                                    popup.getMenu().add(child.getKey().toString());
                                   // Log.d("item", "");
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
                total_calls=0;
                ftv=0;
                fu=0;
                no_of_days=0;
                total_dcr_submitted=0;
                no_late_submitted_dcr=0;
                phy=0;
                der=0;
                gyn=0;
                orth=0;
                gp=0;
                dia=0;
                prog=0;
                mc=0;
                mf=0;
                ihb=0;
                co=0;
                be=0;
                ov=0;
                vra.setText("");
                // TODO Auto-generated method stub
                new DatePickerDialog(Dcr_detail_analysis.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        todate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                total_calls=0;
                ftv=0;
                fu=0;
                no_of_days=0;
                total_dcr_submitted=0;
                no_late_submitted_dcr=0;
                phy=0;
                der=0;
                gyn=0;
                orth=0;
                gp=0;
                dia=0;
                prog=0;
                mc=0;
                mf=0;
                ihb=0;
                co=0;
                be=0;
                ov=0;
                vra.setText("");
                // TODO Auto-generated method stub
                new DatePickerDialog(Dcr_detail_analysis.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });





        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vra.setText("wait while loading the data from the Server ....");
                //numbers of days in selected date range
                no_of_days=noofdays(fromdate.getText().toString(),todate.getText().toString());

            if(validftdate())
            {
                if(report_type.equalsIgnoreCase("DCR DETAILED ANALYSIS"))
                    detailed_dcr_report();
                else if(report_type.equalsIgnoreCase("USER TRACKING ANALYSIS"))
                    tracking_detailed_report();




            }
                //mAuthProgressDialog.dismiss();
                //submit.setEnabled(true);


        }

        });

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                Global.write_file("Admin_" + s_user+"_"+desc.getText(), vra.getText().toString(), Dcr_detail_analysis.this);

            }

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dcr_detail_analysis, menu);
        return true;
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

    private void detailed_dcr_report()
    {
        Toast.makeText(Dcr_detail_analysis.this,"Date Range are valid", Toast.LENGTH_LONG).show();
        Firebase viewtourref = mFirebaseRef.child("users").child(s_user).child("DAILY CALL REPORT");

        viewtourref.addValueEventListener(new ValueEventListener() {
            String data, analysis_data = "DCR AUTOMATIC ANALYSIS REPORT" + separator + separator + "User Selected : " + s_user + separator + "Date Selected : " + fromdate.getText().toString() + " to " + todate.getText().toString();


            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    int i = 10;

                    if (child.getValue().toString().split(separator).length >= 3) {
                        //total number of calls
                        if (child != null && date_validation2(child.getValue().toString().split(separator)[3].split(":")[1].trim())) {
                            total_dcr_submitted++;
                            data = child.getKey().toString() + separator + child.getValue().toString() + separator + separator + data;
                            if (child.getValue().toString().split(separator).length > 7) {
                                if (child.getValue().toString().split(separator)[7].split(":")[1].trim().matches(".*\\d.*")) {
                                    total_calls = total_calls + Integer.parseInt(child.getValue().toString().split(separator)[7].split(":")[1].trim());
                                    if (child.getValue().toString().split(separator)[3].split(":").length >= 1)
                                        submitted_dates = child.getValue().toString().split(separator)[3].split(":")[1] + ", " + submitted_dates;

                                }
                            }

                            //number of calls for fisrt time and follow up
                            String[] dcr = child.getValue().toString().split(separator);
                            String date_dcr = child.getKey().toString().substring(0, 11);
                            while (i < dcr.length) {
                                if (dcr[i].split(":").length == 2) {
                                    if (dcr[i].split(":")[1].trim().equalsIgnoreCase("FIRST TIME VISIT")) {
                                        ftv++;
                                    } else if (dcr[i].split(":")[1].trim().equalsIgnoreCase("FOLLOW UP")) {
                                        fu++;
                                    }
                                }
                                i = i + 13;
                            }

                            //speciality covers of Docter's
                            int spec_index = 13;
                            while (spec_index < dcr.length) {
                                if (dcr[spec_index].split(":").length == 2) {
                                    if (dcr[spec_index].split(":")[1].trim().equalsIgnoreCase("PHYSICIAN")) {
                                        phy++;

                                    } else if (dcr[spec_index].split(":")[1].trim().equalsIgnoreCase("DERMATOLOGIST")) {
                                        der++;
                                    } else if (dcr[spec_index].split(":")[1].trim().equalsIgnoreCase("GYNECOLOGIST")) {
                                        gyn++;
                                    } else if (dcr[spec_index].split(":")[1].trim().equalsIgnoreCase("ORTHOPEDIC")) {
                                        orth++;
                                    } else if (dcr[spec_index].split(":")[1].trim().equalsIgnoreCase("GENERAL PRACTITIONER")) {
                                        gp++;
                                    } else if (dcr[spec_index].split(":")[1].trim().equalsIgnoreCase("DIABETOLOGIST")) {
                                        dia++;
                                    }


                                }
                                spec_index = spec_index + 13;
                            }

                            //free samples count
                            int sample_index = 14;
                            while (sample_index < dcr.length && sample_index + 7 < dcr.length) {
                                if (dcr[sample_index].split(":").length == 2) {
                                    if (dcr[sample_index].split(":")[1].trim().matches(".*\\d.*")) {
                                        prog++;
                                    }
                                }
                                if (dcr[sample_index + 1].split(":").length == 2) {
                                    if (dcr[sample_index + 1].split(":")[1].trim().matches(".*\\d.*")) {
                                        mc++;
                                    }
                                }
                                if (dcr[sample_index + 2].split(":").length == 2) {
                                    if (dcr[sample_index + 2].split(":")[1].trim().matches(".*\\d.*")) {
                                        mf++;
                                    }
                                }
                                if (dcr[sample_index + 3].split(":").length == 2) {
                                    if (dcr[sample_index + 3].split(":")[1].trim().matches(".*\\d.*")) {
                                        ihb++;
                                    }
                                }
                                if (dcr[sample_index + 4].split(":").length == 2) {
                                    if (dcr[sample_index + 4].split(":")[1].trim().matches(".*\\d.*")) {
                                        co++;
                                    }
                                }
                                if (dcr[sample_index + 5].split(":").length == 2) {
                                    if (dcr[sample_index + 5].split(":")[1].trim().matches(".*\\d.*")) {
                                        be++;
                                    }
                                }
                                if (dcr[sample_index + 6].split(":").length == 2) {
                                    if (dcr[sample_index + 6].split(":")[1].trim().matches(".*\\d.*")) {
                                        ov++;
                                    }
                                }


                                sample_index = sample_index + 13;
                            }


                            //late submitted dcr
                            if (dcr.length > 3) {
                                if (!dcr[3].equalsIgnoreCase(date_dcr)) {
                                    no_late_submitted_dcr++;
                                }
                            }


                        } //else
                        //Toast.makeText(Dcr_detail_analysis.this, "No report found", Toast.LENGTH_LONG).show();
                    }
                }

                if (data != "") {
                    float a_calls = (float) total_calls / (float) no_of_days;
                    analysis_data = analysis_data + separator + separator + "1. Estimated Number of Working Days : " + String.valueOf(no_of_days) + separator + separator + "2. Total Number of DCR Submitted : " + String.valueOf(total_dcr_submitted) + separator + separator + "3. Dates for which DCR Submitted : " + submitted_dates + separator + separator + "4. Late Submitted DCR : " + String.valueOf(no_late_submitted_dcr) + separator + separator + "5. Total Number of call in given period : " + String.valueOf(total_calls) + separator + separator + "6. Number of Call for First Time Visit : " + String.valueOf(ftv) + separator + separator + "7. Number of Calls for follow up : " + String.valueOf(fu) + separator + separator + "8. Average Number of Calls are : " + String.valueOf(a_calls) + " calls/day" + separator + separator + "9. Speciality Covered : " + separator + "Physician : " + String.valueOf(phy) + separator + "Dermatologist : " + String.valueOf(der) + separator + "Gynecologist : " + String.valueOf(gyn) + separator + "Orthopedic : " + String.valueOf(orth) + separator + "General Practitioner : " + String.valueOf(gp) + separator + "Diabetologist : " + String.valueOf(dia) + separator + separator + "10. Free Samples Provided in DCR: " + separator + "PROGEMOM SR : " + String.valueOf(prog) + separator + "MOMMI CAL : " + String.valueOf(mc) + separator + "MOMMI FOL : " + String.valueOf(mf) + separator + "IRROW HB : " + String.valueOf(ihb) + separator + "CALITSA ONE : " + String.valueOf(co) + separator + "BIO ELIXSA : " + String.valueOf(be) + "OVITSA : " + String.valueOf(ov) + separator;
                    vra.setText(analysis_data + separator + separator + "DAILY CALL REPORTS from " + fromdate.getText().toString() + " to  " + todate.getText().toString() + separator + data);
                    //submit.setEnabled(true);
                }

                // mAuthProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    private boolean date_validation(String date)
    {
        boolean validated=false;
        boolean valid1=false,valid2=false;
        String[] datebw=date.split("-");
        String[] fdate=fromdate.getText().toString().split("-");
        String[] tdate=todate.getText().toString().split("-");
        int month1,month2,month3=0,day1,day2,year1,year2,day3,year3;
        String month3str;
        month1=Integer.parseInt(fdate[1]);
        month2=Integer.parseInt(tdate[1]);
        month3str=datebw[1];
        day1=Integer.parseInt(fdate[0]);
        day2=Integer.parseInt(tdate[0]);
        day3=Integer.parseInt(datebw[0]);
        year1=Integer.parseInt(fdate[2]);
        year2=Integer.parseInt(tdate[2]);
        year3=Integer.parseInt(datebw[2]);

        if(validftdate())
        {
            if(month3str.equalsIgnoreCase("jan"))
                month3=1;
            else if(month3str.equalsIgnoreCase("feb"))
                month3=2;
            else if(month3str.equalsIgnoreCase("mar"))
                month3=3;
            else if(month3str.equalsIgnoreCase("apr"))
                month3=4;
            else if(month3str.equalsIgnoreCase("may"))
                month3=5;
            else if(month3str.equalsIgnoreCase("jun"))
                month3=6;
            else if(month3str.equalsIgnoreCase("jul"))
                month3=7;
            else if(month3str.equalsIgnoreCase("aug"))
                month3=8;
            else if(month3str.equalsIgnoreCase("sep"))
                month3=9;
            else if(month3str.equalsIgnoreCase("oct"))
                month3=10;
            else if(month3str.equalsIgnoreCase("nov"))
                month3=11;
            else if(month3str.equalsIgnoreCase("dec"))
                month3=12;
        }
        if(month3!=0) {
            valid1 = validdate(fromdate.getText().toString(), String.valueOf(day3) + "-" + String.valueOf(month3) + "-" + String.valueOf(year3));
            valid2 = validdate(String.valueOf(day3) + "-" + String.valueOf(month3) + "-" + String.valueOf(year3),todate.getText().toString());

        }
        if(valid1 && valid2)
        {
            validated=true;
        }
        return validated;
    }

    private boolean date_validation2(String date)
    {
        boolean validated=false;
        boolean valid1=false,valid2=false;
        String[] datebw=date.split("/");
        String[] fdate=fromdate.getText().toString().split("-");
        String[] tdate=todate.getText().toString().split("-");
        int month1,month2,month3=0,day1,day2,year1,year2,day3,year3;
        String month3str;
        if(datebw.length==3) {
            month1 = Integer.parseInt(fdate[1]);
            month2 = Integer.parseInt(tdate[1]);
            month3 = Integer.parseInt(datebw[1]);
            day1 = Integer.parseInt(fdate[0]);
            day2 = Integer.parseInt(tdate[0]);
            day3 = Integer.parseInt(datebw[0]);
            year1 = Integer.parseInt(fdate[2]);
            year2 = Integer.parseInt(tdate[2]);
            year3 = Integer.parseInt("20"+datebw[2]);


            if (month3 != 0) {
                valid1 = validdate(fromdate.getText().toString(), String.valueOf(day3) + "-" + String.valueOf(month3) + "-" + String.valueOf(year3));
                valid2 = validdate(String.valueOf(day3) + "-" + String.valueOf(month3) + "-" + String.valueOf(year3), todate.getText().toString());

            }
            if (valid1 && valid2) {
                validated = true;
            }
            return validated;
        }
        else
            return false;
    }

    private boolean validftdate()
    {
        boolean valid=false;
        String[] fdate=fromdate.getText().toString().split("-");
        String[] tdate=todate.getText().toString().split("-");
        int monthint1,monthint2,day1,day2,year1,year2;
        monthint1=Integer.parseInt(fdate[1]);
        monthint2=Integer.parseInt(tdate[1]);
        day1=Integer.parseInt(fdate[0]);
        day2=Integer.parseInt(tdate[0]);
        year1=Integer.parseInt(fdate[2]);
        year2=Integer.parseInt(tdate[2]);
        if(year2>year1)
        {
            valid=true;
        }
        else if(year2<year1)
        {
            valid=false;

        }
        else
        {
            if(monthint2>monthint1)
            {
                valid=true;
            }
            else if(monthint2<monthint1)
            {
                valid=false;
            }
            else
            {
                if(day2>day1)
                {
                    valid=true;
                }
                else if (day2<day1)
                {
                    valid=false;
                }
                else
                {
                    valid=true;
                }
            }
        }





        return valid;
    }

    private int noofdays(String date1,String date2)
    {
        int nudays=0;
        String[] fdate=date1.split("-");
        String[] tdate=date2.split("-");
        int monthint1,monthint2,day1,day2,year1,year2;
        monthint1=Integer.parseInt(fdate[1]);
        monthint2=Integer.parseInt(tdate[1]);
        day1=Integer.parseInt(fdate[0]);
        day2=Integer.parseInt(tdate[0]);
        year1=Integer.parseInt(fdate[2]);
        year2=Integer.parseInt(tdate[2]);

          if(year2>year1)
          {
              if(day2>=day1)
              {
                  nudays=365+(monthint2-monthint1)*30+day2-day1+1;
              }
              else if(day2<day1)
              {
                  nudays=365+(monthint2-monthint1-1)*30+30+day2-day1+1;
              }
          }
            else if(year2==year1)
          {
              if(day2>=day1)
              {
                  nudays=(monthint2-monthint1)*30+day2-day1+1;
              }
              else if(day2<day1)
              {
                  nudays=(monthint2-monthint1-1)*30+30+day2-day1+1;
              }
          }
        nudays=nudays-nudays/7;
        return nudays;
    }

    private boolean validdate(String date1,String date2)
    {
        boolean valid=false;
        String[] fdate=date1.split("-");
        String[] tdate=date2.split("-");
        int monthint1,monthint2,day1,day2,year1,year2;
        monthint1=Integer.parseInt(fdate[1]);
        monthint2=Integer.parseInt(tdate[1]);
        day1=Integer.parseInt(fdate[0]);
        day2=Integer.parseInt(tdate[0]);
        year1=Integer.parseInt(fdate[2]);
        year2=Integer.parseInt(tdate[2]);
        if(year2>year1)
        {
            valid=true;
        }
        else if(year2<year1)
        {
            valid=false;

        }
        else
        {
            if(monthint2>monthint1)
            {
                valid=true;
            }
            else if(monthint2<monthint1)
            {
                valid=false;
            }
            else
            {
                if(day2>day1)
                {
                    valid=true;
                }
                else if (day2<day1)
                {
                    valid=false;
                }
                else
                {
                    valid=true;
                }
            }
        }





        return valid;
    }


    public void tracking_detailed_report()
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
                String datatoshow = "User Tracking Data Detailed Analysis for : "+separator+"User : "+s_user+separator+"Period : from "+fromdate.getText().toString()+" to "+todate.getText().toString()+separator;
                String previous_child="none";
                String data="";
                String distancep="";
                String valid_loc="";
                int total_loc=0,invalid_loc=0;
                double lat1=0.0,lat2=0.0,long1=0.0,long2=0.0;
                num_locations=0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child != null && Global.date_validation(child.getKey().toString().substring(0, 11), fromdate.getText().toString(), todate.getText().toString())) {
                       if(child.getKey().toString().substring(0, 11).equalsIgnoreCase(previous_child) ) {
                            data= "Location Number : "+num_locations+separator+"Registered Time : "+child.getKey().toString().substring(12, 23);
                           num_locations++;
                           total_loc++;
                           Log.d("num location in old : ",String.valueOf(num_locations));
                       }else if(!child.getKey().toString().substring(0, 11).equalsIgnoreCase(previous_child) )
                       {
                          if(!previous_child.equalsIgnoreCase("none")) {
                              data = separator + "Number of Locations for " + previous_child + " : " + String.valueOf(num_locations-1) + separator+separator+"--------------------------"+separator + "Showing locations for : " + child.getKey().toString().substring(0, 11) + separator+ "Location Number : 1"+separator+"Registered Time : "+ child.getKey().toString().substring(12, 23);
                              num_locations=2;
                              total_loc++;
                          }
                           else
                          {
                              data = "Showing locations for : " + child.getKey().toString().substring(0, 11) + separator + "Location Number : 1"+separator+"Registered Time : "+child.getKey().toString().substring(12, 23);
                              num_locations=2;
                              total_loc++;
                          }


                           Log.d("num location in new : ",String.valueOf(num_locations));
                       }
                        String[] logdata = child.getValue().toString().split(",");
                        Log.d("date", data);
                        Log.d("long", logdata[0] + logdata[1]);
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(Dcr_detail_analysis.this, Locale.getDefault());

                        try {

                            if (logdata[0] != null && logdata[1] != null) {
                                addresses = geocoder.getFromLocation(Double.valueOf(logdata[0]).doubleValue(), Double.valueOf(logdata[1]).doubleValue(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                lat2 = Double.valueOf(logdata[0]).doubleValue();
                                long2 = Double.valueOf(logdata[1]).doubleValue();
                                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city = addresses.get(0).getLocality();
                                String postalCode = addresses.get(0).getPostalCode();
                                if(num_locations>2)
                                {
                                    distancep = "Shortest Distance between " + "Location no. " + String.valueOf(num_locations - 2) + " to Location No. " + String.valueOf(num_locations - 1) + " is : " + String.valueOf(getShortestDistance(lat1, lat2, long1, long2)) + " kms";
                                    if(getShortestDistance(lat1, lat2, long1, long2)<0.1)
                                    {
                                        valid_loc="Comment : distance is too closed less than 100 meters hence there pair of locations are invalid";
                                        invalid_loc++;
                                    }
                                    else
                                    {
                                        valid_loc="";
                                    }
                                    datatoshow = datatoshow + separator + data + separator + "Longitute : " + logdata[1] + separator + "Latitute : " + logdata[0] + separator + "Address : " + address + separator + "City : " + city + separator + "Pin Code: " + postalCode + separator + distancep + separator+valid_loc+separator;
                                    lat1 = Double.valueOf(logdata[0]).doubleValue();
                                    long1 = Double.valueOf(logdata[1]).doubleValue();
                            }
                                else
                            {
                                datatoshow = datatoshow + separator + data + separator + "Longitute : " + logdata[1] + separator + "Latitute : " + logdata[0] + separator + "Address : " + address + separator + "City : " + city + separator + "Pin Code: " + postalCode + separator;
                                lat1 = Double.valueOf(logdata[0]).doubleValue();
                                long1 = Double.valueOf(logdata[1]).doubleValue();
                            }
                                previous_child = child.getKey().toString().substring(0, 11);

                                //Toast.makeText(Admin_view_report.this, "you reached inside", Toast.LENGTH_LONG).show();

                            }
                        } catch (IOException io) {

                        }


                        // place_marker(Double.valueOf(logdata[1]).doubleValue(), Double.valueOf(logdata[0]).doubleValue(), data);

                    }
                    //datatoshow=datatoshow+separator+"Number of locations for "+previous_child+" : "+String.valueOf(num_locations);
                }
                datatoshow=datatoshow+separator+"Total Location registered by user in given period is : "+String.valueOf(total_loc)+separator+"Total Invalid Location registered by User : "+String.valueOf(invalid_loc);
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

    private double getShortestDistance(double lat1,double lat2,double long1,double long2)
    {
        double distance=0.0;
        double radius=6371.0;
        double delta_lat=degree_rad(lat2-lat1);
        double delta_long=degree_rad(long2-long1);
        double a=Math.sin(delta_lat/2)*Math.sin(delta_lat/2)+Math.cos(degree_rad(lat2))*Math.cos(degree_rad(lat1))*Math.sin(delta_long/2)*Math.sin(delta_long/2);
        double c=2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        distance = radius*c;
        return distance;
    }

    private double degree_rad(double degree)
    {
        return degree*Math.PI/180;
    }
}
