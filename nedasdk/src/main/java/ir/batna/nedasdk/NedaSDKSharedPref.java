package ir.batna.nedasdk;

import android.content.Context;
import android.content.SharedPreferences;

public class NedaSDKSharedPref {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public NedaSDKSharedPref(Context context) {

        sharedPreferences = context.getSharedPreferences("NedaSharedPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveData(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public void saveData(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public String loadStringData(String key) {

        String result = sharedPreferences.getString(key, "");
        return result;
    }

    public boolean loadBooleanData(String key) {

        boolean result = sharedPreferences.getBoolean(key, false);
        return result;
    }
}