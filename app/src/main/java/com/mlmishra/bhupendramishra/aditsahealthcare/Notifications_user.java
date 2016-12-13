package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.content.Intent;
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

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;

import java.util.ArrayList;
import java.util.Map;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;

public class Notifications_user extends Activity {
    TextView ninfo,tv;
    String userlogged1;
    ListView notification_list,notification_list_unread;
    ArrayList<String> listOfString_read,listOfString_unread;
    public ArrayAdapter<String> adapter_read,adapter_unread;
    private int count=0;
    String notification_data="";
    String separator = System.getProperty("line.separator");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_user);
        ninfo=(TextView)findViewById(R.id.ninfo);
        Intent i = getIntent();
        userlogged1 = i.getStringExtra("user");
        notification_list = (ListView) findViewById(R.id.notification_list);
        notification_list_unread=(ListView)findViewById(R.id.notification_list_unread);
        //tv=(TextView)findViewById(R.id.tv);
        //ninfo.setText("Please wait .... Data is loading from the server");
        list_populate();
       // ninfo.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notifications_user, menu);
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


    public void list_populate()
    {
            //ninfo.setText("please wait .... Data Loading from Server ....");
            Firebase u_ref = new Firebase(Global.FIREBASE_URL).child("users").child(userlogged1).child("notifications");
            listOfString_read = new ArrayList<String>();
            listOfString_unread = new ArrayList<String>();


            u_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                //PopupMenu popup = new PopupMenu(Add_user.this, seluser);
                int pos=0;
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    for (DataSnapshot child : snapshot.getChildren()) {
                        if (child.getKey() != null) {
                            Map<String, Object> map = (Map<String, Object>) child.getValue();
                            String title=map.get("title").toString();
                            String message=map.get("message").toString();
                            int read=Integer.parseInt(map.get("read").toString());
                            if(read==0) {

                                //tv.setTextColor(Color.RED);

                                listOfString_unread.add(child.getKey() + separator + title + separator + message + separator);

                                
                            }
                            else
                            {
                                //tv.setTextColor(Color.GREEN);
                                listOfString_read.add(child.getKey() + separator + title + separator + message + separator);

                            }
                            pos++;
                            Log.d("item", "");
                        }
                    }
                    adapter_read = new ArrayAdapter<String>(Notifications_user.this,
                            R.layout.list2, listOfString_read);

                    adapter_unread = new ArrayAdapter<String>(Notifications_user.this,
                            R.layout.list3, listOfString_unread);
                    adapter_read.notifyDataSetChanged();
                    adapter_unread.notifyDataSetChanged();
                    notification_list.setAdapter(adapter_read);
                    notification_list_unread.setAdapter(adapter_unread);
                    registerForContextMenu(notification_list);
                    registerForContextMenu(notification_list_unread);


                    notification_list.invalidateViews();
                    notification_list_unread.invalidateViews();

                    // listening to single list item on click
                    notification_list_unread.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {

                            // selected item
                            String product = ((TextView) view).getText().toString();
                            ninfo.setText(product);
                            String[] data=product.split(separator);
                            Firebase del_not=new Firebase(Global.FIREBASE_URL).child("users").child(userlogged1).child("notifications").child(data[0]).child("read");
                            del_not.setValue("1");
                            Global.user_log(new Firebase(Global.FIREBASE_URL), userlogged1, "Notificaton :" + data[0] + " : " + data[1] + " : " + data[2] + " read by user. Notification automatically deleted");
                            list_populate();

                        }
                    });

                    notification_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {

                            // selected item
                            String product = ((TextView) view).getText().toString();
                            ninfo.setText(product);
                            //list_populate();

                        }
                    });
                  // ninfo.setText("");
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    // System.out.println("The read failed: ");
                }


            });
        }



}
