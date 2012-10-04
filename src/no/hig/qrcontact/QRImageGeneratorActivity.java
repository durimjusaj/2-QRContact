package no.hig.qrcontact;

import no.hig.qrcontact.qr_writer.QRCodeGenerator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import no.hig.qrcontact.R;

public class QRImageGeneratorActivity extends Activity {

	private QRCodeGenerator qrcode;
	private ImageView img;
	private Button done;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qr_gen);

		Intent i = getIntent();
		String data = "Null";
		if (i != null) {
			data = i.getStringExtra("value");
		}

		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();

		qrcode = new QRCodeGenerator(data, width, width);

		img = (ImageView) findViewById(R.id.genImg);
		done = (Button) findViewById(R.id.done);
		
		img.setVisibility(View.VISIBLE);
		img.setImageBitmap(qrcode.getQRCode());
		
		done.setOnClickListener(new OnClickListener() {
			
			@Override
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
		
	}
}
