package com.mlmishra.bhupendramishra.aditsahealthcare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by bhupendramishra on 01/01/16.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context,Intent intent)
    {
        Intent intent1 = new Intent(context,Signin_Activity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);

    }
}
