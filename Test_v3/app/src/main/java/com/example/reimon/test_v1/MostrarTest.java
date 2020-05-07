package com.example.reimon.test_v1;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class MostrarTest extends ActionBarActivity {

    private DataBaseManager manag;
    private int contador=0;//Para contar en numero de preguntas
    //Lista de posiciones
    private List<Integer> list = new LinkedList();
    //Lista de 10 posiciones registradas del test, ojo no son los Ids de las preguntas, son los indices de posicion del cursor
    private int[] id_pregunta={0,0,0,0,0,0,0,0,0,0};
    private  Random aleatorio= new Random();


    protected TextView enunciado;
    protected RadioButton checkAnswer1;
    protected RadioButton checkAnswer2;
    protected RadioButton checkAnswer3;
    protected RadioButton checkAnswer4;
    protected Button nextButton;
    protected Button backButton;
    protected TextView textcont;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_test);
        //Cogemos los datos de la serie creada en la anterior activity
        Intent intento = getIntent();
        list = intento.getIntegerArrayListExtra("serie");



        enunciado = (TextView) findViewById(R.id.Enunciado);
        checkAnswer1 = (RadioButton) findViewById(R.id.radioButton1);
        checkAnswer2 = (RadioButton) findViewById(R.id.radioButton2);
        checkAnswer3 = (RadioButton) findViewById(R.id.radioButton3);
        checkAnswer4 = (RadioButton) findViewById(R.id.radioButton4);
        nextButton = (Button)findViewById(R.id.buttonNext);

        backButton = (Button)findViewById(R.id.buttonBack);
        textcont = (TextView) findViewById(R.id.TextCont);


        manag=DataBaseManager.getInstance(this);

        new insertarContenido().execute(list.get(contador));
        textcont.setText((contador+1)+"/10");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mostrar_test, menu);
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

    public void cambiarPregunta (View view) {
        int respuesta = 0; //Partimos de que nada se ha respondido


        //Si alguna de las respuestas esta marcada...
        if (checkAnswer1.isChecked() || checkAnswer2.isChecked() || checkAnswer3.isChecked() || checkAnswer4.isChecked()) {
            //Comprobamos cual esta marcada
            if (checkAnswer1.isChecked())
                respuesta = 2;
            if (checkAnswer2.isChecked())
                respuesta = 3;
            if (checkAnswer3.isChecked())
                respuesta = 4;
            if (checkAnswer4.isChecked())
                respuesta = 5;
            //Registramos cual ha sido respondida, dentro de esta clase tambien se genera el contenido de la siguiente pregugunta
            new registrarContenido().execute(id_pregunta[contador], respuesta);

         }

        else{
            AlertDialog.Builder alerta = new AlertDialog.Builder(this);
            alerta.setMessage("Debe seleccionar una respuesta");
            alerta.show();
        }

    }

    public void anteriorPregunta(View view){
        if (contador==0){
            Intent pantallaInicio = new Intent(this, MainActivity.class);
            startActivity(pantallaInicio);
        } else {

            --contador;
            textcont.setText(contador+"/10");
            new insertarContenido().execute(list.get(contador));

        }

    }


    public class insertarContenido extends AsyncTask<Integer, Integer, String[]> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String[] doInBackground(Integer... params) {

            Cursor cursor = manag.cargarCursorEjercicios();
            cursor.moveToFirst();
            cursor.moveToPosition(params[0].intValue());


             id_pregunta[contador]=params[0];


                String enun = cursor.getString(1);
                String respuesta1 = cursor.getString(2);
                String respuesta2 = cursor.getString(3);
                String respuesta3 = cursor.getString(4);
                String respuesta4 = cursor.getString(5);
                cursor.close();


             String[] test = {enun, respuesta1, respuesta2, respuesta3, respuesta4};

            return test;

        }

        @Override
        protected void onPostExecute(String[] result) {

            enunciado.setText(result[0].toString());
            checkAnswer1.setText(result[1].toString());
            checkAnswer1.setChecked(false);
            checkAnswer2.setText(result[2].toString());
            checkAnswer2.setChecked(false);
            checkAnswer3.setText(result[3].toString());
            checkAnswer3.setChecked(false);
            checkAnswer4.setText(result[4].toString());
            checkAnswer4.setChecked(false);


        }


    }

    public class registrarContenido extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {


            //Al parametro primero, es decir, el ID, le sumamos uno pues el cursor empieza en 0 pero el ID en 1
            manag.actualizarCheckedAnswer(Integer.toString(params[0] + 1), Integer.toString(params[1]));
            Cursor cursor=manag.cargarCursorEjercicios();
            cursor.moveToPosition(params[0]);
            //Almacenamos el acierto o el error
            //Resto uno al argumento para que se corresponda con el verdadero numero de respuesta, misma referencia que la columna checkedAnswer
            if(cursor.getInt(6)==(params[1]-1)) {

                int nCorrect = cursor.getInt(8);
                ++nCorrect;
                manag.actualizar_nCorrect(Integer.toString(params[0]+1),Integer.toString(nCorrect));
            }
            else{
                int nIncorrect=cursor.getInt(9);
                ++nIncorrect;
                manag.actualizar_nICorrect(Integer.toString(params[0]+1),Integer.toString(nIncorrect));

            }
            cursor.close();

            return null;

        }

        @Override
        protected void onPostExecute(Integer result) {
            //Si es la ultima pregunta pasamos a la actividad 'MostrarLista'
            if (contador==9){

                Intent mostrarLista = new Intent(getApplicationContext(), MostrarLista.class);
                mostrarLista.putExtra("numbers", id_pregunta);
                startActivity(mostrarLista);
            }
            //Si no es la ultima pregunta
            else {
                //Comprobamos la antepenultima pregunta para en la ultima cambiar al boton 'Final'

                if (contador == 8)
                    nextButton.setText("Final");
               else
                    nextButton.setText("Siguiente");

                ++contador;
                textcont.setText((contador+1) + "/10");
                new insertarContenido().execute(list.get(contador));

            }

        }

    }


}
