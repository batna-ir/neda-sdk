package ir.batna.nedasdk;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import java.util.Random;

import static ir.batna.nedasdk.Neda.clientRegisteredListener;
import static ir.batna.nedasdk.NedaUtils.log;

public class ClientService extends IntentService {

    private static CharSequence name;
    public ClientService() {
        super(NedaUtils.NEDA_CLIENT_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        log("NedaClientSrvice started");
        name = getAppName(this);
        if (Build.VERSION.SDK_INT >= 26) {
            configureForegroundService(this, this.getPackageName());
        }
        NedaSDKSharedPref sharedPref = new NedaSDKSharedPref(getApplicationContext());
        final Bundle bundle = intent.getExtras();
        String type = bundle.getString(NedaUtils.TYPE);
        log("Intent type is:" + type);
        switch (type) {

            case NedaUtils.REGISTER_APP:
                saveToken(bundle, sharedPref);
                break;
            case NedaUtils.PUSH:
                handlePushMessage(bundle);
                break;
        }
    }

    private void saveToken(Bundle bundle, NedaSDKSharedPref sharedPref) {

        String packageName = bundle.getString(NedaUtils.APP);
        if (packageName.equalsIgnoreCase(getApplicationContext().getPackageName())) {

            String nedaToken = bundle.getString(NedaUtils.TOKEN);
            if (nedaToken == null || nedaToken.equalsIgnoreCase("")) {

                log("NedaToken received null or empty");
            } else {

                log("Package: " + packageName + ", NedaToken: " + nedaToken);
                sharedPref.saveData(NedaUtils.NEDA_TOKEN, nedaToken);
                clientRegisteredListener.onClientRegistered(nedaToken);
                if (!sharedPref.loadStringData(NedaUtils.NEDA_TOKEN).equalsIgnoreCase("")) {
                    log("NedaToken saved in app storage");
                }
            }
        } else {
            log("Received token was not intended for this application, so how did we receive it?!!!");
        }
    }

    private void handlePushMessage(Bundle bundle) {

        String data = bundle.getString(NedaUtils.DATA);
        log("Push received: " + data);
        handleMessage(data);
    }

    public void handleMessage(String data) {

        log("Creating default Notification");
        createNotification(this, data);
    }

    private void createNotification(Context context, String text) {

        if (Build.VERSION.SDK_INT >= 23) {
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (Build.VERSION.SDK_INT >= 26) {
                log("Creating notification channel");
                String description = context.getPackageName() + NedaUtils.NOTIFICATION_CHANNEL;
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
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
        } else {
            log("This version of Android does NOT support notifications");
        }
    }

    private void configureForegroundService(Context context, String text) {
        createNotificationChannel(context);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, context.getPackageName())
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_launcher_foreground);
        startForeground(NedaUtils.NOTIFICATION_ID, notification.build());
        log("Foreground service started");
    }

    private void createNotificationChannel(Context context) {

        if (Build.VERSION.SDK_INT >= 26) {
            log("Creating notification channel");
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(name.toString(), name, importance);
            channel.setDescription(name.toString());
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
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
        return  appName;
    }

}
