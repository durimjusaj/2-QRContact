package no.hig.qrcontact;

import java.io.IOException;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import no.hig.qrcontact.qr_reader.QRCodeReader;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

public class QRContactReaderActivity extends Activity implements
		SensorEventListener {

	private Camera mCamera;
	private QRCodeReader mPreview;
	private Handler autoFocusHandler;
	private MediaPlayer beep = null;

	Button scanButton;

	ImageScanner scanner;

	private QRContactDrawCircle draw;

	// Sensor

	private SensorManager sensorManager;
	private Sensor sensor;

	private boolean previewing = true;

	static {
		System.loadLibrary("iconv");
	}

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.qr_code_reader);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		autoFocusHandler = new Handler();
		mCamera = getCameraInstance();
		
		if(mCamera == null){
			Intent in = new Intent();
			if (getParent() == null) {
				setResult(Activity.RESULT_CANCELED, in);
			} else {
				getParent().setResult(Activity.RESULT_CANCELED, in);
			}
			finish();
		}
		
		/* Instance barcode scanner */
		scanner = new ImageScanner();
		scanner.setConfig(0, Config.X_DENSITY, 3);
		scanner.setConfig(0, Config.Y_DENSITY, 3);

		mPreview = new QRCodeReader(this, mCamera, previewCb, autoFocusCB);
		FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
		preview.addView(mPreview);
		draw = new QRContactDrawCircle(this, 5);
		preview.addView(draw);

		beep = MediaPlayer.create(this, R.raw.button_click);
		beep.setVolume(100, 100);
		try {
			beep.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		scanButton = (Button) findViewById(R.id.cancel_button);

		scanButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent in = new Intent();
				if (getParent() == null) {
					setResult(Activity.RESULT_CANCELED, in);
				} else {
					getParent().setResult(Activity.RESULT_CANCELED, in);
				}
				finish();
			}
		});

		Display display = getWindowManager().getDefaultDisplay();
		draw.xMax = (float) display.getWidth() - 50;
		draw.yMax = (float) display.getHeight() - 50;

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}

	public void onPause() {
		super.onPause();
		releaseCamera();
		if (sensor != null) {
			sensorManager.unregisterListener(this);
		}
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		} catch (Exception e) {
			
		}
		return c;
	}

	private void releaseCamera() {
		if (mCamera != null) {
			previewing = false;
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (previewing)
				mCamera.autoFocus(autoFocusCB);
		}
	};

	PreviewCallback previewCb = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			Camera.Parameters parameters = camera.getParameters();
			Size size = parameters.getPreviewSize();

			Image barcode = new Image(size.width, size.height, "Y800");
			barcode.setData(data);

			int result = scanner.scanImage(barcode);

			if (result != 0) {
				previewing = false;
				mCamera.setPreviewCallback(null);
				mCamera.stopPreview();

				SymbolSet syms = scanner.getResults();
				for (Symbol sym : syms) {
					beep.start();
					Intent in = new Intent();
					in.putExtra("result", sym.getData());
					if (getParent() == null) {
						setResult(Activity.RESULT_OK, in);
					} else {
						getParent().setResult(Activity.RESULT_OK, in);
					}
					finish();

				}
			}
		}
	};

	// Mimic continuous auto-focusing
	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			// the values you were calculating originally here were over 10000!
			draw.setX(event.values[0]);
			draw.setY(event.values[1]);

		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (sensor != null) {
			sensorManager.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_GAME);
		}
	}
}
