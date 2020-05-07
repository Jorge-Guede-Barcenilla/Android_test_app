package com.example.reimon.test_v1;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataBaseManager.getInstance(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void inicioTest(View view){
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(int i=1; i<59; i++){
            list.add(i);
        }
        Collections.shuffle(list);
        Intent intent = new Intent(this, MostrarTest.class);
        intent.putExtra("serie", (java.io.Serializable) list);
        startActivity(intent);

    }
    public void abrirNavegador (View v)  {
        String url = "http://developer.android.com/index.html";
        Intent i = new Intent (Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void abrirEstadisticas(View view){
        Intent intento= new Intent (this,MostrarEstadistica.class);
        startActivity(intento);

    }
}
