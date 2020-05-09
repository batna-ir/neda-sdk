package ir.batna.nedasdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Neda {

    public static OnClientRegisteredListener clientRegisteredListener;

    public static void register(Context context, OnClientRegisteredListener onClientRegisteredListener) {
        clientRegisteredListener = onClientRegisteredListener;
        NedaSDKSharedPref sharedPref = new NedaSDKSharedPref(context);
        String nedaToken = sharedPref.loadStringData(NedaUtils.NEDA_TOKEN);
        if (!nedaToken.equalsIgnoreCase("")) {
            clientRegisteredListener.onClientRegistered(nedaToken);
        } else {
            String packageName = context.getPackageName();
            Log.i("Configuring Neda ", "for the first time");
            Intent intent = new Intent();
            intent.putExtra(NedaUtils.TYPE, NedaUtils.REGISTER_APP);
            intent.putExtra(NedaUtils.APP, packageName);
            intent.setComponent(new ComponentName(NedaUtils.NEDA_PACKAGE_NAME, NedaUtils.NEDA_COMPONENT_NAME));
            context.startService(intent);
        }
    }
}
