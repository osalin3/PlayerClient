package com.example.ohsal.playerclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ohsal.Service.AudioServerAIDL;

public class MainActivity extends AppCompatActivity {
    private AudioServerAIDL AudioService;
    private boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(isBound)
                    {
                        AudioService.playClip("Clip");
                    }
                    else
                    {
                        System.out.println("SERVICE NOT BOUND");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(!isBound){
            boolean b = false;

            Intent i = new Intent(AudioServerAIDL.class.getName());
            ResolveInfo info = getPackageManager().resolveService(i, Context.BIND_AUTO_CREATE);
            i.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));

            b = bindService(i, this.mConnection, Context.BIND_AUTO_CREATE);
            if (b) {
                System.out.println("bindService() succeeded!");
            } else {
                System.out.println("bindService() failed!");
            }

        }
    }

    @Override
    protected void onPause(){
        if(isBound){
            unbindService(this.mConnection);
        }
        super.onPause();
    }


    private final ServiceConnection mConnection = new ServiceConnection()
    {
        public void onServiceConnected(ComponentName className, IBinder iservice)
        {
            AudioService = AudioServerAIDL.Stub.asInterface(iservice);
            isBound = true;
        }
        public void onServiceDisconnected(ComponentName className)
        {
            AudioService = null;
            isBound = true;
        }
    };
}
