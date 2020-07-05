package ir.batna.nedasdk;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;

import java.util.Random;

import static ir.batna.nedasdk.Neda.clientRegisteredListener;
import static ir.batna.nedasdk.NedaUtils.log;

public class ClientReceiver extends BroadcastReceiver {

    private static CharSequence name;

    @Override
    public void onReceive(Context context, Intent intent) {

        log("NedaClientReceiver started");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context = context.createDeviceProtectedStorageContext();
        }
        String action = intent.getAction();
        if (action.equals(NedaUtils.NEDA_RECEIVER_INTENT_ACTION)) {
            log("NedaClientReceiver action type is \"message\"");
            name = getAppName(context);
            final Bundle bundle = intent.getExtras();
            String type = bundle.getString(NedaUtils.TYPE);
            log("Intent type is:" + type);
            switch (type) {

                case NedaUtils.REGISTER_APP:
                    displayToken(context, bundle);
                    break;
                case NedaUtils.PUSH:
                    handlePushMessage(context, bundle);
                    break;
            }
        }
    }

    private void displayToken(Context context, Bundle bundle) {

        String packageName = bundle.getString(NedaUtils.APP);
        if (packageName.equalsIgnoreCase(context.getPackageName())) {

            String nedaToken = bundle.getString(NedaUtils.TOKEN);
            if (nedaToken == null || nedaToken.equalsIgnoreCase("")) {

                log("NedaToken received null or empty");
            } else {

                log("Package: " + packageName + ", NedaToken: " + nedaToken);
                clientRegisteredListener.onClientRegistered(nedaToken);
            }
        } else {
            log("Received token was not intended for this application, so how did we receive it?!!!");
        }
    }

    private void handlePushMessage(Context context, Bundle bundle) {

        String data = bundle.getString(NedaUtils.DATA);
        log("Push received: " + data);
        handleMessage(context, data);
    }

    public void handleMessage(Context context, String data) {

        log("Creating default Notification");
        createNotification(context, data);
    }

    private void createNotification(Context context, String text) {

        if (Build.VERSION.SDK_INT >= 23) {
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (Build.VERSION.SDK_INT >= 26) {
                log("Creating notification channel");
                String description = context.getPackageName() + NedaUtils.NOTIFICATION_CHANNEL;
                int importance = NotificationManager.IMPORTANCE_LOW;
                NotificationChannel channel = new NotificationChannel(name.toString(), name, importance);
                channel.setDescription(description);
                notificationManager.createNotificationChannel(channel);
            }

            NotificationCompat.Builder notification = new NotificationCompat.Builder(context, name.toString())
                    .setContentText(text)
                    .setSmallIcon(R.drawable.ic_launcher_foreground);
            Random random = new Random();
            int id = random.nextInt(NedaUtils.UPPER_BOUND);
            notificationManager.notify(id, notification.build());

            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(NedaUtils.VIBRATE_DURATION);
        } else {
            log("This version of Android does NOT support notifications");
        }
    }

    private CharSequence getAppName(Context context) {

        ApplicationInfo applicationInfo = context.getApplicationInfo();
        CharSequence appName = null;
        try {
            appName = applicationInfo.nonLocalizedLabel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (appName == null) appName = context.getPackageName();
        return appName;
    }
}
