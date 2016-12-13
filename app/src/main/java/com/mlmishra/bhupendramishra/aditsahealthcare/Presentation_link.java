package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.Firebase;

public class Presentation_link extends Activity {
        Button addlink;
        Button deletelink;
        Button deleteall;
        Firebase ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation_link);
        addlink=(Button)findViewById(R.id.padd);
        deletelink=(Button)findViewById(R.id.pdelete);
        deleteall=(Button)findViewById(R.id.pdeleteall);
        ref=new Firebase(Global.FIREBASE_URL);

        addlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Presentation_link.this, Add_presentation_link.class);
                startActivity(intent);

            }
        });

        deletelink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Presentation_link.this, Delete_presentation_link.class);
                startActivity(intent);

            }
        });
        deleteall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ref.child("plinks").removeValue();
                Toast.makeText(Presentation_link.this,"All links deleted successfully", Toast.LENGTH_LONG).show();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_presentation_link, menu);
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
        if(!Global.isNetworkAvailable(Presentation_link.this)) {
            Intent intent = new Intent(Presentation_link.this, Signin_Activity.class);
            startActivity(intent);
            finish();
        }
    }
}
