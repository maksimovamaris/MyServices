package com.example.myservices;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
private static final String TAG="MainAct";
    private static final String MESSAGE = "TIMER_COUNTDOWN";
    private long parameter;

private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.start).setOnClickListener(this::onClick);
        findViewById(R.id.stop).setOnClickListener(this::onClick);
        editText=findViewById(R.id.parameter);

    }
public void onClick(View v) {
    switch (v.getId()) {
        case R.id.start:
            Log.d(TAG, "start_service_button clicked");
            parameter=Integer.parseInt(editText.getText().toString());
            editText.setText("");
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
        parameter=1000*40L;
        Intent intent=new Intent(this,TimerService.class).putExtra(MESSAGE,parameter);
        startService(intent);
    }
    private void stopService()
    {
        Intent intent=new Intent(this,TimerService.class);
        intent.setAction(TimerService.ACTION_CLOSE);
        startService(intent);
    }

}
