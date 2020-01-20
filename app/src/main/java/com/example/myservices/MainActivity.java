package com.example.myservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
private static final String TAG="MainAct";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.start).setOnClickListener(this::onClick);
        findViewById(R.id.stop).setOnClickListener(this::onClick);

    }
public void onClick(View v) {
    switch (v.getId()) {
        case R.id.start:
            Log.d(TAG, "start_service_button clicked");

            startService();
            break;

        case R.id.stop:
        default:
            Log.d(TAG, "stop_service_button clicked");

            stopService();
            break;
    }
}


    private void startService()
    {
        Intent intent=new Intent(this,TimerService.class);
        startService(intent);
    }
    private void stopService()
    {
        Intent intent=new Intent(this,TimerService.class);
        intent.setAction(TimerService.ACTION_CLOSE);
        startService(intent);
    }

}
