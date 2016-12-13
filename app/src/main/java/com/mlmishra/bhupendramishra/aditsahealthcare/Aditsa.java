package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.app.Application;

import com.firebase.client.Firebase;


/**
 * Created by bhupendramishra on 13/01/16.
 */
public class Aditsa extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);

        // other setup code
    }

}
