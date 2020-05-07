package com.example.reimon.test_v1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;


public class MostrarLista extends ActionBarActivity {

    private ListView list;
    private int [] id_pregunta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_lista);

        Intent intento = getIntent();
        id_pregunta = intento.getIntArrayExtra("numbers");
        list=(ListView)findViewById(R.id.listView1);

        new VerBD(list, this).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mostrar_lista, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void volverMenu(View v){
        Intent intento= new Intent(this,MainActivity.class);
        startActivity(intento);

    }

    public class VerBD  extends AsyncTask<String, Integer, Void> {


        private ListView list;
        private Context context;
        private SimpleCursorAdapter adapter;
        private DataBaseManager manag;
        private String respondido;
        private int contador;
        private ArrayList<ItemPreguntaLista> items = new ArrayList<ItemPreguntaLista>();

        public VerBD (ListView lista,Context cont){
            list=lista;
            context=cont;
            manag=DataBaseManager.getInstance(cont);
        }
        @Override protected void onPreExecute() {

        }

        @Override
        protected  Void doInBackground(String... params) {



            for( contador=0;contador<10;++contador){

                items.add(new ItemPreguntaLista(id_pregunta[contador]));
            }

            return null;
        }

        @Override protected void onProgressUpdate(Integer... prog) {



        }

        @Override protected void onPostExecute(Void result) {

            ArrayList<ItemPreguntaLista> Items= items;

            ItemPreguntaLista_Adapter adapter=new ItemPreguntaLista_Adapter((android.app.Activity) context,Items);
            list.setAdapter(adapter);

        }


    }
}
