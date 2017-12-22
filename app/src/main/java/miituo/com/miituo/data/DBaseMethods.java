package miituo.com.miituo.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by moon on 30/10/16.
 */
public class DBaseMethods {

    public static modelBase base;

//================================AsynTask to connect DB============================================
    public static class ThreadDBInsert extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            //Aqui va el nombre de la tala
            String val = strings[0];
            //Log.w("Here",val);

            switch (val){

                case modelBase.FeedEntryUsuario.TABLE_NAME:
                    // Gets the data repository in write mode
                    SQLiteDatabase db2 = base.getWritableDatabase();

                    // Create a new map of values, where column names are the keys
                    ContentValues values2 = new ContentValues();
                    //values.put(modelBase.FeedEntryArticle.COLUMN_ID, strings[6]);
                    values2.put(modelBase.FeedEntryUsuario.COLUMN_NAME, strings[1]);
                    values2.put(modelBase.FeedEntryUsuario.COLUMN_MOTHERNAME, strings[2]);
                    //values2.put(modelBase.FeedEntryUsuario.COLUMN_UBI, strings[3]);
                    values2.put(modelBase.FeedEntryUsuario.COLUMN_LASTNAME, strings[3]);
                    values2.put(modelBase.FeedEntryUsuario.COLUMN_CELPHONE, strings[4]);
                    values2.put(modelBase.FeedEntryUsuario.COLUMN_TOKEN, strings[5]);

                    // Insert the new row, returning the primary key value of the new row
                    //Just change name table and the values....
                    long newRowId2 = db2.insert(val, null, values2);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    return "" + newRowId2;

                case modelBase.FeedEntryPoliza.TABLE_NAME:
                    // Gets the data repository in write mode
                    SQLiteDatabase db = base.getWritableDatabase();

                    // Create a new map of values, where column names are the keys
                    ContentValues values = new ContentValues();
                    //values.put(modelBase.FeedEntryArticle.COLUMN_ID, strings[6]);
                    values.put(modelBase.FeedEntryPoliza.COLUMN_IDUSER, strings[1]);
                    values.put(modelBase.FeedEntryPoliza.COLUMN_INSURANCE, strings[2]);
                    values.put(modelBase.FeedEntryPoliza.COLUMN_LASTODOMETER, strings[3]);
                    values.put(modelBase.FeedEntryPoliza.COLUMN_NOPOLICY, strings[4]);
                    values.put(modelBase.FeedEntryPoliza.COLUMN_ODOMETERPIE, strings[5]);
                    values.put(modelBase.FeedEntryPoliza.COLUMN_VEHICLEPIE, strings[6]);
                    values.put(modelBase.FeedEntryPoliza.COLUMN_REPORT_STATE, strings[7]);
                    values.put(modelBase.FeedEntryPoliza.COLUMN_RATE, strings[8]);

                    // Insert the new row, returning the primary key value of the new row
                    //Just change name table and the values....
                    long newRowId = db.insert(val, null, values);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    return "" + newRowId;

                case modelBase.FeedEntryVehiculo.TABLE_NAME:
                    // Gets the data repository in write mode
                    SQLiteDatabase dbvei = base.getWritableDatabase();

                    // Create a new map of values, where column names are the keys
                    ContentValues valuesvei = new ContentValues();
                    //values.put(modelBase.FeedEntryArticle.COLUMN_ID, strings[6]);
                    valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_POLIZA, strings[1]);
                    valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_BRAND, strings[2]);
                    valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_CAPACITY, strings[3]);
                    valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_COLOR, strings[4]);
                    valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_DESCRIPTION, strings[5]);
                    valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_MODEL, strings[6]);
                    valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_PLATES, strings[7]);
                    valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_TYPE, strings[8]);
                    valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_SUBTYPE, strings[8]);

                    // Insert the new row, returning the primary key value of the new row
                    //Just change name table and the values....
                    long newRowvei = dbvei.insert(val, null, valuesvei);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    return "" + newRowvei;

                default:
                    break;
            }

            return "";
        }
    }

//================================AsynTask to update DB============================================
    public static class ThreadDBUpdate extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            //Aqui va el nombre de la tala
            /*String val = strings[0];
            Log.w("Here",val);

            switch (val){
                case modelBase.FeedEntryProyectos.TABLE_NAME:
                    // Gets the data repository in write mode
                    SQLiteDatabase db = base.getWritableDatabase();

                    // Create a new map of values, where column names are the keys
                    ContentValues values = new ContentValues();
                    values.put(modelBase.FeedEntryProyectos.COLUMN_STATUS, strings[2]);
                    values.put(modelBase.FeedEntryProyectos.COLUMN_SENT, strings[3]);

                    //update sent
                    long newRowId = db.update(val, values,modelBase.FeedEntryProyectos.COLUMN_ID +" = "+strings[1],null);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    return "" + newRowId;

                default:
                    break;
            }*/

            return "";
        }
    }

