package no.hig.qrcontact;

import no.hig.qrcontact.entities.Contact;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import no.hig.qrcontact.R;

public class QRContactAlerts {

	enum QRContactAlertType {
		SUCCES, ERROR, UPDATE, EXISTS
	};

	private Context con;

	public QRContactAlerts(Context c) {
		con = c;
	}

	public void ShowAddedAlert(QRContactAlertType type) {

		AlertDialog.Builder alert = new AlertDialog.Builder(con);
		alert.setTitle(R.string.contact_added_title);
		alert.setIcon(android.R.drawable.ic_dialog_info);
		switch (type) {
		case SUCCES:
			alert.setMessage(R.string.contact_added_message);
			break;
		case ERROR:
			alert.setMessage(R.string.contact_added_error_message);
			break;
		case UPDATE:
			alert.setMessage(R.string.contact_added_update_message);
			break;
		case EXISTS:
			alert.setMessage(R.string.contact_added_exists_message);
			break;
		}

		alert.setPositiveButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});

		AlertDialog dialog = alert.create();

		dialog.show();

	}

	public void ShowListContactAlert(final Contact c) {

		AlertDialog.Builder alert = new AlertDialog.Builder(con);
		alert.setTitle(R.string.contact_added_title);
		alert.setIcon(android.R.drawable.ic_menu_view);

		LayoutInflater li = LayoutInflater.from(con);
		View v = li.inflate(R.layout.qrcontact_list_click, null);

		alert.setView(v);

		final AlertDialog dialog = alert.create();

		dialog.show();

		Button call = (Button) v.findViewById(R.id.dialog_call);
		Button share = (Button) v.findViewById(R.id.dialog_share);
		Button edit = (Button) v.findViewById(R.id.dialog_edit);
		Button delete = (Button) v.findViewById(R.id.dialog_delete);

		call.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + c.getNumber()));
				con.startActivity(callIntent);

				dialog.dismiss();

			}
		});

		share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(con, QRImageGeneratorActivity.class);
				i.putExtra("value", Contact.objectToString(c));
				con.startActivity(i);

				dialog.dismiss();

			}
		});

		edit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

	}
}
