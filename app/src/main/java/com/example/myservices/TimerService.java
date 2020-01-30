package com.example.myservices;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class TimerService extends Service {
    private static final String TAG = "TimerService";
    static final String ACTION_CLOSE = "TIME_SERVICE_ACTION_CLOSE";
    static final String MESSAGE = "TIMER_COUNTDOWN";
    private final String CHANNEL_ID = "channel_id_2";
    private NotificationCompat.Builder builder;
    private static final long TIMER_PERIOD = 1000L;

    private static final long TIME_COUNTDOWN = 1000 * 20L;
    private long countdown;
    private CountDownTimer mCountDownTimer;
    private static final int NOTIFICATION_ID = 1;
    private int progress = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        Log.d(TAG, "onCreate() called");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onstartcommand called");
        switch(intent.getAction()) {
            case (ACTION_CLOSE):
                stopSelf();
                break;
            default: {
                countdown = intent.getLongExtra(MESSAGE, TIME_COUNTDOWN);
                startCountDownTimer(countdown, TIMER_PERIOD);
                startForeground(NOTIFICATION_ID, createNotification(1000, progress));
                break;
            }
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification createNotification(long currentTime, int progress) {
        builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Intent intentCloseService = new Intent(this, TimerService.class);
        intentCloseService.setAction(ACTION_CLOSE);
        PendingIntent pendingIntentCloseService = PendingIntent.getService(this, 0, intentCloseService, 0);

        builder.setContentTitle(getResources().getString(R.string.timer_title))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setProgress((int) (currentTime), progress, false)
                .setContentText(getResources().getString(R.string.timer_desc) + currentTime)
                .setOnlyAlertOnce(true)

                .addAction(0, getString(R.string.stop), pendingIntentCloseService)
                .setContentIntent(pendingIntent);
        return builder.build();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        CharSequence name = getString(R.string.channel_name);
        String descr = getString(R.string.channel_descr);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(descr);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

    }

    private void updateNotification(@NonNull Notification notification, int maxProgress) {
        progress = (int) (TIMER_PERIOD) + progress;
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        builder.setProgress(maxProgress, progress, false);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

    private void startCountDownTimer(long time, long period) {
        mCountDownTimer = new CountDownTimer(time, period) {
            @Override
            public void onTick(long millisUntilFinished) {
                progress = progress + 1;
                Log.d(TAG, "onTick() called with: millisUntilFinished = [" + millsToSeconds(millisUntilFinished) + "]");
                updateNotification(createNotification(millsToSeconds(millisUntilFinished), progress), (int) (time));
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "startCountDownTimer() called with: time = [" + time + "], period = [" + period + "]");
                stopSelf();
            }
        };
        mCountDownTimer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
        stopCountDownTimer();
    }

    private void stopCountDownTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }

    }

    private long millsToSeconds(long time) {
        return time / 1000L;
    }
}
