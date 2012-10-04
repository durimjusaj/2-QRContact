package no.hig.qrcontact;

import no.hig.qrcontact.R;
import no.hig.qrcontact.entities.Contact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class QRContactMainActivity extends Activity {

	private TextView t;
	private int RESULT_CODE = 456;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qrcard);

		findViewById(R.id.reader).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(),
						QRContactReaderActivity.class);
				startActivityForResult(i, RESULT_CODE);
			}
		});
		// Test
		findViewById(R.id.writer).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(),
						QRImageGeneratorActivity.class);
				startActivity(i);
			}
		});

		t = (TextView) findViewById(R.id.result);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RESULT_CODE) {
			if (resultCode == RESULT_OK) {
				String result = data.getStringExtra("result");
				Contact c = (Contact) Contact.stringToObject(result);
				if (c != null && c.getName() != null && c.getSurname() != null) {
					t.setText(c.getName() + "" + c.getSurname());
				} else {
					t.setText("This is not unique QRContact QRCode!");
				}
			}
		}

		if (resultCode == RESULT_CANCELED) {
			// Write your code on no result return
		}
	}

}
