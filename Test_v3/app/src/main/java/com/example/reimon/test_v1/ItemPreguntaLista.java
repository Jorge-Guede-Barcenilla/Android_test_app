package com.example.reimon.test_v1;

import android.database.Cursor;

/**
 * Created by Reimon on 20/12/2014.
 */
public class ItemPreguntaLista {
    private DataBaseManager manag=DataBaseManager.getInstance(null);

    protected int ID;
    protected String pregunta;
    protected String respuesta;
    protected boolean correcto;

    ItemPreguntaLista(int id){
        ID=id;
        Cursor cursor= manag.cargarCursorEjercicios();
        cursor.moveToFirst();
        cursor.moveToPosition(id);
        pregunta=cursor.getString(1);
        respuesta=cursor.getString(cursor.getInt(7));
        cursor.close();
        corregir(id);

    }
    private void corregir(int id_){

        Cursor cursor=manag.cargarCursorEjercicios();
        cursor.moveToFirst();
        cursor.moveToPosition(id_);
        if ((cursor.getInt(7)-1)==cursor.getInt(6)){
            correcto=true;}
        else {
            correcto = false;
        }
        cursor.close();
    }

    public int getId(){
        return ID;
    }
    public String getPregunta(){
        return pregunta;
    }
    public String getRespuesta(){
        return respuesta;
    }
    public boolean getCorrecto(){
        return correcto;
    }

}
