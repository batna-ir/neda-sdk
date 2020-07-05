package ir.batna.nedasdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import static ir.batna.nedasdk.NedaUtils.log;

public class Neda {

    public static OnClientRegisteredListener clientRegisteredListener;

    public static void register(Context context, OnClientRegisteredListener onClientRegisteredListener) {

        clientRegisteredListener = onClientRegisteredListener;
        String packageName = context.getPackageName();
        log("Configuring NedaClient for: " + packageName);
        String signature = AppSignature.getAppSignatureDigest(context);
        long installDate = 0;
        try {
            installDate = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).firstInstallTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        log("Package: " + packageName + ", signature: " + signature + " installDate: " + installDate);
        Intent intent = new Intent();
        intent.putExtra(NedaUtils.TYPE, NedaUtils.REGISTER_APP);
        intent.putExtra(NedaUtils.APP, packageName);
        if (signature != null) {
            intent.putExtra(NedaUtils.SIGNATURE, signature);
        }
        intent.putExtra(NedaUtils.INSTALL_DATE, installDate);
        intent.setComponent(new ComponentName(NedaUtils.NEDA_PACKAGE_NAME, NedaUtils.NEDA_COMPONENT_SERVICE));
        context.startService(intent);
        log("Register request sent to Neda");

    }
}
