package ir.batna.nedasdk;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import static ir.batna.nedasdk.Neda.clientRegisteredListener;

public class NedaClientService extends IntentService {

    public NedaClientService() {
        super(NedaUtils.NEDA_CLIENT_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i("NedaClientService ", "started now");
        NedaSDKSharedPref sharedPref = new NedaSDKSharedPref(getApplicationContext());
        final Bundle bundle = intent.getExtras();
        String type = bundle.getString(NedaUtils.TYPE);
        switch (type) {

            case NedaUtils.REGISTER_APP: saveToken(bundle, sharedPref); break;
            case NedaUtils.PUSH: handlePushMessage(bundle); break;
        }
    }

    private void saveToken(Bundle bundle, NedaSDKSharedPref sharedPref) {

        String packageName = bundle.getString(NedaUtils.APP);
        Log.i("app", packageName);
        if (packageName.equalsIgnoreCase(getApplicationContext().getPackageName())) {

            String nedaToken = bundle.getString(NedaUtils.TOKEN);
            if (nedaToken == null || nedaToken.equalsIgnoreCase("")) {

                Log.i("NedaToken ", "received null or empty");
            } else {

                Log.i("received token ", "from saba app");
                sharedPref.saveData(NedaUtils.NEDA_TOKEN, nedaToken);
                clientRegisteredListener.onClientRegistered(nedaToken);
                if (!sharedPref.loadStringData(NedaUtils.NEDA_TOKEN).equalsIgnoreCase("")) {
                    Log.i("Token saved ", " in app storage");
                }
            }
        }
    }

    private void handlePushMessage(Bundle bundle) {

        Log.i("Push received: ", "data");
        String data = bundle.getString(NedaUtils.DATA);
        handleMessage(data);
    }

    public void handleMessage(String data){

    }
}
