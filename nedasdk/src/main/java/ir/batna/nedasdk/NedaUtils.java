package ir.batna.nedasdk;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NedaUtils {

    public static final String NEDA_PACKAGE_NAME = "ir.batna.neda";
    public static final String NEDA_COMPONENT_SERVICE = "ir.batna.neda.service.NedaService";
    public static final String NEDA_TOKEN = "nedaToken";
    public static final String TYPE = "type";
    public static final String REGISTER_APP = "registerApp";
    public static final String APP = "app";
    public static final String NEDA_CLIENT_SERVICE = "MyIntentService";
    public static final String PUSH = "push";
    public static final String TOKEN = "token";
    public static final String DATA = "data";
    public static final String SIGNATURE = "signature";
    public static final String NOTIFICATION_CHANNEL = " notification channel";
    public static final String INSTALL_DATE = "installDate";
    public static final int UPPER_BOUND = 2100000;
    public static final int NOTIFICATION_ID = 100;
    public static final int VIBRATE_DURATION = 300;

    public static String getSha256(String input) {

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.reset();

            byte[] byteData = digest.digest(input.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < byteData.length; i++){
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void log(String text) {

        Log.v("NEDA ", "==> " + text);
    }
}
