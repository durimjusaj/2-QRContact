package no.hig.qrcontact.db;

import java.util.ArrayList;
import java.util.List;

import no.hig.qrcontact.entities.Contact;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyQRContactDBHendler extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "qrcontact";

	// Contacts table name
	private static final String TABLE_CONTATCS = "myqrcontact";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_SURNAME = "surname";
	private static final String KEY_NUMBER = "number";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_SHARE = "share";

	public MyQRContactDBHendler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		SQLiteDatabase db = this.getWritableDatabase();
		onCreate(db);

	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_RSS_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_CONTATCS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_NAME + " TEXT," + KEY_SURNAME + " TEXT," + KEY_EMAIL
				+ " TEXT," + KEY_NUMBER + " TEXT," + KEY_SHARE + " INTEGER"
				+ ")";
		db.execSQL(CREATE_RSS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTATCS);

		// Create tables again
		onCreate(db);
	}

	public void addContact(Contact m) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, m.getName());
		values.put(KEY_SURNAME, m.getSurname());
		values.put(KEY_NUMBER, m.getNumber());
		values.put(KEY_SHARE, m.getShare());
		values.put(KEY_EMAIL, m.getEmail());
		values.put(KEY_ID, m.getId());
		if (!isContactExists(db, 1)) {
			db.insert(TABLE_CONTATCS, null, values);
			db.close();
		} else {
			updateContact(m);
			db.close();
		}
	}

	public List<Contact> getAllContacts() {
		List<Contact> siteList = new ArrayList<Contact>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CONTATCS
				+ " ORDER BY id DESC";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Contact m = new Contact(cursor.getString(1),
						cursor.getString(2), cursor.getString(3),
						Integer.parseInt(cursor.getString(5)));

				m.setEmail(cursor.getString(4));
				m.setId(Integer.parseInt(cursor.getString(0)));
				// Adding contact to list
				siteList.add(m);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		// return contact list
		return siteList;
	}

	public int updateContact(Contact m) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, m.getName());
		values.put(KEY_SURNAME, m.getSurname());
		values.put(KEY_NUMBER, m.getNumber());
		values.put(KEY_EMAIL, m.getEmail());
		values.put(KEY_SHARE, m.getShare());
		Log.d("Email", m.getId() + "");
		// updating row return
		int update = db.update(TABLE_CONTATCS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(m.getId()) });
		db.close();
		return update;

	}

	public Contact getContact(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CONTATCS, new String[] { KEY_ID,
				KEY_NAME, KEY_SURNAME, KEY_NUMBER, KEY_EMAIL, KEY_SHARE },
				KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null,
				null, null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();

			Contact m = new Contact(cursor.getString(1), cursor.getString(2),
					cursor.getString(3), Integer.parseInt(cursor.getString(5)));
			m.setEmail(cursor.getString(4));
			m.setId(Integer.parseInt(cursor.getString(0)));
			cursor.close();
			db.close();

			return m;
		} else {
			return null;
		}
	}

	public void deleteContact(Contact m) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTATCS, KEY_ID + " = ?",
				new String[] { String.valueOf(m.getId()) });
		db.close();
	}

	public boolean isContactExists(SQLiteDatabase db, int id) {

		Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_CONTATCS
				+ " WHERE id = '" + id + "'", new String[] {});
		boolean exists = (cursor.getCount() > 0);
		return exists;
	}

	public int getCount() {
		String selectQuery = "SELECT  * FROM " + TABLE_CONTATCS
				+ " ORDER BY id DESC";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		return cursor.getCount();
	}

}
