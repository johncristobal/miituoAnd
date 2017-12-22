package miituo.com.miituo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by john_vera on 26/12/16.
 *
 * Create the tables and all the queries to add,update,delete data....
 *
 */
public class modelBase extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public Context contex;
    //public static final int DATABASE_VERSION = 22;
    public static final String DATABASE_NAME = "miituomodel.db";

    /* Inner class that defines the table contents */
    public static class FeedEntryUsuario implements BaseColumns {
        public static final String TABLE_NAME = "usuario";
        public static final String COLUMN_NAME = "nombre";
        public static final String COLUMN_MOTHERNAME = "apmaterno";
        public static final String COLUMN_LASTNAME = "appaterno";
        //public static final String COLUMN_UBI = "ubicacion";
        public static final String COLUMN_CELPHONE = "celular";
        public static final String COLUMN_TOKEN = "token";
    }

    /* Inner class that defines the table contents */
    public static class FeedEntryPoliza implements BaseColumns {
        public static final String TABLE_NAME = "poliza";
        public static final String COLUMN_IDUSER = "iduser";
        public static final String COLUMN_INSURANCE = "insurance";
        public static final String COLUMN_LASTODOMETER = "lastodometer";
        public static final String COLUMN_NOPOLICY = "nopolicy";
        public static final String COLUMN_ODOMETERPIE = "odometerpie";
        public static final String COLUMN_VEHICLEPIE = "vehiclepie";
        public static final String COLUMN_REPORT_STATE = "report_state";
        public static final String COLUMN_RATE = "rate";
        public static final String COLUMN_LIMIT_DATE = "fechalimite";
        public static final String COLUMN_PAYMENT = "payment";
    }

    public static class FeedEntryVehiculo implements BaseColumns {
        public static final String TABLE_NAME = "vehiculo";
        public static final String COLUMN_POLIZA = "id_poliza";
        public static final String COLUMN_BRAND = "brand";
        public static final String COLUMN_CAPACITY = "capacity";
        public static final String COLUMN_COLOR = "color";
        public static final String COLUMN_DESCRIPTION = "descripcion";
        public static final String COLUMN_MODEL = "model";
        public static final String COLUMN_PLATES = "plates";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_SUBTYPE = "subtype";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String DATE_TYPE = " DATE";
    private static final String INT_TYPE = " INTEGER";

    private static final String SQL_CREATE_ENTRIESUsuario =
            "CREATE TABLE " + FeedEntryUsuario.TABLE_NAME + " (" +
                    FeedEntryUsuario._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    FeedEntryUsuario.COLUMN_NAME + TEXT_TYPE + "," +
                    FeedEntryUsuario.COLUMN_MOTHERNAME + TEXT_TYPE + "," +
                    //FeedEntryUsuario.COLUMN_UBI + TEXT_TYPE + "," +
                    FeedEntryUsuario.COLUMN_LASTNAME+ TEXT_TYPE + "," +
                    FeedEntryUsuario.COLUMN_CELPHONE + TEXT_TYPE + "," +
                    FeedEntryUsuario.COLUMN_TOKEN + TEXT_TYPE + " )";

    private static final String SQL_CREATE_ENTRIESPoliza =
            "CREATE TABLE " + FeedEntryPoliza.TABLE_NAME + " (" +
                    FeedEntryPoliza._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    FeedEntryPoliza.COLUMN_IDUSER + TEXT_TYPE + "," +
                    FeedEntryPoliza.COLUMN_INSURANCE + TEXT_TYPE + "," +
                    FeedEntryPoliza.COLUMN_LASTODOMETER + TEXT_TYPE + "," +
                    FeedEntryPoliza.COLUMN_NOPOLICY + TEXT_TYPE + "," +
                    FeedEntryPoliza.COLUMN_ODOMETERPIE + TEXT_TYPE + "," +
                    FeedEntryPoliza.COLUMN_VEHICLEPIE + TEXT_TYPE + "," +
                    FeedEntryPoliza.COLUMN_REPORT_STATE + TEXT_TYPE + "," +
                    FeedEntryPoliza.COLUMN_RATE + TEXT_TYPE + "," +
                    FeedEntryPoliza.COLUMN_PAYMENT + TEXT_TYPE + "," +
                    FeedEntryPoliza.COLUMN_LIMIT_DATE + TEXT_TYPE + " )";

    private static final String SQL_CREATE_ENTRIESVehiculo =
            "CREATE TABLE " + FeedEntryVehiculo.TABLE_NAME + " (" +
                    FeedEntryVehiculo._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    FeedEntryVehiculo.COLUMN_POLIZA + TEXT_TYPE + "," +
                    FeedEntryVehiculo.COLUMN_BRAND + TEXT_TYPE + "," +
                    FeedEntryVehiculo.COLUMN_CAPACITY + TEXT_TYPE + "," +
                    FeedEntryVehiculo.COLUMN_COLOR + TEXT_TYPE + "," +
                    FeedEntryVehiculo.COLUMN_DESCRIPTION + TEXT_TYPE + "," +
                    FeedEntryVehiculo.COLUMN_MODEL + TEXT_TYPE + "," +
                    FeedEntryVehiculo.COLUMN_PLATES + TEXT_TYPE + "," +
                    FeedEntryVehiculo.COLUMN_TYPE + TEXT_TYPE + "," +
                    FeedEntryVehiculo.COLUMN_SUBTYPE + TEXT_TYPE +")";

    private static final String SQL_DELETE_ENTRIESUsuario =
            "DROP TABLE IF EXISTS " + FeedEntryUsuario.TABLE_NAME;
    private static final String SQL_DELETE_ENTRIESPoliza =
            "DROP TABLE IF EXISTS " + FeedEntryPoliza.TABLE_NAME;
    private static final String SQL_DELETE_ENTRIESVehiculo =
            "DROP TABLE IF EXISTS " + FeedEntryVehiculo.TABLE_NAME;

    public modelBase(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIESUsuario);
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIESPoliza);
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIESVehiculo);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIESUsuario);
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIESPoliza);
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIESVehiculo);
        onCreate(sqLiteDatabase);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void eraseDB(){
    }
}
