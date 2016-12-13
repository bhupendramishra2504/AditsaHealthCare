package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.Firebase;
import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Date;
import android.view.View;
import android.graphics.Bitmap;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by bhupendramishra on 27/10/15.
 */
public class Global {
    public static final String FIREBASE_URL = "https://aditsa-healthcare.firebaseio.com";
    //public static String value;

    public static int user_index=0;
    public static String userlist1;
    public static String user_type,user_type_del_user="";
    public static boolean asm_user_delete=false;
    public static boolean  list_loaded=false;
    //public static ArrayList<String> userlist= new ArrayList<String>();
    private static List<String> items_array;

    public static float track_time;
    public static String[] speciality={"PHYSICIAN","DERMATOLOGIST","GYNECOLOGIST","ORTHOPEDIC","GENERAL PRACTITIONER","DIABETOLOGIST","GENERAL MEDICINE","ONCOLOGY","OPTHALMOLOGIST"};
    public static String[] userlist;
    public static String separator = System.getProperty("line.separator");

    public class User {
        private String report;
        private String date;


        public User() {}
        public User(String date, String report) {
            this.date = date;
            this.report = report;
        }
        public String getreport() {
            return report;
        }
        public String getdate() {
            return date;
        }


    }

    public static String getFirebaseURl()
    {
        return FIREBASE_URL;
    }


    public static boolean isvalid_string(String chk)
    {
        boolean valid=false;
        if(chk!=null )
            valid=true;
        else
            valid=false;

        return valid;
    }

    public static void write2Firebase(Firebase ref,String value)
    {
        ref.setValue(value);
    }

