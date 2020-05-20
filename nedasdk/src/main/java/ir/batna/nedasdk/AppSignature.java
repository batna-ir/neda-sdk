package ir.batna.nedasdk;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.os.Build;

public class AppSignature {

    public static String getAppSignatureDigest(Context context) {

        Signature[] signatures = null;
        try {

            if (Build.VERSION.SDK_INT >= 28) {
                SigningInfo signingInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNING_CERTIFICATES).signingInfo;
                signatures = signingInfo.getApkContentsSigners();
            } else {
                signatures = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES).signatures;
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (signatures != null) {
            return NedaUtils.getSha256(signatures[0].toCharsString());
        } else {
            return null;
        }
    }
}
