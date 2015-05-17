package voyage.unal.com.voyagebudget.LN;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import voyage.unal.com.voyagebudget.DetallesActivity;
import voyage.unal.com.voyagebudget.R;

public class MiAdaptador extends BaseAdapter {
	private final Activity actividad;
	private final String[] lista;
    private final Double[] calificacion;

	public MiAdaptador(Activity actividad, ArrayList<String> titulos,ArrayList<Double> calificaciones) {
		super();
		this.actividad = actividad;
		lista = new String[titulos.size()];
		titulos.toArray(lista);
        calificacion = new Double[calificaciones.size()];
        calificaciones.toArray(calificacion);
	}

	public MiAdaptador(Activity actividad, String[] titulos,Double[] calificacion) {
		super();
		this.actividad = actividad;
		this.lista = titulos;
        this.calificacion = calificacion;
	}

    public MiAdaptador(Activity actividad, String[] titulos,double[] calificacion) {
        super();
        this.actividad = actividad;
        this.lista = titulos;
        this.calificacion=new Double[calificacion.length];
        for (int i=0;i<calificacion.length;i++){
            this.calificacion[i]=calificacion[i];
        }
    }

	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = actividad.getLayoutInflater();
		View view = inflater.inflate(R.layout.elemento_lista, null, true);
		TextView textView = (TextView) view.findViewById(R.id.rowTitle);
        RatingBar rt=(RatingBar)view.findViewById(R.id.ratingBar);
		ImageView imageView = (ImageView) view.findViewById(R.id.icono);


		if (lista[position] != null) {
			textView.setText(Util.toCammelCase(lista[position].toLowerCase()));
			textView.setHint(Util.toCammelCase(lista[position].toLowerCase()));
		}
		imageView.setImageResource(R.mipmap.ic_launcher);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detalles=new Intent(actividad,DetallesActivity.class);
                detalles.putExtra("titulo",lista[position]);
                actividad.startActivity(detalles);
            }
        });
		rt.setNumStars(5);
        rt.setEnabled(false);
        rt.setRating(Float.parseFloat(calificacion[position]+""));
		return view;
	}

	public int getCount() {
		return lista.length;
	}

	public Object getItem(int arg0) {
		return lista[arg0];
	}

	public long getItemId(int position) {
		return position;
	}
}