package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import android.app.Activity;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;


public class Add_presentation_link extends Activity {
    EditText title,link;
    Button savelink,findlink;
    Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_presentation_link);
        title=(EditText)findViewById(R.id.ptitle);
        link=(EditText)findViewById(R.id.plink);
        savelink=(Button)findViewById(R.id.savelink);
        findlink=(Button)findViewById(R.id.findlink);



        ref=new Firebase(Global.FIREBASE_URL);

        savelink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ptitle=title.getText().toString();
                String plink=link.getText().toString();
                add_plink(ptitle,plink);

            }
        });

        findlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_presentation_link.this, Copycom.class);
                startActivity(intent);

            }
        });


    }

    private void add_plink(String user, String password)
    {
        Firebase tourref =ref.child("plinks");
        Map<String, Object> user_datas = new HashMap<String, Object>();
        user_datas.put(user, password);
        tourref.updateChildren(user_datas);
        Toast.makeText(Add_presentation_link.this, "link Submitted Successfully", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_presentation_link, menu);
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
    public void onResume() {
        super.onResume();
        if(!Global.isNetworkAvailable(Add_presentation_link.this)) {
            Intent intent = new Intent(Add_presentation_link.this, Signin_Activity.class);
            startActivity(intent);
            finish();
        }
    }
}
