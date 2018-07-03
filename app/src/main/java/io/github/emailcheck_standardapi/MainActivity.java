package io.github.emailcheck_standardapi;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Check whether there are contacts' emails in a given list.
 */
public class MainActivity extends AppCompatActivity {
    List<String> list = new ArrayList<>();
    List<String> results = new ArrayList<>();
    boolean booleanFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list.add("test@gmail.com");
        list.add("test@163.com");
        list.add("yangxinyu@bupt.edu.cn");

        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME+" COLLATE LOCALIZED ASC");
        if (cursor.moveToFirst()) {
            int idColum = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            int displayNameColumn = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

            // Get email addresses
            do {
                String contactId = cursor.getString(idColum);
                String displayName = cursor.getString(displayNameColumn);

                Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = "+contactId, null, null);
                if (emails.moveToFirst()) {
                    do {
                        String emailType = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                        String emailValue = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

                        //Log.d("Log", emailType);
                        //Log.d("Log", emailValue);
                        for (String email : list) {
                            if (email.equals(emailValue)) {
                                results.add(emailValue);
                                booleanFlag = true;
                            }
                        }

                    } while (emails.moveToNext());
                }
                emails.close();
            } while (cursor.moveToNext());
        }
        cursor.close();

        if (booleanFlag) {
            Log.d("Log", "There are matching emails.");
            for (String result : results) {
                Log.d("Log", result);
            }
        } else {
            Log.d("Log", "There are not matching emails.");
        }

    }

}
