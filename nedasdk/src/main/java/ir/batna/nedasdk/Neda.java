package ir.batna.nedasdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static ir.batna.nedasdk.NedaUtils.log;

public class Neda {

    public static OnClientRegisteredListener clientRegisteredListener;

    public static void register(Context context, OnClientRegisteredListener onClientRegisteredListener) {
        clientRegisteredListener = onClientRegisteredListener;
        NedaSDKSharedPref sharedPref = new NedaSDKSharedPref(context);
        String nedaToken = sharedPref.loadStringData(NedaUtils.NEDA_TOKEN);
        if (!nedaToken.equalsIgnoreCase("")) {

            log("Token already exists: " + nedaToken);
            clientRegisteredListener.onClientRegistered(nedaToken);
        } else {

            log("Configuring NedaClient for the first time");
            String packageName = context.getPackageName();
            String signature = AppSignature.getAppSignatureDigest(context);
            log("Package: " + packageName + ", signature: " + signature);
            Intent intent = new Intent();
            intent.putExtra(NedaUtils.TYPE, NedaUtils.REGISTER_APP);
            intent.putExtra(NedaUtils.APP, packageName);
            if (signature != null) {
                intent.putExtra(NedaUtils.SIGNATURE, signature);
            }
            intent.setComponent(new ComponentName(NedaUtils.NEDA_PACKAGE_NAME, NedaUtils.NEDA_COMPONENT_SERVICE));
            context.startService(intent);
            log("Register request sent to Neda");
        }
    }
}
