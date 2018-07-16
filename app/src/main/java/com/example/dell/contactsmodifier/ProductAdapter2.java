package com.example.dell.contactsmodifier;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class ProductAdapter2 extends BaseAdapter {


    private Activity act;
    private LayoutInflater inflater;
    private List<product_pojo> proList;
    private Context context;

    public ProductAdapter2(Activity act, List<product_pojo> proList)
    {
        this.act=act;
        this.proList=proList;

    }

    @Override
    public int getCount(){return proList.size();}

    @Override
    public Object getItem(int position){return proList.get(position);}

    @Override
    public long getItemId(int position){return position;}

    @Override
    public View getView(int position, View v, ViewGroup parent)
    {
    if(inflater==null)
        inflater=(LayoutInflater)act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     if(v==null)
         v=inflater.inflate(R.layout.pro_view_format,null);

        TextView p1= (TextView) v.findViewById(R.id.t1);
        TextView p2= (TextView) v.findViewById(R.id.t2);
        TextView p3= (TextView)v.findViewById(R.id.t3);

        product_pojo list=proList.get(position);
        context = v.getContext();
        p1.setText(list.getName());
        p1.setOnClickListener(new ListItemClickListener(position,list));
        p2.setOnClickListener(new ListItemClickListener2(position, list,0));
        p3.setOnClickListener(new ListItemClickListener2(position, list,1));

        return v;

    }

    private class ListItemClickListener implements View.OnClickListener{

        int position;
        product_pojo list;

        public ListItemClickListener(int position,product_pojo list)
        {
            this.position=position;
            this.list=list;
        }

        @Override
        public void onClick(final View v) {

            final Dialog d = new Dialog(v.getRootView().getContext());
            final product_pojo list = proList.get(position);
            d.setContentView(R.layout.menu);
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            TextView t = (TextView) d.findViewById(R.id.t1);
            TextView tt = (TextView) d.findViewById(R.id.t2);
            TextView b = (TextView) d.findViewById(R.id.t3);
            TextView bb = (TextView) d.findViewById(R.id.t4);


            t.setText(list.getName());
            tt.setText(list.getNumber());
            b.setText("WAPP");

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ProductHandler db;
                    db = new ProductHandler(v.getRootView().getContext());
                    db.updateProduct(new product_pojo(list.getName(), list.getNumber(), "YES"));
                    notifyDataSetChanged();
                    AddContact c=new AddContact(v.getContext(),list.getName().trim(),list.getNumber().trim());
                    c.Add();
                    Constants.index=1;
                    displaySelectedScreen2(0);
                    d.dismiss();

                }
            });
            bb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteContact(v.getContext(),list.getNumber().trim(),list.getName().trim());
                    ProductHandler db;
                    db = new ProductHandler(v.getRootView().getContext());
                    db.deleteCon(list.getName(),list.getNumber());
                    notifyDataSetChanged();
                    Constants.index=1;
                    displaySelectedScreen2(0);
                    d.dismiss();
                }
            });
            d.show();
        }
    }

    public static boolean deleteContact(Context ctx, String phone, String name) {
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        Cursor cur = ctx.getContentResolver().query(contactUri, null, null, null, null);
        try {
            if (cur.moveToFirst()) {
                do {
                    if (cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)).equalsIgnoreCase(name)) {
                        String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                        ctx.getContentResolver().delete(uri, null, null);
                        return true;
                    }
                } while (cur.moveToNext());
            }
            Toast.makeText(ctx,name+" blocked!!",Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ctx, "Permission not granted!! Grant permission!!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private class ListItemClickListener2 implements View.OnClickListener {

        int position;
        product_pojo list;
        int sim=0;

        public ListItemClickListener2(int position, product_pojo list,int sim) {
            this.position = position;
            this.list = list;
            this.sim=sim;
        }

        @Override
        public void onClick(View v) {

            if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                String simSlotName[] = {
                        "extra_asus_dial_use_dualsim",
                        "com.android.phone.extra.slot",
                        "slot",
                        "simslot",
                        "sim_slot",
                        "subscription",
                        "Subscription",
                        "phone",
                        "com.android.phone.DialingMode",
                        "simSlot",
                        "slot_id",
                        "simId",
                        "simnum",
                        "phone_type",
                        "slotId",
                        "slotIdx"
                };


                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + list.getNumber().trim()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("com.android.phone.force.slot", true);
                intent.putExtra("Cdma_Supp", true);
                for (String s : simSlotName)
                    intent.putExtra(s, sim);
                v.getContext().startActivity(intent);

            }
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
            FragmentTransaction ft = ((FragmentActivity)context).getSupportFragmentManager()
                    .beginTransaction();
            ft.replace(R.id.content_frame2, fragment2);
            try{ft.commit();}
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}