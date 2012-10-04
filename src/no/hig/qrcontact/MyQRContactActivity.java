package no.hig.qrcontact;

import no.hig.qrcontact.db.MyQRContactDBHendler;
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

public class MyQRContactActivity extends Activity {

	private TextView name;
	private TextView surname;
	private TextView number;
	private TextView email;
	private Button save;
	private Button cancel;
	private MyQRContactDBHendler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_qrcontact);

		name = (TextView) findViewById(R.id.name);
		surname = (TextView) findViewById(R.id.surname);
		number = (TextView) findViewById(R.id.number);
		email = (TextView) findViewById(R.id.email);

		save = (Button) findViewById(R.id.save_qr);
		cancel = (Button) findViewById(R.id.cancel_qr);

		db = new MyQRContactDBHendler(getApplicationContext());

		Contact c = db.getContact(1);

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
					Toast.makeText(MyQRContactActivity.this,
							R.string.fields_required, Toast.LENGTH_SHORT)
							.show();

				} else {

					Contact myQRContact = new Contact(
							name.getText().toString(), surname.getText()
									.toString(), number.getText().toString());
					myQRContact.setId(1);
					if (email.getText().toString().trim().length() != 0) {
						if (android.util.Patterns.EMAIL_ADDRESS.matcher(
								email.getText().toString()).matches()) {
							myQRContact.setEmail(email.getText().toString());
							db.addContact(myQRContact);

							Toast.makeText(MyQRContactActivity.this,
									R.string.succes_saved, Toast.LENGTH_SHORT)
									.show();

							process(Activity.RESULT_OK);
						} else {
							Toast.makeText(MyQRContactActivity.this,
									R.string.email_check, Toast.LENGTH_SHORT)
									.show();
						}
					} else {
						myQRContact.setEmail("");
						db.addContact(myQRContact);

						Toast.makeText(MyQRContactActivity.this,
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
				process(Activity.RESULT_CANCELED);
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
