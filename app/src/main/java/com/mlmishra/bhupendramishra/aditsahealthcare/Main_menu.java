package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import android.util.Log;
import android.provider.MediaStore;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import android.provider.MediaStore.Video.Media;
import android.content.ContentValues;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

public class Main_menu extends Activity {
    String userlogged;
    TextView userinfo;
    Button pres,report,track,adduser,deluser,chgpwd,splrole,mmsettings,send_mylocation,vtourreport,mmbackup,preslink,setting_admin,update_dr_master_list,camera,send_doc,update,sendmail,planner_user,admin_planner,planner_googlemap,dcr_da,notification,notification_user,self_planner;
    Firebase ref;
    Uri mUri;
    String separator = System.getProperty("line.separator");
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        //getSupportActionBar().hide();
        Intent i = getIntent();
        userlogged= i.getStringExtra("user");
        userinfo=(TextView)findViewById(R.id.mmtv0);
        userinfo.setText("Logged in as : "+userlogged);

        pres=(Button)findViewById(R.id.mmpres);
        report=(Button)findViewById(R.id.mmreport);
        track=(Button)findViewById(R.id.mmtrack);
        adduser=(Button)findViewById(R.id.mmadduser);
        deluser=(Button)findViewById(R.id.mmdeluser);
        chgpwd=(Button)findViewById(R.id.mmchgpwd);
        mmsettings=(Button)findViewById(R.id.mmsettings);
        send_mylocation=(Button)findViewById(R.id.mmsml);
        vtourreport=(Button)findViewById(R.id.vtourreport);
        mmbackup=(Button)findViewById(R.id.mmbackup);
        preslink=(Button)findViewById(R.id.mmpreslink);
        setting_admin=(Button)findViewById(R.id.mmsetting);
        update_dr_master_list=(Button)findViewById(R.id.mmuml);
        camera=(Button)findViewById(R.id.camera);
        send_doc=(Button)findViewById(R.id.senddoc);
        sendmail=(Button)findViewById(R.id.sendmail);
        update=(Button)findViewById(R.id.update);
        planner_user=(Button)findViewById(R.id.mmplanner);
        admin_planner=(Button)findViewById(R.id.mmadminplanner);
        planner_googlemap=(Button)findViewById(R.id.mmpgm);
        dcr_da=(Button)findViewById(R.id.vdcrdetailreport);
        notification=(Button)findViewById(R.id.mmnotification);
        notification_user=(Button)findViewById(R.id.mmnotification_user);
        self_planner=(Button)findViewById(R.id.mmselfplanner);




        track.setVisibility(View.GONE);
        adduser.setVisibility(View.GONE);
        deluser.setVisibility(View.GONE);
        chgpwd.setVisibility(View.GONE);
        mmbackup.setVisibility(View.GONE);
        mmsettings.setVisibility(View.GONE);
        vtourreport.setVisibility(View.GONE);
        preslink.setVisibility(View.VISIBLE);
        report.setVisibility(View.GONE);
        send_mylocation.setVisibility(View.GONE);
        setting_admin.setVisibility(View.GONE);
        admin_planner.setVisibility(View.GONE);
        planner_googlemap.setVisibility(View.GONE);
        dcr_da.setVisibility(View.GONE);

        update_dr_master_list.setVisibility(View.GONE);
        pres.setVisibility(View.GONE);
        camera.setVisibility(View.VISIBLE);
        send_doc.setVisibility(View.VISIBLE);
        self_planner.setVisibility(View.VISIBLE);
        unread_notifications();

