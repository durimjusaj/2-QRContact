package no.hig.qrcontact;

import no.hig.qrcontact.db.ContactDBHendler;
import no.hig.qrcontact.entities.Contact;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QRContactEditActivity extends Activity {

	private TextView name;
	private TextView surname;
	private TextView number;
	private TextView email;
	private Button save;
	private Button cancel;
	private ContactDBHendler db;
	private Contact c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qrcontact_edit);

		name = (TextView) findViewById(R.id.edit_name);
		surname = (TextView) findViewById(R.id.edit_surname);
		number = (TextView) findViewById(R.id.edit_number);
		email = (TextView) findViewById(R.id.edit_email);

		save = (Button) findViewById(R.id.edit_save_qr);
		cancel = (Button) findViewById(R.id.edit_cancel_qr);

		db = new ContactDBHendler(getApplicationContext());

		final Intent i = getIntent();
		if (i != null) {
			c = db.getContact(Integer.parseInt(i.getExtras().get("id")
					.toString()));
		} else {
			process(RESULT_CANCELED);
		}
		if (c != null) {
			name.setText(c.getName());
			surname.setText(c.getSurname());
			number.setText(c.getNumber());
			email.setText(c.getEmail());
		}

		save.setOnClickListener(new OnClickListener() {

			@SuppressLint({ "NewApi", "NewApi" })
			@Override
			public void onClick(View v) {
				if (name.getText().equals("") || surname.getText().equals("")
						|| number.getText().equals("")) {
					Toast.makeText(QRContactEditActivity.this,
							"All Fields Required.", Toast.LENGTH_SHORT).show();

				} else {

					c.setName(name.getText().toString());
					c.setSurname(surname.getText().toString());
					c.setNumber(number.getText().toString());

					if (email.getText().toString().trim().length() != 0) {
						if (android.util.Patterns.EMAIL_ADDRESS.matcher(
								email.getText().toString()).matches()) {
							c.setEmail(email.getText().toString());
							db.updateContact(c);

							Toast.makeText(QRContactEditActivity.this,
									R.string.succes_saved, Toast.LENGTH_SHORT)
									.show();

							process(Activity.RESULT_OK);
						} else {
							Toast.makeText(QRContactEditActivity.this,
									R.string.email_check, Toast.LENGTH_SHORT)
									.show();
						}
					} else {
						c.setEmail("");
						db.updateContact(c);

						Toast.makeText(QRContactEditActivity.this,
								R.string.succes_saved, Toast.LENGTH_SHORT)
								.show();

						process(Activity.RESULT_OK);
					}
				}

			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				process(RESULT_CANCELED);
			}
		});

	}

	private void process(int resultCode) {
		Intent in = new Intent();
		if (getParent() == null) {
			setResult(resultCode, in);
		} else {
			getParent().setResult(resultCode, in);
		}
		finish();
	}
}
