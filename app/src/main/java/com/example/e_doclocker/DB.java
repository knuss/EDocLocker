package com.example.e_doclocker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "edoclocker";

    //extended constructor
    public DB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //sql statements to create the tables when the database is created
        String create_admin="CREATE TABLE admin (username TEXT primary key,password TEXT);";
        db.execSQL(create_admin);

        String prepopulateAdmin="INSERT INTO ADMIN VALUES('admin','admin1234');";
        db.execSQL(prepopulateAdmin);

        String create_user="CREATE TABLE user (nic TEXT PRIMARY KEY, name TEXT, email TEXT, mobile TEXT, dob TEXT, city TEXT, password TEXT, image BLOB);";
        db.execSQL(create_user);

        String create_document="CREATE TABLE document (nic TEXT, type TEXT, file TEXT, encKey BLOB);";
        db.execSQL(create_document);

        String create_tblKyes="CREATE TABLE KEYS(nic TEXT,encKey BLOB,ts TEXT);";
        db.execSQL(create_tblKyes);

        String create_feedback="CREATE TABLE feedback (userid TEXT, feedback TEXT);";
        db.execSQL(create_feedback);
    }
    public Admin verifyAdminLogin(String username,String password){
        SQLiteDatabase db=this.getReadableDatabase();
        String query="select * from admin where username='"+username+"' AND password='"+password+"'";
        Admin user=null;
        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();
        if(cursor.getCount()>0){
            user=new Admin(cursor.getString(0),cursor.getString(1));
        }
        return user;
    }
    public Citizen verifyUserLogin(String username,String password){
        SQLiteDatabase db=this.getReadableDatabase();
        String query="select * from user where nic='"+username+"' AND password='"+password+"'";
        Citizen user=null;
        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();
        if(cursor.getCount()>0){
            String nic=cursor.getString(0);
            String name=cursor.getString(1);
            String email=cursor.getString(2);
            String mobile=cursor.getString(3);
            String dob=cursor.getString(4);
            String city=cursor.getString(5);
            byte[] image=cursor.getBlob(7);
            Bitmap bitmap=null;
            if(image!=null) {
                bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            }
            user=new Citizen(nic,name,email,mobile,dob,city,password,bitmap);
        }
        return user;
    }
    public ArrayList<Citizen> getCitizens(){
        ArrayList<Citizen> data=new ArrayList<>();
        String query="select * from user";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();
        do{
            String nic=cursor.getString(0);
            String name=cursor.getString(1);
            String email=cursor.getString(2);
            String mobile=cursor.getString(3);
            String dob=cursor.getString(4);
            String city=cursor.getString(5);
            String password=cursor.getString(6);
            byte[] image=cursor.getBlob(7);
            Bitmap bitmap=null;
            if(image!=null) {
                bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            }
            Citizen citizen=new Citizen(nic,name,email,mobile,dob,city,password,bitmap);
            data.add(citizen);
        }while (cursor.moveToNext());
        return data;
    }
    public Citizen getCitizens(String _nic){
        Citizen data=null;
        String query="select * from user where nic='"+_nic+"'";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            do {
                String nic = cursor.getString(0);
                String name = cursor.getString(1);
                String email = cursor.getString(2);
                String mobile = cursor.getString(3);
                String dob = cursor.getString(4);
                String city = cursor.getString(5);
                String password = cursor.getString(6);
                byte[] image = cursor.getBlob(7);
                Bitmap bitmap = null;
                if (image != null) {
                    bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                }
                Citizen citizen = new Citizen(nic, name, email, mobile, dob, city, password, bitmap);
                data = citizen;
            } while (cursor.moveToNext());
        }
        return data;
    }
    public ArrayList<Document> getDocuments(String nic){
        ArrayList<Document> docs=new ArrayList<>();
        String sql="select * from document where nic='"+nic+"'";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(sql,null);
        if(cursor!=null && cursor.moveToFirst()) {
            do {
                String docType = cursor.getString(1);
                String file = cursor.getString(2);
                byte[] key = cursor.getBlob(3);
                Document document = new Document(docType, file, key, nic);
                docs.add(document);
            } while (cursor.moveToNext());
        }
        return docs;
    }
    public ArrayList<Feedback> getFeedback(){
        ArrayList<Feedback> fbs=new ArrayList<>();
        String sql="select * from feedback";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(sql,null);
        if(cursor!=null && cursor.moveToFirst()) {
            do {
                String nic = cursor.getString(0);
                String feedback = cursor.getString(1);
                Feedback fb = new Feedback(nic, feedback);
                fbs.add(fb);
            } while (cursor.moveToNext());
        }
        return fbs;
    }
    public Key getKey(String nic){
        Key encKey=null;
        String sql="select * from keys where nic='"+nic+"' order by enckey desc LIMIT 1";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(sql,null);
        if(cursor!=null && cursor.moveToFirst()) {
            do {
                String docType = cursor.getString(0);
                byte[] ky = cursor.getBlob(1);
                String ts = cursor.getString(2);

                encKey = new Key(docType, ky, ts);

            } while (cursor.moveToNext());
        }
        return encKey;
    }
    public void addCitizen(Citizen citizen){
        SQLiteDatabase db=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("nic",citizen.getNic());
        values.put("name",citizen.getName());
        values.put("email",citizen.getEmail());
        values.put("mobile",citizen.getMpbile());
        values.put("dob",citizen.getDob());
        values.put("city",citizen.getCity());
        values.put("password",citizen.getPassword());
        if (citizen.getImage()!=null){
            values.put("image",getBitmapAsByteArray(citizen.getImage()));
        }
        long id=db.insert("user",null,values);
    }
    public void addKey(String nic,byte[] enckey,String ts){
        SQLiteDatabase db=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("nic",nic);
        values.put("enckey",enckey);
        values.put("ts",ts);
        long id=db.insert("keys",null,values);
    }
    public void addDocument(Document document){
        SQLiteDatabase db=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("type",document.getDocType());
        values.put("file",document.getFile());
        values.put("encKey",document.getKey());
        values.put("nic",document.getUser());
        long id=db.insert("document",null,values);
    }
    public void addFeedback(Feedback feedback){
        SQLiteDatabase db=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("userid",feedback.getUserID());
        values.put("feedback",feedback.getFeedback());
        long id=db.insert("feedback",null,values);
    }
    public void updateCitizen(Citizen citizen){
        SQLiteDatabase db=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("name",citizen.getName());
        values.put("email",citizen.getEmail());
        values.put("mobile",citizen.getMpbile());
        values.put("dob",citizen.getDob());
        values.put("city",citizen.getCity());
        long id=db.update("user",values,"nic="+citizen.getNic(),null);
    }
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //if the database is upgraded then recreate the tables
        db.execSQL("DROP TABLE IF EXISTS admin");
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS document");
        db.execSQL("DROP TABLE IF EXISTS feedback");
    }
}
