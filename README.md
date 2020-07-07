
Neda-SDK is an Android SDK for implementing NEDA push notification platform in your app.

# Setup
Add jitpack repo to your project's build.grade file:

  	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
Add Neda-SDK library to your module's build.gradle file:
  
  	dependencies {
	        implementation 'com.github.batna-ir:neda-sdk:0.1.7'
	}
  
Add a broadcast receiver to your app's main module with the exact following atributes:

        <receiver
            android:name=".NedaClientReceiver"
            android:enabled="true">
            <intent-filter>
                <action android:name="ir.batna.neda.message" />
            </intent-filter>
        </receiver>
        
Create a class called "NedaClientReceiver" which extends ClientReceiver. This is the class you will receive notifications in. By default Neda-SDK displays receiving push notifications on the phone. In order to change the default behavior, override handleMessage() as follow:

    @Override
    public void handleMessage(Context context, String data) {

    }
    
Finally, to initialize your app in Neda platfrom write the following code in your app's main activity:

        Neda.register(this, new OnClientRegisteredListener() {
            @Override
            public void onClientRegistered(final String token) {
                // do what u want here
            }
        });
 
 You will receive your token via OnClientRegisteredListener() callback. You will need that token to send push notifications to that device. Others should not have your app's token, so don't share it and keep it secure.
