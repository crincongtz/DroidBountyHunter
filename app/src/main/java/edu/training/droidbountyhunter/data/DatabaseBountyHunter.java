package edu.training.droidbountyhunter.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import edu.training.droidbountyhunter.models.Fugitive;

public class DatabaseBountyHunter {
    private static String TAG = DatabaseBountyHunter.class.getSimpleName();
    /** ------------------- Nombre de Base de Datos --------------------------**/
    private static final String DataBaseName = "DroidBountyHunterDataBase";
    /** ------------------ Versión de Base de Datos --------------------------**/
    private static final int version = 3;
    /** ---------------------- Tablas y Campos -------------------------------**/
    private static final String TABLE_NAME = "fugitives";
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_NAME = "name";
    private static final String COLUMN_NAME_STATUS = "status";
    private static final String COLUMN_NAME_PHOTO = "photo";
    private static final String COLUMN_NAME_LATITUDE = "latitude";
    private static final String COLUMN_NAME_LONGITUDE = "longitude";
 /** ------------------- Declaración de Tablas ----------------------------**/
    private static final String TFugitives = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_NAME_ID + " INTEGER PRIMARY KEY NOT NULL, " +
            COLUMN_NAME_NAME + " TEXT NOT NULL, " +
            COLUMN_NAME_STATUS + " INTEGER, " +
            COLUMN_NAME_PHOTO + " TEXT, " +
            COLUMN_NAME_LATITUDE + " TEXT, " +
            COLUMN_NAME_LONGITUDE + " TEXT, " +
            "UNIQUE (" + COLUMN_NAME_NAME + ") ON CONFLICT REPLACE);";
    /** ---------------------- Variables y Helpers ---------------------------**/
    private DBHelper helper;
    private SQLiteDatabase database;
    private Context context;

    public DatabaseBountyHunter(Context context){
        this.context = context;
    }

    public DatabaseBountyHunter open(){
        helper = new DBHelper(context);
        database = helper.getWritableDatabase();
        return this;
    }

    public void close(){
        helper.close();
        database.close();
    }

    private Cursor querySQL(String sql, String[] selectionArgs){
        Cursor cursor = null;
        open();
        cursor = database.rawQuery(sql, selectionArgs);
        return cursor;
    }

    public ArrayList<Fugitive> GetFugitives(boolean isCaptured){
        ArrayList<Fugitive> fugitives = new ArrayList<>();
        String stringCaptured = isCaptured ? "1" : "0";
        Cursor dataCursor = querySQL("SELECT * FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_NAME_STATUS + "= ? ORDER BY " + COLUMN_NAME_NAME,
                new String[]{stringCaptured});
        if (dataCursor != null && dataCursor.getCount() > 0){
            for (dataCursor.moveToFirst() ; !dataCursor.isAfterLast() ;
                 dataCursor.moveToNext()){
                int id = dataCursor.getInt(dataCursor.getColumnIndex(COLUMN_NAME_ID));
                String name = dataCursor
                        .getString(dataCursor.getColumnIndex(COLUMN_NAME_NAME));
                String status = dataCursor
                        .getString(dataCursor.getColumnIndex(COLUMN_NAME_STATUS));
                String photo = dataCursor
                        .getString(dataCursor.getColumnIndex(COLUMN_NAME_PHOTO));
                String latitude = dataCursor
                        .getString(dataCursor.getColumnIndex(COLUMN_NAME_LATITUDE));
                String longitude = dataCursor
                        .getString(dataCursor.getColumnIndex(COLUMN_NAME_LONGITUDE));
                fugitives.add(new Fugitive(id, name, status, photo,
                        Double.parseDouble(latitude),
                        Double.parseDouble(longitude)));
            }
        }
        close();
        return fugitives;
    }


    public void InsertFugitive(Fugitive fugitive){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_NAME, fugitive.getName());
        values.put(COLUMN_NAME_STATUS, fugitive.getStatus());
        values.put(COLUMN_NAME_PHOTO, fugitive.getPhoto());
        values.put(COLUMN_NAME_LATITUDE, fugitive.getLatitude());
        values.put(COLUMN_NAME_LONGITUDE, fugitive.getLongitude());
        open();
        database.insert(TABLE_NAME,null, values);
        close();
    }

    public void UpdateFugitive(Fugitive fugitive){
        open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_NAME, fugitive.getName());
        values.put(COLUMN_NAME_STATUS, fugitive.getStatus());
        values.put(COLUMN_NAME_PHOTO, fugitive.getPhoto());
        values.put(COLUMN_NAME_LATITUDE, fugitive.getLatitude());
        values.put(COLUMN_NAME_LONGITUDE, fugitive.getLongitude());
        database.update(TABLE_NAME,values,COLUMN_NAME_NAME + "=?",new
                String[]{fugitive.getName()});
        close();
    }

    public void DeleteFugitive(int idFugitive){
        open();
        database.delete(TABLE_NAME, COLUMN_NAME_ID + "=?", new String[]{String.valueOf(idFugitive)});
        close();
    }



    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DataBaseName, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "Creación de la base de datos");
            db.execSQL(TFugitives);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Actualización de la BDD de la versión " + oldVersion + "a la " +
                    + newVersion + ", de la que se destruirá la información anterior");

            // Destruir BDD anterior y crearla nuevamente las tablas actualizadas
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            // Re-creando nuevamente la BDD actualizada
            onCreate(db);
        }
    }


}

