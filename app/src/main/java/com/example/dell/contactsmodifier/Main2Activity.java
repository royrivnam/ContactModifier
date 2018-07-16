package com.example.dell.contactsmodifier;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    ProductHandler db;
    int a=0,b=0;
    private SQLiteHelper dbHelper=new SQLiteHelper(Main2Activity.this);
    static final Integer LOCATION = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        db=new ProductHandler(this);

        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);
        String name="",number="",status="YES";
        if(cursor.moveToLast()) {
             do{

                name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                number=filter(number);
                db.addProduct(new product_pojo(name, number, status));

            }while (cursor.moveToPrevious());
        }
        cursor.close();
        a=db.getProductsCountWapp();
        b=db.getProductsCountOthers();
        Constants.tab1=a;
        Constants.tab2=b;
        displaySelectedScreen2(0);
        displaySelectedScreen(0);

    }

    public String filter(String phone)
    {
        String k="";
        if(phone.length()==10)
        {
            k="+91"+phone;
            return k;
        }
        else
        {
            for(int i=0;i<phone.length();i++)
            {
                if(phone.charAt(i)>='0' && phone.charAt(i)<='9')
                k=k+phone.charAt(i);
            }
            if(k.length()>10)
                k="+"+k;
            else
                k="+91"+k;
            return k;
        }
    }
    public void displaySelectedScreen(int itemId) {
        Fragment fragment = null;
        switch (itemId) {
            case 0:
                fragment = new Visible();
                break;

            case 1:
                fragment=new Invisible();
                break;

        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame1, fragment);
            ft.commit();
        }
    }
    public void displaySelectedScreen2(int item) {
        Fragment fragment2 = null;
        switch(item)
        {
            case 0:
                fragment2=new tabbed();
                break;
        }
        if (fragment2!=null) {
            FragmentTransaction ft = getSupportFragmentManager()
                    .beginTransaction();
            ft.replace(R.id.content_frame2, fragment2);
            try{ft.commit();}
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id)
        {
            case R.id.nav_unblock:
                Constants.totCon=0;
                SQLiteDatabase database=dbHelper.getWritableDatabase();
                String selque="SELECT  * FROM " + dbHelper.TABLE+" where "+dbHelper.Status+"='NO'";
                Cursor c=database.rawQuery(selque,null);
                if(c.moveToFirst())
                {
                    do{
                        AddContactAll contact=new AddContactAll(Main2Activity.this,c.getString(0).trim(),c.getString(1).trim());
                        contact.Add();
                    }while(c.moveToNext());
                }
                c.close();
                Toast.makeText(Main2Activity.this,"Contacts Unblocked: "+Constants.totCon,Toast.LENGTH_SHORT).show();
                db=new ProductHandler(Main2Activity.this);
                db.updateStatusToUnblock();
                displaySelectedScreen2(0);

                break;

            case R.id.nav_block:
                Constants.totCon=0;
                database=dbHelper.getWritableDatabase();
                String selquer="SELECT  * FROM " + dbHelper.TABLE+" where "+dbHelper.Status+"='YES'";
                Cursor cur=database.rawQuery(selquer,null);
                if(cur.moveToFirst())
                {
                    do{
                        deleteContact(Main2Activity.this,cur.getString(0).trim(),cur.getString(1).trim());
                    }while(cur.moveToNext());
                }
                cur.close();
                Toast.makeText(Main2Activity.this,"Contacts Blocked: "+Constants.totCon,Toast.LENGTH_SHORT).show();
                db=new ProductHandler(Main2Activity.this);
                db.updateStatusToBlock();
                displaySelectedScreen2(0);
                break;

            case R.id.nav_admin:
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static boolean deleteContact(Context ctx, String name, String phone) {
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        Cursor cur = ctx.getContentResolver().query(contactUri, null, null, null, null);
        try {
            if (cur.moveToFirst()) {
                do {
                    if (cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)).equalsIgnoreCase(name)) {
                        String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                        ctx.getContentResolver().delete(uri, null, null);
                        Constants.totCon++;
                        return true;
                    }
                } while (cur.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ctx, "Permission not granted!! Grant permission!!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
