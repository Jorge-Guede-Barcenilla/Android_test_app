package com.example.reimon.test_v1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Reimon on 20/12/2014.
 */
public class ItemPreguntaLista_Adapter extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<ItemPreguntaLista> items;

    public ItemPreguntaLista_Adapter(Activity activity, ArrayList<ItemPreguntaLista> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position).getId();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        View vi=contentView;

        if(contentView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.row_item, null);
        }

        ItemPreguntaLista item = items.get(position);


        TextView enunciado = (TextView) vi.findViewById(R.id.pregunta);
        enunciado.setText(item.getPregunta());

        TextView respuesta = (TextView) vi.findViewById(R.id.respuesta1);
        respuesta.setText(item.getRespuesta());

        ImageView imageOk = (ImageView) vi.findViewById(R.id.icon_ok);
        ImageView imageWrong = (ImageView) vi.findViewById(R.id.icon_wrong);

        if(item.getCorrecto()){
            imageOk.setVisibility(View.VISIBLE);
            imageWrong.setVisibility(View.INVISIBLE);
       }
       else{
            imageOk.setVisibility(View.INVISIBLE);
            imageWrong.setVisibility(View.VISIBLE);
        }





        return vi;
    }

}
