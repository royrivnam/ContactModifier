package com.example.dell.contactsmodifier;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.util.ArrayList;

public class AddContactAll {

    Context ctx;
    String DisplayName = null;
    String MobileNumber = null;

    public AddContactAll(Context ctx, String name, String number)
    {
        this.ctx=ctx;
        this.DisplayName=name;
        this.MobileNumber=number;
    }

    public void Add()
    {
        ArrayList<ContentProviderOperation> ops = new ArrayList < ContentProviderOperation > ();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        if (DisplayName != null) {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            DisplayName).build());
        }

        if (MobileNumber != null) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }
        try {
            ctx.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Constants.totCon++;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ctx, "Permission not granted!! Grant permission!!", Toast.LENGTH_SHORT).show();
        }
    }
}