        if(!userlogged.equals("offline")) {
            ref = new Firebase(Global.FIREBASE_URL);

            Firebase usertype = ref.child(userlogged).child("type");
            usertype.addValueEventListener(new ValueEventListener() {
                String type;

                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    if (snapshot.getValue() != null) {

                        type = snapshot.getValue().toString();
                        //Toast.makeText(Main_menu.this, type, Toast.LENGTH_LONG).show();
                        Log.d("User type", type);


                        if (type.equals("director")) {
                            track.setVisibility(View.VISIBLE);
                            adduser.setVisibility(View.VISIBLE);
                            deluser.setVisibility(View.VISIBLE);
                            chgpwd.setVisibility(View.VISIBLE);
                            mmsettings.setVisibility(View.VISIBLE);
                            mmbackup.setVisibility(View.VISIBLE);
                            vtourreport.setVisibility(View.VISIBLE);
                            preslink.setVisibility(View.VISIBLE);
                            report.setVisibility(View.GONE);
                            send_mylocation.setVisibility(View.GONE);
                            setting_admin.setVisibility(View.VISIBLE);
                            update_dr_master_list.setVisibility(View.VISIBLE);
                            admin_planner.setVisibility(View.VISIBLE);
                            planner_googlemap.setVisibility(View.VISIBLE);
                            dcr_da.setVisibility(View.VISIBLE);
                            planner_user.setVisibility(View.GONE);
                            notification.setVisibility(View.VISIBLE);
                            notification_user.setVisibility(View.GONE);
                            camera.setVisibility(View.GONE);
                            send_doc.setVisibility(View.GONE);
                            sendmail.setVisibility(View.GONE);


                        } else if (type.equals("admin")) {
                            track.setVisibility(View.VISIBLE);
                            adduser.setVisibility(View.GONE);
                            deluser.setVisibility(View.GONE);
                            chgpwd.setVisibility(View.GONE);
                            mmbackup.setVisibility(View.GONE);
                            mmsettings.setVisibility(View.GONE);
                            vtourreport.setVisibility(View.VISIBLE);
                            preslink.setVisibility(View.VISIBLE);
                            pres.setVisibility(View.VISIBLE);
                            report.setVisibility(View.VISIBLE);
                            send_mylocation.setVisibility(View.VISIBLE);
                            setting_admin.setVisibility(View.GONE);
                            update_dr_master_list.setVisibility(View.GONE);
                            admin_planner.setVisibility(View.VISIBLE);
                            planner_googlemap.setVisibility(View.VISIBLE);
                            dcr_da.setVisibility(View.GONE);
                            planner_user.setVisibility(View.VISIBLE);
                            notification.setVisibility(View.GONE);
                            notification_user.setVisibility(View.VISIBLE);

                        } else if (type.equals("se")) {
                            track.setVisibility(View.GONE);
                            adduser.setVisibility(View.GONE);
                            deluser.setVisibility(View.GONE);
                            chgpwd.setVisibility(View.GONE);
                            mmbackup.setVisibility(View.GONE);
                            mmsettings.setVisibility(View.GONE);
                            vtourreport.setVisibility(View.GONE);
                            preslink.setVisibility(View.GONE);
                            pres.setVisibility(View.VISIBLE);
                            report.setVisibility(View.VISIBLE);
                            send_mylocation.setVisibility(View.VISIBLE);
                            setting_admin.setVisibility(View.GONE);
                            update_dr_master_list.setVisibility(View.GONE);
                            admin_planner.setVisibility(View.GONE);
                            planner_googlemap.setVisibility(View.GONE);
                            dcr_da.setVisibility(View.GONE);
                            planner_user.setVisibility(View.VISIBLE);
                            notification.setVisibility(View.GONE);
                            notification_user.setVisibility(View.VISIBLE);

                        } else if (type.equals("rsm")) {
                            track.setVisibility(View.VISIBLE);
                            adduser.setVisibility(View.GONE);
                            deluser.setVisibility(View.GONE);
                            chgpwd.setVisibility(View.GONE);
                            mmbackup.setVisibility(View.GONE);
                            mmsettings.setVisibility(View.GONE);
                            vtourreport.setVisibility(View.VISIBLE);
                            preslink.setVisibility(View.GONE);
                            pres.setVisibility(View.VISIBLE);
                            report.setVisibility(View.VISIBLE);
                            send_mylocation.setVisibility(View.VISIBLE);
                            setting_admin.setVisibility(View.GONE);
                            update_dr_master_list.setVisibility(View.VISIBLE);
                            admin_planner.setVisibility(View.VISIBLE);
                            planner_googlemap.setVisibility(View.VISIBLE);
                            dcr_da.setVisibility(View.GONE);
                            planner_user.setVisibility(View.VISIBLE);
                            notification.setVisibility(View.GONE);
                            notification_user.setVisibility(View.VISIBLE);

                        } else if (type.equals("asm")) {
                            track.setVisibility(View.VISIBLE);
                            adduser.setVisibility(View.GONE);
                            deluser.setVisibility(View.GONE);
                            chgpwd.setVisibility(View.GONE);
                            mmbackup.setVisibility(View.GONE);
                            mmsettings.setVisibility(View.GONE);
                            vtourreport.setVisibility(View.VISIBLE);
                            preslink.setVisibility(View.GONE);
                            pres.setVisibility(View.VISIBLE);
                            report.setVisibility(View.VISIBLE);
                            send_mylocation.setVisibility(View.VISIBLE);
                            setting_admin.setVisibility(View.GONE);
                            update_dr_master_list.setVisibility(View.GONE);
                            admin_planner.setVisibility(View.GONE);
                            planner_googlemap.setVisibility(View.VISIBLE);
                            dcr_da.setVisibility(View.GONE);
                            planner_user.setVisibility(View.VISIBLE);
                            notification.setVisibility(View.GONE);
                            notification_user.setVisibility(View.VISIBLE);

                        } else if (userlogged.equals("offline")) {
                            track.setVisibility(View.GONE);
                            adduser.setVisibility(View.GONE);
                            deluser.setVisibility(View.GONE);
                            chgpwd.setVisibility(View.GONE);
                            mmsettings.setVisibility(View.GONE);
                            report.setVisibility(View.GONE);
                            send_mylocation.setVisibility(View.GONE);
                            vtourreport.setVisibility(View.GONE);
                            mmbackup.setVisibility(View.GONE);
                            pres.setVisibility(View.VISIBLE);
                            preslink.setVisibility(View.GONE);
                            setting_admin.setVisibility(View.GONE);
                            update_dr_master_list.setVisibility(View.GONE);
                            admin_planner.setVisibility(View.GONE);
                            send_doc.setVisibility(View.GONE);
                            update.setVisibility(View.GONE);
                            sendmail.setVisibility(View.GONE);
                            planner_googlemap.setVisibility(View.GONE);
                            dcr_da.setVisibility(View.GONE);
                            planner_user.setVisibility(View.GONE);
                            notification.setVisibility(View.GONE);
                            notification_user.setVisibility(View.GONE);
                        }


                    } else {
                        Toast.makeText(Main_menu.this, "Database corrupted or tempered", Toast.LENGTH_LONG).show();
                        track.setVisibility(View.GONE);
                        adduser.setVisibility(View.GONE);
                        deluser.setVisibility(View.GONE);
                        chgpwd.setVisibility(View.GONE);
                        mmsettings.setVisibility(View.GONE);
                        report.setVisibility(View.GONE);
                        send_mylocation.setVisibility(View.GONE);
                        vtourreport.setVisibility(View.GONE);
                        mmbackup.setVisibility(View.GONE);
                        pres.setVisibility(View.VISIBLE);
                        preslink.setVisibility(View.GONE);
                        setting_admin.setVisibility(View.GONE);
                        update_dr_master_list.setVisibility(View.GONE);
                        admin_planner.setVisibility(View.GONE);
                        planner_googlemap.setVisibility(View.GONE);
                        dcr_da.setVisibility(View.GONE);
                        notification.setVisibility(View.GONE);
                        notification_user.setVisibility(View.GONE);
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
            track.setVisibility(View.GONE);
            adduser.setVisibility(View.GONE);
            deluser.setVisibility(View.GONE);
            chgpwd.setVisibility(View.GONE);
            mmsettings.setVisibility(View.GONE);
            report.setVisibility(View.GONE);
            send_mylocation.setVisibility(View.GONE);
            vtourreport.setVisibility(View.GONE);
            mmbackup.setVisibility(View.GONE);
            pres.setVisibility(View.VISIBLE);
            preslink.setVisibility(View.GONE);
            update.setVisibility(View.GONE);
            dcr_da.setVisibility(View.GONE);
            notification.setVisibility(View.GONE);
        }




        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_menu.this, Add_user.class);
                startActivity(intent);
               // finish();

            }
        });

        deluser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_menu.this, Delete_user.class);
                startActivity(intent);
               // finish();

            }
        });

        chgpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_menu.this, Change_password.class);
                startActivity(intent);
              //  finish();

            }
        });

        pres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_menu.this, Presentation_activity.class);
               startActivity(intent);
               // finish();


            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_menu.this, Reporting_activity.class);
                intent.putExtra("user", userlogged);
                startActivity(intent);
               // finish();

            }
        });

        mmbackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_menu.this, Backup_activity.class);
                intent.putExtra("user", userlogged);
                startActivity(intent);
               // finish();

            }
        });

        preslink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_menu.this, Presentation_link.class);
                intent.putExtra("user", userlogged);
                startActivity(intent);
               // finish();

            }
        });

        setting_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_menu.this, Settings_admin.class);
                intent.putExtra("user", userlogged);
                startActivity(intent);
                // finish();

            }
        });


        planner_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_menu.this, Planner_user.class);
                intent.putExtra("user", userlogged);
                startActivity(intent);
                // finish();

            }
        });

        planner_googlemap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_menu.this, Planner_googlemap.class);
                intent.putExtra("user", userlogged);
                startActivity(intent);
                // finish();

            }
        });


        mmsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Main_menu.this)
                        .setTitle("Delete Data")
                        .setMessage("Are you sure you want to delete the data")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Firebase delref = ref.child("users");
                                delref.removeValue();
                                // continue with delete
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });


        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AlertDialog.Builder builder = new AlertDialog.Builder(Main_menu.this);
                //builder.setTitle("ENTER USERNAME TO TRACK");

