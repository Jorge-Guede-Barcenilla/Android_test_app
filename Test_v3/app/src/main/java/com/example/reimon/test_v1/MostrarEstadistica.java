package com.example.reimon.test_v1;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MostrarEstadistica extends ActionBarActivity {

    private DataBaseManager manag=DataBaseManager.getInstance(this);
    private TextView resultadoGlobal;
    private TextView nAciertos;
    private TextView nFallos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_estadistica);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mostrar_estadistica, menu);
        resultadoGlobal=(TextView)findViewById(R.id.Num_estadistica);
        nAciertos=(TextView)findViewById(R.id.Num_aciertos);
        nFallos=(TextView)findViewById(R.id.Num_fallos);

        new ComprobarProgreso_BorrarProgreso().execute(0);

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





    public class ComprobarProgreso_BorrarProgreso extends AsyncTask<Integer,Integer,Integer[]>{


        @Override protected void onPreExecute() {
            resultadoGlobal.setText("Calculando...");
        }

        @Override
        protected Integer[] doInBackground(Integer... params) {

            if (params[0]==0) {
                Integer resultado[] ={0,0,0}; //Corresponde respectivamente: porcentaje,num_aciertos,num_fallos
                Cursor cursor = manag.cargarCursorEjercicios();
                //Es verdad que los indices van de 1 al 59, pero la posicion del cursor cuenta con el 0
                //Luego entonces iría de 0 a 58
                for (int i = 0; i < 58; i++) {
                    cursor.moveToPosition(i);
                    if (cursor.getInt(8)>0) {
                        ++resultado[0];
                    }
                    resultado[1]+=cursor.getInt(8);
                    resultado[2]+=cursor.getInt(9);
                }
                cursor.close();
                return resultado;
            }

            else{
                for(int i=1;i<59;i++){
                    manag.actualizar_nCorrect(Integer.toString(i),"0");
                    manag.actualizar_nICorrect(Integer.toString(i),"0");

                }
                Integer[] zero={0,0,0};
                return zero;
            }


        }

        @Override
        protected void onPostExecute(Integer[] result) {

            if (result[0]<=0)
                resultadoGlobal.setText( "0%");
            else{
                double porcentaje=(float)result[0]/58;
                resultadoGlobal.setText( String.format("%.3f",porcentaje)+" %");
            }

            nAciertos.setText(Integer.toString(result[1]));
            nFallos.setText(Integer.toString(result[2]));


        }
    }


    public void volverAndroidQuiz(View view){
        Intent intento=new Intent(this,MainActivity.class);
        startActivity(intento);
    }

    public void borrarResutados(View view){

        new ComprobarProgreso_BorrarProgreso().execute(1);

    }




}
