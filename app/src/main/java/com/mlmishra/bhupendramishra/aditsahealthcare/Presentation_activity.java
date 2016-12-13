package com.mlmishra.bhupendramishra.aditsahealthcare;


import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.widget.TextView;

import java.io.File;
import android.net.Uri;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.Toast;

import java.util.List;
import android.os.Handler;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ContextMenu;
import android.widget.AdapterView.AdapterContextMenuInfo;


public class Presentation_activity extends Activity {

    String path_file= Environment.getExternalStorageDirectory()+"/.AditsaHealthCare";
    public ArrayAdapter<String> madapter;
    public ListView lv;
    public String[] files;
    public Context context;
    private Button update_pres;
    Firebase ref;
    Firebase u_ref;
    boolean download_flag=true;
    boolean filedeleted = false;
    File f;
    Handler handler;
    private static final int delete = Menu.FIRST + 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation_activity);
        update_pres=(Button)findViewById(R.id.updatepres);
        ref=new Firebase(Global.FIREBASE_URL);
        u_ref = ref.child("plinks");

        update_pres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new AlertDialog.Builder(Presentation_activity.this)
                        .setTitle("UPDATE PRESENTATION")
                        .setMessage("DO YOU REALLY WANT TO UPDATE THE PRESENTATION?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                u_ref.addListenerForSingleValueEvent(new ValueEventListener() {


                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {

                                        for (DataSnapshot child : snapshot.getChildren()) {
                                            if (child.getKey() != null) {

                                                f = new File(Environment.getExternalStorageDirectory() + "/.AditsaHealthCare/" + child.getKey().toString() + ".pptx");

                                                if (f.exists()) {

                                                    Toast.makeText(Presentation_activity.this, "Presentation already present, not downloading "+child.getKey().toString() + ".pptx"+ " delete existing file by long press", Toast.LENGTH_LONG).show();
                                                }
                                                else  {
                                                    DownloadManager.Request request = new DownloadManager.Request(
                                                            Uri.parse(child.getValue().toString()));
                                                    request.allowScanningByMediaScanner();
                                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                                    request.setDestinationInExternalPublicDir("/.AditsaHealthCare", child.getKey().toString() + ".pptx");
                                                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                                    dm.enqueue(request);
                                                }
                                            }
                                        }


                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {
                                        // System.out.println("The read failed: ");
                                    }


                                });


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
                //Intent intent = new Intent(Main_menu.this, Presentation_activity.class);
                //startActivity(intent);


            }
        });







        //getSupportActionBar().hide();
        File folder = new File(Environment.getExternalStorageDirectory() + "/.AditsaHealthCare");
        boolean success = true;

        if (!folder.exists()) {
            success = folder.mkdir();
        }

        files = folder.list();

        lv = (ListView) findViewById(R.id.list_view);
        madapter = new ArrayAdapter<String>(this, R.layout.pres_list, R.id.listtv, files);
        madapter.notifyDataSetChanged();
        lv.setAdapter(madapter);
        registerForContextMenu(lv);

        lv.invalidateViews();
        // listening to single list item on click
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // selected item

                String product = ((TextView) view).getText().toString();
                File file1 = new File(path_file+"/"+product);

                final Uri uri = Uri.fromFile(file1);
                final Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");

                PackageManager pm = getPackageManager();
                List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);
                if(list.size() > 0)
                    startActivity(intent);
                else
                    Toast.makeText(Presentation_activity.this,"No App Found to view this file", Toast.LENGTH_LONG).show();






            }
        });


    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle("");
        menu.add(0, delete, Menu.NONE, "Delete");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        final String key = ((TextView) info.targetView).getText().toString();
        //Toast.makeText(Saved_Pages.this,key,Toast.LENGTH_LONG).show();
        //Toast.makeText(Saved_Pages.this,item.getItemId(),Toast.LENGTH_LONG).show();
        switch (item.getItemId()) {


            case delete:

                handler = new Handler();
                Runnable r = new Runnable() {
                    public void run() {
                        File folder = new File(Environment.getExternalStorageDirectory() + "/.AditsaHealthCare/" + key);
                        boolean deleted = folder.delete();
                        if (deleted) {
                            Toast.makeText(Presentation_activity.this, "File Deleted " , Toast.LENGTH_LONG).show();

                            //madapter.remove(key);

                           lv.invalidateViews();
                           lv.refreshDrawableState();


                            Intent intent = new Intent(Presentation_activity.this, Presentation_activity.class);

                            startActivity(intent);
                            finish();
                        }
                    }

                };
                    handler.postDelayed(r,1000);




                return true;
            default:

                return super.onContextItemSelected(item);


        }


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_presentation_activity, menu);
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
    public void onBackPressed() {

        finish();
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
