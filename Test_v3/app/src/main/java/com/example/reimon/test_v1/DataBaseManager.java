package com.example.reimon.test_v1;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Reimon on 12/12/2014.
 */
public class DataBaseManager {
    //Nombre de la tabla
    public static final String TABLE_NAME="Ejercicios";
    //Nombre de las columnas
    public static final String CN_ID="_id";
    public static final String CN_QUESTION="Question";
    public static final String CN_ANSWER1="Answer1";
    public static final String CN_ANSWER2="Answer2";
    public static final String CN_ANSWER3="Answer3";
    public static final String CN_ANSWER4="Answer4";
    public static final String CN_CORRECT_ANSWER="CorrectAnswer";
    public static final String CN_CHECKED_ANSWER="CheckedAnswer";
    public static final String CN_N_CORRECT="nCorrect";
    public static final String CN_N_INCORRECT="nIncorrect";



    private static DataBaseManager DBManag; //Para crear una clase 'Global' Compartida por varias actividades

    //Comando en lenguaje SQL para crear tabla: create table Ejercicios (_id integer primary key autoincrement, question text not null, answer1 text,answer2 text);

    public static final String CREATE_TABLE= " CREATE TABLE IF NOT EXISTS " + TABLE_NAME+ " ("
            + CN_ID + " integer primary key autoincrement, "
            + CN_QUESTION + " text not null,"
            + CN_ANSWER1 + " text not null,"
            + CN_ANSWER2 + " text not null,"
            + CN_ANSWER3 + " text not null,"
            + CN_ANSWER4 + " text not null,"
            + CN_CORRECT_ANSWER + " text not null,"
            + CN_CHECKED_ANSWER + " text,"
            + CN_N_CORRECT + " text,"
            + CN_N_INCORRECT + " text);" ;

    private static DbHelper helper;
    private static SQLiteDatabase db1;
    private static InsertarDb importar_Db;

    public static DataBaseManager getInstance(Context context){

        if (DBManag==null){
            DBManag=new DataBaseManager(context);
        }
        return DBManag;
    }

    private DataBaseManager(Context context){
        //Obtenemos la unica instancia 'Global' que existe para evitar posibles errores
        helper=DbHelper.getInstance(context);
        //Create and/or open a database that will be used for reading and writing.
        db1= helper.getWritableDatabase();

        //Se vuelve a crear la estructura de ejercicios para su posterior actualizacion de datos de insercion
        db1.execSQL(DataBaseManager.CREATE_TABLE);
        //Se insertan los nuevos datos
        importar_Db = new InsertarDb(db1,context);
        importar_Db.execute();

    }

    private ContentValues generarContentValues(String columname,String c_answer){
        ContentValues valores=new ContentValues();
        valores.put(columname,c_answer);

        return valores;


    }


    public void eliminar(int _id){
        //DELETE FROM <nombreTabla> WHERE id = '4';
        db1.execSQL(" DELETE FROM " + TABLE_NAME + "WHERE" + CN_ID + "=" + _id);

    }

    public void actualizarCheckedAnswer(String _id,String checked_answer){
        String[] argumentos={_id};
        db1.update(TABLE_NAME,generarContentValues(CN_CHECKED_ANSWER,checked_answer), "_id=?",argumentos);
      }
    public void actualizar_nCorrect (String _id,String i){
        String[] argumentos={_id};
        db1.update(TABLE_NAME,generarContentValues(CN_N_CORRECT,i),"_id=?",argumentos);

    }
    public void actualizar_nICorrect (String _id,String i){
        String[] argumentos={_id};
        db1.update(TABLE_NAME,generarContentValues(CN_N_INCORRECT,i),"_id=?",argumentos);

    }


    public Cursor cargarCursorEjercicios(){

        //Estos son los campos que queremos que nos devuelva
        String [] columnas= new String [] {CN_ID,CN_QUESTION,CN_ANSWER1,CN_ANSWER2,CN_ANSWER3,CN_ANSWER4,CN_CORRECT_ANSWER,CN_CHECKED_ANSWER,CN_N_CORRECT,CN_N_INCORRECT};


        return db1.query(TABLE_NAME,columnas,null,null,null,null,null,null);

    }

    //Clase anidada para precargar asincronamente los datos en la tabla

    public class InsertarDb extends AsyncTask<String,Integer,Void> {
        SQLiteDatabase db;
        InputStream is=null;
        Context mContext;
        //private Integer lNumeroLineas = 0;
        private Integer lineasAcumuladas = 0;
        private String line;
        private ProgressDialog dialog;

        public InsertarDb(SQLiteDatabase db1,Context cont){
            db=db1;
            mContext=cont;

        }

        @Override protected void onPreExecute() {

            super.onPreExecute();

            dialog = new ProgressDialog(mContext,3);
            dialog.setProgressStyle(1);
            dialog.setMessage("Importando Cuestionario...");
            dialog.setMax(38);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            InputStream is = null;
            try{
                is=mContext.getAssets().open("import.sql");


                if(is!=null){
                    BufferedReader reader= new BufferedReader(new InputStreamReader(is));
                    //Abrimos una transaccion entre android y la base sql para poder editar la base de datos
                    db.beginTransaction();
                    //Almacenamos la primera linea almacenada en el StringBuffer
                    line = reader.readLine();

                    while (!TextUtils.isEmpty(line)){
                        lineasAcumuladas=lineasAcumuladas+1;
                        db.execSQL(line);
                        publishProgress ((int)lineasAcumuladas);
                        line=reader.readLine();
                    }
                    db.setTransactionSuccessful();

                }

            } catch (Exception ex){
                Log.e("Error: ", ex.getMessage());
            }finally {
                db.endTransaction();
                if(is!=null){
                    try {
                        is.close();
                    } catch (IOException e){
                        Log.e("Error: ",e.getMessage());
                    }
                }
            }
            return null;


        }

        protected void onProgressUpdate(Integer... progress) {
            dialog.setProgress((int)progress[0]);

        }

        @Override protected void onPostExecute(Void result) {

            dialog.dismiss();
        }



    }




}