    public static List<String> userlistFirebase(Firebase ref)
    {
        items_array=new ArrayList<String>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.getKey() != null) {


                        items_array.add(child.getKey().toString());

                        Log.d("item", "");
                    }
                    //Toast.makeText(Tracking_enable_self.this,items_array[index],Toast.LENGTH_LONG).show();
                    //user_index=user_index+1;
                }


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // System.out.println("The read failed: ");
            }
        });


        return items_array;
    }

    public static void write_file(String file,String data,Context context)
    {

        Random rand=new Random();
        PrintWriter pw;
        try {

            File f = new File(Environment.getExternalStorageDirectory() + "/AditsaHC/" + file + ".txt");
            File f1=new File(Environment.getExternalStorageDirectory() + "/AditsaHC");
            if(!f1.exists()) {
                f1.mkdir();
            }

            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("ddMMMyyyyhhmma");
            String formattedDate = df.format(c.getTime());
                f = new File(Environment.getExternalStorageDirectory() + "/AditsaHC/" + file+"("+formattedDate+")"+ ".txt");


            pw = new PrintWriter(new FileWriter(f));

            pw.print(data);
            pw.flush();

            Toast.makeText(context,"File Saved Successfully at : "+Environment.getExternalStorageDirectory() + "/AditsaHC/" + file+"("+formattedDate+")"+ ".txt",Toast.LENGTH_LONG).show();
            pw.close();
        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public static void user_log(Firebase ref,String userlogged1,String reason)
    {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        String formattedDate = df.format(c.getTime());
        Firebase tourref = ref.child("users").child(userlogged1).child("LOG");
        Map<String, Object> nickname = new HashMap<String, Object>();
        nickname.put(formattedDate, reason);
        tourref.updateChildren(nickname);
        //Toast.makeText(Daily_call_report.this, "Daily Call Report Submitted Successfully", Toast.LENGTH_LONG).show();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public class MediaScannerWrapper implements
            MediaScannerConnection.MediaScannerConnectionClient {
        private MediaScannerConnection mConnection;
        private String mPath;
        private String mMimeType;

        // filePath - where to scan;
        // mime type of media to scan i.e. "image/jpeg".
        // use "*/*" for any media
        public MediaScannerWrapper(Context ctx, String filePath, String mime){
            mPath = filePath;
            mMimeType = mime;
            mConnection = new MediaScannerConnection(ctx, this);
        }

        // do the scanning
        public void scan() {
            mConnection.connect();
        }

        // start the scan when scanner is ready
        public void onMediaScannerConnected() {
            mConnection.scanFile(mPath, mMimeType);
            Log.w("MediaScannerWrapper", "media file scanned: " + mPath);
        }

        public void onScanCompleted(String path, Uri uri) {
            // when scan is completes, update media file tags
        }
    }


    public static int upload_drlist(Firebase ref,Context context)
    {

        String line;
        String[] splitdata;
        FileInputStream is;
        BufferedReader reader;
        int count=0;
        boolean valid_data=false;
        final File file = new File(Environment.getExternalStorageDirectory()+"/drlist.txt");

        try {
            if (file.exists()) {
                is = new FileInputStream(file);
                reader = new BufferedReader(new InputStreamReader(is));
                line = reader.readLine();
                if (line.equalsIgnoreCase("DRLIST")) {
                    valid_data = true;
                }
                if (valid_data) {
                    while (line != null) {
                        Log.d("StackOverflow", line);
                        line = reader.readLine();
                        if(line!=null) {
                            splitdata = line.split(";");
                            if (splitdata.length==5) {
                                Firebase tourref = ref.child("drlist").child(splitdata[0]).child(splitdata[1]);

                                Firebase tref = ref.child("territory_list");

                                Map<String, Object> ter_list = new HashMap<String, Object>();
                                ter_list.put(splitdata[0], "");
                                tref.updateChildren(ter_list);

                                Map<String, Object> nickname = new HashMap<String, Object>();
                                nickname.put(splitdata[2], "");
                                tourref.updateChildren(nickname);

                                Firebase drdetails = ref.child("drdetails").child(splitdata[2]);

                                Map<String, Object> ad = new HashMap<String, Object>();
                                ad.put("Address", splitdata[3]);
                                drdetails.updateChildren(ad);

                                Map<String, Object> ap = new HashMap<String, Object>();
                                ap.put("Appointment_timings", splitdata[4]);
                                drdetails.updateChildren(ap);

                                Map<String, Object> sp = new HashMap<String, Object>();
                                sp.put("Speciality", splitdata[1]);
                                drdetails.updateChildren(sp);
                                count++;
                            }
                        }

                    }
                }
                else
                    Toast.makeText(context,"File not a valid update file",Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(context,"File not found",Toast.LENGTH_LONG).show();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }


        return count;
        //Toast.makeText(Daily_call_report.this, "Daily Call Report Submitted Successfully", Toast.LENGTH_LONG).show();
    }

    public static int upload_planner(Firebase ref,Context context)
    {

        String line;
        String[] splitdata;
        FileInputStream is;
        BufferedReader reader;
        int count=0;
        boolean valid_data=false;
        final File file = new File(Environment.getExternalStorageDirectory()+"/planner.txt");

        try {
            if (file.exists()) {
                is = new FileInputStream(file);
                reader = new BufferedReader(new InputStreamReader(is));
                line = reader.readLine();


                while (line != null) {
                    Log.d("StackOverflow", line);
                    line = reader.readLine();
                    if (line != null) {
                        splitdata = line.split(";");
                        if (splitdata.length == 3) {
                            Firebase tourref = ref.child("users").child(splitdata[0]).child("planner");

                            Firebase tref = tourref.child(splitdata[1]);
                            String[] drdata=splitdata[2].split(",");

                            Map<String, Object> ter_list = new HashMap<String, Object>();
                            ter_list.put(drdata[0],drdata[1]);
                            tref.updateChildren(ter_list);


                        }
                    }

                }
            }



            else
                Toast.makeText(context,"File not found",Toast.LENGTH_LONG).show();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }


        return count;
        //Toast.makeText(Daily_call_report.this, "Daily Call Report Submitted Successfully", Toast.LENGTH_LONG).show();
    }

    public static boolean date_validation(String date,String fromdate,String todate)
    {
        boolean validated=false;
        boolean valid1=false,valid2=false;
        String[] datebw=date.split("-");
        String[] fdate=fromdate.split("-");
        String[] tdate=todate.split("-");
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

        if(validftdate(fromdate,todate))
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
            valid1 = validdate(fromdate, String.valueOf(day3) + "-" + String.valueOf(month3) + "-" + String.valueOf(year3));
            valid2 = validdate(String.valueOf(day3) + "-" + String.valueOf(month3) + "-" + String.valueOf(year3),todate);

        }
        if(valid1 && valid2)
        {
            validated=true;
        }
        return validated;
    }

    public static boolean date_validation2(String date,String fromdate,String todate)
    {
        boolean validated=false;
        boolean valid1=false,valid2=false;
        String[] datebw=date.split("/");
        String[] fdate=fromdate.split("-");
        String[] tdate=todate.split("-");
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
            year3 = Integer.parseInt("20" + datebw[2]);


            if (month3 != 0) {
                valid1 = validdate(fromdate, String.valueOf(day3) + "-" + String.valueOf(month3) + "-" + String.valueOf(year3));
                valid2 = validdate(String.valueOf(day3) + "-" + String.valueOf(month3) + "-" + String.valueOf(year3), todate);

            }
            if (valid1 && valid2) {
                validated = true;
            }


            return validated;
        }
        else
            return false;
    }

    public static boolean date_validation1(String date,String fromdate,String todate)
    {
        boolean validated=false;
        boolean valid1=false,valid2=false;
        //String[] datebw=date.split("-");
        String[] fdate=fromdate.split("-");
        String[] tdate=todate.split("-");
        int month3=0,day3=0,year3=0;

        month3=Integer.parseInt(date.substring(2,4));

        day3=Integer.parseInt(date.substring(0,2));

        year3=Integer.parseInt(date.substring(4,8));

        Log.d("day",String.valueOf(day3));
        Log.d("month",String.valueOf(month3));
        Log.d("year",String.valueOf(year3));


        if(validftdate(fromdate,todate))
        {


            valid1 = validdate(fromdate, String.valueOf(day3) + "-" + String.valueOf(month3) + "-" + String.valueOf(year3));
            valid2 = validdate(String.valueOf(day3) + "-" + String.valueOf(month3) + "-" + String.valueOf(year3),todate);

        }
        if(valid1 && valid2)
        {
            validated=true;
        }
        return validated;
    }


    public static boolean validftdate(String fromdate,String todate)
    {
        boolean valid=false;
        String[] fdate=fromdate.split("-");
        String[] tdate=todate.split("-");
        if(fdate.length>2 && tdate.length>2) {
            int monthint1, monthint2, day1, day2, year1, year2;
            monthint1 = Integer.parseInt(fdate[1]);
            monthint2 = Integer.parseInt(tdate[1]);
            day1 = Integer.parseInt(fdate[0]);
            day2 = Integer.parseInt(tdate[0]);
            year1 = Integer.parseInt(fdate[2]);
            year2 = Integer.parseInt(tdate[2]);
            if (year2 > year1) {
                valid = true;
            } else if (year2 < year1) {
                valid = false;

            } else {
                if (monthint2 > monthint1) {
                    valid = true;
                } else if (monthint2 < monthint1) {
                    valid = false;
                } else {
                    if (day2 > day1) {
                        valid = true;
                    } else if (day2 < day1) {
                        valid = false;
                    } else {
                        valid = true;
                    }
                }
            }
        }




        return valid;
    }

    public static boolean validdate(String date1,String date2)
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

    public static void load_user_list()
    {
        Firebase u_ref;

//        Log.d("user type", Global.user_type);
        list_loaded=false;
        userlist=new String[40];
        //Toast.makeText(Admin_view_report.this,"user type"+Global.user_type,Toast.LENGTH_LONG).show();
            Firebase mFirebaseRef=new Firebase(Global.FIREBASE_URL);
            u_ref = mFirebaseRef.child("userlist");
            u_ref.addListenerForSingleValueEvent(new ValueEventListener()
            {

                @Override
                public void onDataChange(DataSnapshot snapshot) {
                        int i=0;
                    for (DataSnapshot child : snapshot.getChildren()) {
                        if (child.getKey() != null) {
                           // popup.getMenu().add(child.getKey().toString());
                            Log.d("item", "");
                            userlist[i]=child.getKey().toString();
                            i++;
                        }
                    }
                    list_loaded=true;


                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    // System.out.println("The read failed: ");
                }


            });


    }

}
