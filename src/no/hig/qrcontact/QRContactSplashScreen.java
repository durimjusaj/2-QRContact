package no.hig.qrcontact;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;

public class QRContactSplashScreen extends Activity {

	private boolean active = true;
	private int timer = 4000;

	private Thread splashThread = null;
	
	private MediaPlayer sound = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qrcontact_splashscreen);
		
		sound = MediaPlayer.create(this, R.raw.splash_sound);
		sound.setVolume(100, 100);
		sound.start();
		 
		splashThread = new Thread() {
			
			@Override
			public void run() {
				
				try {
					int waited = 0;
	                while(active && (waited < timer)) {
	                    sleep(100);
	                    if(active) {
	                        waited += 100;
	                    }
	                }
				} catch (Exception e) {
					// Nothing 
				} finally {
					finish();
					startActivity(new Intent(getApplicationContext(), QRContactListActivity.class));
					sound.stop();
				}
			}
		
		};
		splashThread.start();
	}
}