// Set up the input
               //final EditText input = new EditText(Main_menu.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
             // input.setInputType(InputType.TYPE_CLASS_TEXT);
            //  builder.setView(input);

// Set up the buttons
            //   builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
             //      @Override
             //    public void onClick(DialogInterface dialog, int which) {
                //String user_track = input.getText().toString();

                        Intent intent = new Intent(Main_menu.this, Tracking_enable_self.class);
                        intent.putExtra("user", userlogged);
                        startActivity(intent);
                        //finish();

                //   }
              // });
             // builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
           //        @Override
           //       public void onClick(DialogInterface dialog, int which) {
           //            dialog.cancel();
           //        }
           //    });

           //    builder.show();




            }
        });

        send_mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_menu.this, Track_Start_User.class);
                intent.putExtra("user", userlogged);
                startActivity(intent);


            }
        });

        vtourreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_menu.this, Admin_view_report.class);
                intent.putExtra("user", userlogged);
                startActivity(intent);


            }
        });


        admin_planner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_menu.this, Manage_planner.class);
                intent.putExtra("user", userlogged);
                startActivity(intent);


            }
        });

        self_planner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_menu.this, Self_planner.class);
                intent.putExtra("user", userlogged);
                startActivity(intent);


            }
        });

        update_dr_master_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_menu.this, Update_Dr_master_List.class);
                intent.putExtra("user", userlogged);
                startActivity(intent);


            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_menu.this, Manage_notification.class);
                intent.putExtra("user", userlogged);
                startActivity(intent);


            }
        });


        dcr_da.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu


                PopupMenu popup = new PopupMenu(Main_menu.this, dcr_da);


                popup.getMenu().add("DCR DETAILED ANALYSIS");
                popup.getMenu().add("USER TRACKING ANALYSIS");
                popup.getMenu().add("USER PLANNER ANALYSIS");


                popup.getMenuInflater().inflate(R.menu.menu_daily_call_report, popup.getMenu());
                //popup.setOutsideTouchable(true);
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //s_report = (String) item.getTitle();
                        dcr_da.setText(item.getTitle());
                        if (dcr_da.getText().toString().equalsIgnoreCase("DCR DETAILED ANALYSIS")) {
                            Intent intent = new Intent(Main_menu.this, Dcr_detail_analysis.class);
                            intent.putExtra("report", "DCR DETAILED ANALYSIS");
                            startActivity(intent);
                        } else if (dcr_da.getText().toString().equalsIgnoreCase("USER TRACKING ANALYSIS")) {
                            Intent intent = new Intent(Main_menu.this, Dcr_detail_analysis.class);
                            intent.putExtra("report", "USER TRACKING ANALYSIS");
                            startActivity(intent);
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

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri imageFileUri = getContentResolver().insert(
                        Media.EXTERNAL_CONTENT_URI, new ContentValues());
                mUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                        "pic"+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
                //intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mUri);
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mUri);
                //intent.putExtra("user", userlogged);
                startActivityForResult(intent, 1);

            }
        });

        send_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, 100);

            }
        });

        notification_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_menu.this, Notifications_user.class);
                intent.putExtra("user", userlogged);
                startActivity(intent);

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                //Try Google play
                intent.setData(Uri.parse("market://details?id=com.aditsahealthcare.sales"));
                startActivity(intent);
               // https://play.google.com/store/apps/details?id=com.aditsahealthcare.sales&hl=en


            }
        });

        sendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                //emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"bhupendramishr@gmail.com"});
                //emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject here");
                //emailIntent.putExtra(Intent.EXTRA_TEXT, "body text");
                startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));

            }
        });








    }

    @Override
    public void onBackPressed() {


        Intent intent = new Intent(Main_menu.this, Signin_Activity.class);
        intent.putExtra("user", "Random");
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!Global.isNetworkAvailable(Main_menu.this)) {
            track.setVisibility(View.GONE);
            adduser.setVisibility(View.GONE);
            deluser.setVisibility(View.GONE);
            chgpwd.setVisibility(View.GONE);
            mmsettings.setVisibility(View.GONE);
            report.setVisibility(View.GONE);
            send_mylocation.setVisibility(View.GONE);
            vtourreport.setVisibility(View.GONE);
            mmbackup.setVisibility(View.GONE);
            pres.setVisibility(View.VISIBLE);
            preslink.setVisibility(View.GONE);;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case 100:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("text/plain");
                    //emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"bhupendramishr@gmail.com"});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject here");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "body text");
                    emailIntent.putExtra(Intent.EXTRA_STREAM, selectedImage);
                    startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
                }
        }
    }


    public void unread_notifications() {
        if (!userlogged.equalsIgnoreCase("saurabh")) {
            Firebase u_ref = new Firebase(Global.FIREBASE_URL).child("users").child(userlogged).child("notifications");

            u_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                //PopupMenu popup = new PopupMenu(Add_user.this, seluser);

                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    for (DataSnapshot child : snapshot.getChildren()) {
                        if (child.getKey() != null) {
                            Map<String, Object> map = (Map<String, Object>) child.getValue();
                            String title = map.get("title").toString();
                            String message = map.get("message").toString();
                            int read = Integer.parseInt(map.get("read").toString());
                            if (read == 0) {
                                i++;
                            }

                            Log.d("item", "");
                        }
                    }

                    notification_user.setText("NOTIFICATION" + " ( " + String.valueOf(i) + " )");
                    if (i > 0) {
                        Intent intent = new Intent(Main_menu.this, Notifications_user.class);
                        intent.putExtra("user", userlogged);
// use System.currentTimeMillis() to have a unique ID for the pending intent
                        PendingIntent pIntent = PendingIntent.getActivity(Main_menu.this, (int) System.currentTimeMillis(), intent, 0);
                        Notification n = new Notification.Builder(Main_menu.this)
                                .setContentTitle("Aditsa Director :")
                                .setContentText(String.valueOf(i) + " unread messages")
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentIntent(pIntent)
                                .setAutoCancel(true)
                                .build();
                        NotificationManager notificationManager = (NotificationManager)
                                getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.notify(0, n);
                    }
                }


                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    // System.out.println("The read failed: ");
                }


            });
        }
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
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
