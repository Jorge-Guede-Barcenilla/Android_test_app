package com.example.reimon.test_v1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Reimon on 12/12/2014.
 */
//Clase encargada de crear la base de datos

public class DbHelper extends SQLiteOpenHelper {

    private static DbHelper sInstance;
    private static final String DB_NAME="Base1.sqlite"; //Nombre de la base de datos
    private static final int DB_SCHEME_VERSION = 1; //Version del esquema(o estructura) de la base de datos


	/*  'CursorFactory' es una clase que
	 * extiende la clase cursor y se implementar
	 * validaciones	extra u operaciones sobre la
	 * base de datos, normalmente esta en NULL*/

	/*'version' es la version del esquema*/

    public static DbHelper getInstance(Context context){
        // Usamos el contexto de la aplicación para asegurarnos
        //de que no perdemos accidentalmente el contexto de la actividad


        if (sInstance == null) {
            sInstance = new DbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DbHelper(Context context) {

        //'super' llama a la superclase SQLiteOpenHelper
        super(context, DB_NAME, null, DB_SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //Metodo para crear nuestra tabla


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }


}

