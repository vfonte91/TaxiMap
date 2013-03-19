package com.example.taximap.menu;

import com.example.taximap.R;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;


public class ContactsView extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_contacts);
		TextView contactView = (TextView) findViewById(R.id.contactsView);

		Cursor cursor = getContacts();

		while (cursor.moveToNext()) {

			String displayName = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
			contactView.append("Name: ");
			contactView.append(displayName);
			contactView.append("\n");
		}
	}

	private Cursor getContacts() {
		// Run query
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] Columns = new String[] { ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME };
		String Selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '"
				+ ("1") + "'";
		String[] selectionArgs = null;
		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
				+ " COLLATE LOCALIZED ASC";

		return this.managedQuery(uri, Columns, Selection, selectionArgs,
				sortOrder);
	}

}
	