//================================AsynTask to readAll DB============================================
    public static class ThreadDBRead extends AsyncTask<String, Void, ArrayList<Object>> {

        @Override
        protected ArrayList<Object> doInBackground(String... strings) {

            String val = strings[0];
            //Log.w("Here",val);

            switch(val){
                //usuarios...
                case modelBase.FeedEntryUsuario.TABLE_NAME:
                    ArrayList<Object> proyectos = new ArrayList<>();
                    String query = "select * from "+ modelBase.FeedEntryUsuario.TABLE_NAME;
                    SQLiteDatabase db = base.getWritableDatabase();

                    Cursor cursor = db.rawQuery(query,null);
                    if (cursor.moveToFirst()){
                        do {
                            ClientMovil fond = new ClientMovil();
                            //fond.setId(Integer.parseInt(cursor.getString(0)));
                            fond.setId(Integer.parseInt(cursor.getString(0)));
                            fond.setName(cursor.getString(1));
                            fond.setLastName(cursor.getString(2));
                            fond.setMotherName(cursor.getString(3));
                            fond.setCelphone(cursor.getString(4));
                            fond.setToken(cursor.getString(5));

                            proyectos.add(fond);
                        }while(cursor.moveToNext());
                    }

                    return proyectos;

                case modelBase.FeedEntryPoliza.TABLE_NAME:
                    ArrayList<Object> polizas = new ArrayList<>();
                    String querypoliza = "select * from "+ modelBase.FeedEntryPoliza.TABLE_NAME;
                    SQLiteDatabase dbpoliza = base.getWritableDatabase();

                    Cursor cursorpoliza = dbpoliza.rawQuery(querypoliza,null);
                    if (cursorpoliza.moveToFirst()){
                        do {
                            try {
                                InsurancePolicyDetail fond = new InsurancePolicyDetail();

                                fond.setId(Integer.parseInt(cursorpoliza.getString(1)));
                                fond.setInsuranceCarrier(new InsuranceCarrier(0, cursorpoliza.getString(2)));
                                fond.setLastOdometer(Integer.parseInt(cursorpoliza.getString(3)));
                                fond.setNoPolicy(cursorpoliza.getString(4));

                                fond.setHasOdometerPicture(Boolean.parseBoolean(cursorpoliza.getString(5)));
                                fond.setHasVehiclePictures(Boolean.parseBoolean(cursorpoliza.getString(6)));

                                fond.setReportState(Integer.parseInt(cursorpoliza.getString(7)));
                                fond.setRate(Float.parseFloat(cursorpoliza.getString(8)));

                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                Date date = formatter.parse(cursorpoliza.getString(9));
                                fond.setLimitReportDate(date);

                                polizas.add(fond);
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }while(cursorpoliza.moveToNext());
                    }

                    return polizas;

                case modelBase.FeedEntryVehiculo.TABLE_NAME:
                    ArrayList<Object> carritos = new ArrayList<>();
                    String queryauto = "select * from "+ modelBase.FeedEntryVehiculo.TABLE_NAME;
                    SQLiteDatabase dbcarro = base.getWritableDatabase();

                    Cursor cursorcar = dbcarro.rawQuery(queryauto,null);
                    if (cursorcar.moveToFirst()){
                        do {
                            VehicleMovil fond = new VehicleMovil();

                            fond.setId(Integer.parseInt(cursorcar.getString(0)));
                            fond.setBrand(new VehicleBrand(0,cursorcar.getString(2)));
                            fond.setCapacity(Integer.parseInt(cursorcar.getString(3)));
                            fond.setColor((cursorcar.getString(4)));
                            fond.setDescription(new VehicleDescription(0,cursorcar.getString(5)));
                            fond.setModel(new VehicleModel(0,Integer.parseInt(cursorcar.getString(6))));
                            fond.setPlates(cursorcar.getString(7));
                            fond.setType(new VehicleType(0,cursorcar.getString(8)));
                            fond.setSubtype(new VehicleSubtype(0,cursorcar.getString(9)));

                            carritos.add(fond);

                        }while(cursorcar.moveToNext());
                    }

                    return carritos;

                default:
                    break;
            }

            return null;
        }
    }

//================================AsynTask to Delete DB============================================
    public static class ThreadDBDelete extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String val = strings[0];
            //Log.w("Here",val);

            switch (val){
                case modelBase.FeedEntryUsuario.TABLE_NAME:

                SQLiteDatabase db = base.getWritableDatabase();
                int res = db.delete(modelBase.FeedEntryUsuario.TABLE_NAME, modelBase.FeedEntryUsuario._ID + "= ?",new String[]{strings[1]}); //Aqui va id de strings

                db.close();
                return ""+res;

                default:
                    return "";
            }

        }
    }

//================================AsynTask to readOne DB============================================
    public static class ThreadDBReadone extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... strings) {

            return null;
            /*String val = strings[0];
            Log.w("Here",val);

            //Personalizar para que retorne las columnas que quiera....
            //Usar variable strings...
            SQLiteDatabase db = base.getReadableDatabase();
            Cursor cursor = db.query(
                    modelBase.FeedEntryProyectos.TABLE_NAME,
                    new String[] {modelBase.FeedEntryProyectos._ID, modelBase.FeedEntryProyectos.COLUMN_NAME},
                    modelBase.FeedEntryProyectos._ID + "= ?",
                    new String[]{strings[1]},
                    null,
                    null,
                    null,
                    null);

            if(cursor != null)
            {
                cursor.moveToFirst();

                ProyectosDao fonda = new ProyectosDao();
                fonda.setId(cursor.getInt(0));
                fonda.setName((cursor.getString(1)));

                return fonda;
            }
            else{
                return null;
            }*/
        }
    }
}
