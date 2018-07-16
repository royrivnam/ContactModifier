package com.example.dell.contactsmodifier;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ProductHandler {

    private SQLiteHelper dbHelper;

    public ProductHandler(Context context){dbHelper=new SQLiteHelper(context);}

    int addProduct(product_pojo pj)
    {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues v=new ContentValues();
        v.put(dbHelper.Name,pj.getName());
        v.put(dbHelper.Number,pj.getNumber());
        v.put(dbHelper.Status,pj.getStatus());
        long idE=0;
        try{idE=db.insert(dbHelper.TABLE,null,v);}
        catch(Exception e)
        {
            e.printStackTrace();
        }
        db.close();
        return (int)idE;
    }

    public int updateProduct(product_pojo pj)
    {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues v=new ContentValues();
        v.put(dbHelper.Name,pj.getName());
        v.put(dbHelper.Number,pj.getNumber());
        v.put(dbHelper.Status,pj.getStatus());

        return db.update(dbHelper.TABLE,v,dbHelper.Name+"= ? and "+dbHelper.Number+"= ?",new String[]{pj.getName(),pj.getNumber()});
    }

    boolean ifContactPresent(String Name,String Number)
    {
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor c=db.query(dbHelper.TABLE,new String[]{dbHelper.Name,dbHelper.Number,dbHelper.Status},dbHelper.Name+"= ? and "+dbHelper.Number+"= ?",new String[]{Name,Number},null,null,null,null);
        if(c!=null)
            return true;
        else
            return false;

    }

    public void updateStatusToBlock()
    {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.execSQL("UPDATE "+dbHelper.TABLE+" SET "+dbHelper.Status+"='NO' WHERE "+dbHelper.Status+"='YES'");
        db.close();
    }

    public void updateStatusToUnblock()
    {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.execSQL("UPDATE "+dbHelper.TABLE+" SET "+dbHelper.Status+"='YES' WHERE "+dbHelper.Status+"='NO'");
        db.close();
    }

    public List<product_pojo> getAllProductsOthers(){

        SQLiteDatabase db=dbHelper.getWritableDatabase();
        List<product_pojo> pjList=new ArrayList<product_pojo>();
        String selque="SELECT  * FROM " + dbHelper.TABLE+" where "+dbHelper.Status+"='NO' ORDER BY "+dbHelper.Name;
        Cursor c=db.rawQuery(selque,null);
        if(c.moveToFirst())
        {
            do{
                product_pojo pj=new product_pojo();
                pj.setName(c.getString(0));
                pj.setNumber(c.getString(1));
                pj.setStatus(c.getString(2));

                pjList.add(pj);
            }while(c.moveToNext());
        }
        db.close();
        return pjList;
    }

    public List<product_pojo> getAllProductsWapp(){

        SQLiteDatabase db=dbHelper.getWritableDatabase();
        List<product_pojo> pjList=new ArrayList<product_pojo>();
        String selque="SELECT  * FROM " + dbHelper.TABLE+" where "+dbHelper.Status+"='YES' ORDER BY "+dbHelper.Name;
        Cursor c=db.rawQuery(selque,null);
        if(c.moveToFirst())
        {
            do{
                product_pojo pj=new product_pojo();
                pj.setName(c.getString(0));
                pj.setNumber(c.getString(1));
                pj.setStatus(c.getString(2));

                pjList.add(pj);
            }while(c.moveToNext());
        }
        db.close();
        return pjList;
    }

    public int getProductsCount() {

        String countQuery = "SELECT  * FROM " + dbHelper.TABLE;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int p= cursor.getCount();
        db.close();
        return p;
    }

    public int getProductsCountOthers() {

        String countQuery="SELECT  * FROM " + dbHelper.TABLE+" where "+dbHelper.Status+"='NO'";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int p= cursor.getCount();
        cursor.close();
        db.close();
        return p;
    }

    public int getProductsCountWapp() {

        String countQuery="SELECT  * FROM " + dbHelper.TABLE+" where "+dbHelper.Status+"='YES'";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int p= cursor.getCount();
        cursor.close();
        db.close();
        return p;

    }
    public int deleteCon(String name, String phone) {

        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete(dbHelper.TABLE,dbHelper.Name+"= ? AND "+dbHelper.Number+"= ?",new String[]{name, phone});
        db.close();
        return 1;

    }


}
