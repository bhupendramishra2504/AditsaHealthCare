package com.mlmishra.bhupendramishra.aditsahealthcare;


import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import android.widget.TextView;
import android.net.Uri;
import android.database.Cursor;

public class Update_Dr_master_List extends Activity {

    Button seldr,speciality,drterritory,drsubmit;
    EditText drname,draddress,drapoint,add_territory;
    TextView filepath;
    boolean add_user,mod_user,add_ter,upload_list;
    Firebase ref;
    PopupMenu popup,popup_dr;
    boolean first_time_popup=false,first_time_popup_dr=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__dr_master__list);

        seldr=(Button)findViewById(R.id.drnamebutton);
        speciality=(Button)findViewById(R.id.drspeciality);
        drterritory=(Button)findViewById(R.id.drterritory);
        drsubmit=(Button)findViewById(R.id.drsubmit);

        drname=(EditText)findViewById(R.id.drname);
        draddress=(EditText)findViewById(R.id.draddress);
        drapoint=(EditText)findViewById(R.id.drapoint);
        add_territory=(EditText)findViewById(R.id.drater);

        ref=new Firebase(Global.FIREBASE_URL);

        filepath=(TextView)findViewById(R.id.udltv);

        seldr.setVisibility(View.GONE);
        add_territory.setVisibility(View.GONE);
        drname.setVisibility(View.VISIBLE);
        draddress.setVisibility(View.VISIBLE);
        drapoint.setVisibility(View.VISIBLE);
        speciality.setVisibility(View.VISIBLE);
        drterritory.setVisibility(View.VISIBLE);

        popup = new PopupMenu(Update_Dr_master_List.this, drterritory);
        popup_dr = new PopupMenu(Update_Dr_master_List.this, seldr);

        drsubmit.setText("Add to Master List");

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int chkid) {
                add_user= ((RadioButton) findViewById(R.id.U11)).isChecked();
                mod_user=((RadioButton) findViewById(R.id.U12)).isChecked();
                add_ter=((RadioButton) findViewById(R.id.U13)).isChecked();
                upload_list=((RadioButton) findViewById(R.id.U14)).isChecked();

                if (add_user) {
                    seldr.setVisibility(View.GONE);
                    add_territory.setVisibility(View.GONE);
                    filepath.setVisibility(View.GONE);
                    drname.setVisibility(View.VISIBLE);
                    draddress.setVisibility(View.VISIBLE);
                    drapoint.setVisibility(View.VISIBLE);
                    speciality.setVisibility(View.VISIBLE);
                    drterritory.setVisibility(View.VISIBLE);

                    drsubmit.setText("Add to Master List");


                }
                else if(mod_user){
                    seldr.setVisibility(View.VISIBLE);
                    drname.setVisibility(View.GONE);
                    filepath.setVisibility(View.GONE);
                    add_territory.setVisibility(View.GONE);
                    draddress.setVisibility(View.VISIBLE);
                    drapoint.setVisibility(View.VISIBLE);
                    speciality.setVisibility(View.VISIBLE);
                    drterritory.setVisibility(View.VISIBLE);
                    drsubmit.setText("Update Master List");
                }

                else if(add_ter){
                    seldr.setVisibility(View.GONE);
                    drname.setVisibility(View.GONE);
                    filepath.setVisibility(View.GONE);
                    draddress.setVisibility(View.GONE);
                    drapoint.setVisibility(View.GONE);
                    speciality.setVisibility(View.GONE);
                    drterritory.setVisibility(View.GONE);
                    add_territory.setVisibility(View.VISIBLE);
                    drsubmit.setText("Add Territory");

                }
                else if(upload_list){
                    seldr.setVisibility(View.GONE);
                    drname.setVisibility(View.GONE);
                    filepath.setVisibility(View.VISIBLE);
                    draddress.setVisibility(View.GONE);
                    drapoint.setVisibility(View.GONE);
                    speciality.setVisibility(View.GONE);
                    drterritory.setVisibility(View.GONE);
                    add_territory.setVisibility(View.GONE);
                    drsubmit.setText("UPDATE");

                }
            }
        });

        drsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (drsubmit.getText().toString().equalsIgnoreCase("Add Territory")) {
                    if (add_territory != null) {
                        Firebase fhq = ref.child("territory_list");
                        Map<String, Object> addhq_data = new HashMap<String, Object>();
                        addhq_data.put(add_territory.getText().toString(), "");
                        fhq.updateChildren(addhq_data);
                        Toast.makeText(Update_Dr_master_List.this, "Territory Added Successfully", Toast.LENGTH_LONG).show();
                        first_time_popup = false;
                        popup = new PopupMenu(Update_Dr_master_List.this, drterritory);
                    } else
                        Toast.makeText(Update_Dr_master_List.this, "Fill Valid Data", Toast.LENGTH_LONG).show();
                } else if (drsubmit.getText().toString().equalsIgnoreCase("ADD TO MASTER LIST")) {

                    if(!(drname==null | drterritory.getText().toString().equalsIgnoreCase("DR TERRITORY")|speciality.getText().toString().equalsIgnoreCase("SELECT DR SPECIALITY"))) {
                        Firebase masterlist = ref.child("drlist").child(drterritory.getText().toString()).child(speciality.getText().toString());
                        Map<String, Object> adddr_data = new HashMap<String, Object>();
                        adddr_data.put(drname.getText().toString(), "");
                        masterlist.updateChildren(adddr_data);
                        Toast.makeText(Update_Dr_master_List.this, "Doctor's Details successfully added in the server", Toast.LENGTH_LONG).show();

                        Firebase drdetails = ref.child("drdetails").child(drname.getText().toString());
                        Firebase dradd = drdetails.child("Address");
                        dradd.setValue(draddress.getText().toString());

                        Firebase drspeciality = drdetails.child("Speciality");
                        drspeciality.setValue(speciality.getText().toString());

                        Firebase dratiming = drdetails.child("Appointment_timings");
                        dratiming.setValue(drapoint.getText().toString());

                        first_time_popup_dr = false;
                        popup_dr = new PopupMenu(Update_Dr_master_List.this, seldr);
                    }
                    else
                        Toast.makeText(Update_Dr_master_List.this,"Check the Data, some critical data is missed, try again",Toast.LENGTH_LONG).show();


                } else if (drsubmit.getText().toString().equalsIgnoreCase("UPDATE MASTER LIST")) {

                    if(!(seldr.getText().toString().equalsIgnoreCase("SELECT DOCTER")|speciality.getText().toString().equalsIgnoreCase("SELECT DR SPECIALITY")|drterritory.getText().toString().equalsIgnoreCase("DR TERRITORY"))) {

                        Firebase drdetails = ref.child("drdetails").child(seldr.getText().toString());
                        Firebase dradd = drdetails.child("Address");
                        dradd.setValue(draddress.getText().toString());

                        Firebase dratiming = drdetails.child("Appointment_timings");
                        dratiming.setValue(drapoint.getText().toString());

                        Toast.makeText(Update_Dr_master_List.this, "Doctor's Details successfully updated in the server", Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(Update_Dr_master_List.this, "Doctor's Details Updation data not valid Check your details again and try", Toast.LENGTH_LONG).show();


                }

                else if (drsubmit.getText().toString().equalsIgnoreCase("UPDATE")) {

                        int updatecount=Global.upload_drlist(ref,Update_Dr_master_List.this);
                        if(updatecount>0)
                        {
                            Toast.makeText(Update_Dr_master_List.this,"Record Updated : "+updatecount,Toast.LENGTH_LONG).show();
                        }
                }


            }
        });


        drterritory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu


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
                                                popup.getMenu().add(child.getKey().toString());
                                                Log.d("item", "");
                                            }
                                        }

                                        first_time_popup_dr = false;
                                        popup_dr = new PopupMenu(Update_Dr_master_List.this, seldr);
                                        popup.getMenuInflater().inflate(R.menu.menu_update__dr_master__list, popup.getMenu());
                                        //popup.setOutsideTouchable(true);
                                        //registering popup with OnMenuItemClickListener
                                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                            public boolean onMenuItemClick(MenuItem item) {
                                                //asm_user = (String) item.getTitle();
                                                drterritory.setText(item.getTitle());
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
                                first_time_popup = true;
                            } else
                                popup.show();

                        } else
                            popup.dismiss();
                    }
                });


            }
        });


        seldr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                if(!(drterritory.getText().toString().equalsIgnoreCase("DR TERRITORY")|speciality.getText().toString().equalsIgnoreCase("SELECT DR SPECIALITY"))) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (!isFinishing()) {
                                if (!first_time_popup_dr) {
                                    Firebase u_ref = ref.child("drlist").child(drterritory.getText().toString()).child(speciality.getText().toString());

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
                                                    seldr.setText(item.getTitle());
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
                    Toast.makeText(Update_Dr_master_List.this,"Select correct territory and Speciality to Proceed",Toast.LENGTH_LONG).show();


            }
        });






        speciality.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu


                PopupMenu popup = new PopupMenu(Update_Dr_master_List.this, speciality);


                for( int i=0;i<Global.speciality.length;i++)
                {
                    popup.getMenu().add(Global.speciality[i]);
                }

                first_time_popup_dr = false;
                popup_dr = new PopupMenu(Update_Dr_master_List.this, seldr);

                popup.getMenuInflater().inflate(R.menu.menu_update__dr_master__list, popup.getMenu());
                //popup.setOutsideTouchable(true);
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //s_report = (String) item.getTitle();
                        speciality.setText(item.getTitle());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 101:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d("TAG", "File Uri: " + uri.toString());
                    // Get the path

                    if (uri.getScheme().toString().compareTo("content")==0)
                    {
                        Cursor cursor =getContentResolver().query(uri, null, null, null, null);
                        if (cursor.moveToFirst())
                        {
                            int column_index = cursor.getColumnIndexOrThrow("");//Instead of "MediaStore.Images.Media.DATA" can be used "_data"
                            Uri filePathUri = Uri.parse(cursor.getString(column_index));
                            String file_name = filePathUri.getLastPathSegment().toString();
                            String file_path=filePathUri.getPath();
                            Toast.makeText(this,"File Name & PATH are:"+file_name+"\n"+file_path, Toast.LENGTH_LONG).show();
                        }
                    }
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update__dr_master__list, menu);
        return true;
    }


    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    101);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
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
}
