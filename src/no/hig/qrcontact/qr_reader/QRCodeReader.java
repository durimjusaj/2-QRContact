package no.hig.qrcontact.qr_reader;

import java.io.IOException;

import android.util.Log;

import android.view.SurfaceView;
import android.view.SurfaceHolder;

import android.annotation.SuppressLint;
import android.content.Context;

import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.AutoFocusCallback;

/** A basic Camera preview class */
public class QRCodeReader extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder mHolder;
	private Camera mCamera;
	private PreviewCallback previewCallback;
	private AutoFocusCallback autoFocusCallback;

	public QRCodeReader(Context context, Camera camera,
			PreviewCallback previewCb, AutoFocusCallback autoFocusCb) {
		super(context);
		mCamera = camera;
		previewCallback = previewCb;
		autoFocusCallback = autoFocusCb;

		mHolder = getHolder();
		mHolder.addCallback(this);

		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			// Log.d("DBG", "Error setting camera preview: " + e.getMessage());
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// Camera preview released in activity
	}

	@SuppressLint("NewApi")
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (mHolder.getSurface() == null) {
			return;
		}

		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			// Log.d("DBG", "Error starting camera preview: " + e.getMessage());
		}

		try {

			mCamera.setDisplayOrientation(90);

			mCamera.setPreviewDisplay(mHolder);
			mCamera.setPreviewCallback(previewCallback);
			mCamera.startPreview();
			mCamera.autoFocus(autoFocusCallback);
		} catch (Exception e) {
			// Log.d("DBG", "Error starting camera preview: " + e.getMessage());
		}
	}

}
