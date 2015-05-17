package voyage.unal.com.voyagebudget;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import voyage.unal.com.voyagebudget.LN.Util;


public class DetallesActivity extends ActionBarActivity {

    private TextView titulo;
    private TextView descripcion;
    private TextView costo;
    private TextView prioridad;
    private TextView tiempo;
    private TextView web;
    private ImageView im;
    private RatingBar rt;
    private URL imageUrl = null;
    private HttpURLConnection conn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);
        titulo = (TextView) findViewById(R.id.textNombre);
        descripcion = (TextView) findViewById(R.id.textDescripcion);
        costo = (TextView) findViewById(R.id.textPrecio);
        prioridad = (TextView) findViewById(R.id.textPrioridad);
        tiempo = (TextView) findViewById(R.id.textTiempo);
        web = (TextView) findViewById(R.id.textWebPage);
        im = (ImageView) findViewById(R.id.imageDetalles);
        rt = (RatingBar) findViewById(R.id.calificacionDetalles);
        Bundle b = getIntent().getExtras();
        String cad = b.getString("titulo");
        String cad2 = b.getString("descripcion");
        Double cad3 = b.getDouble("costo");
        Integer cad4 = b.getInt("prioridad");
        String cad5 = b.getString("sitio_web");
        Double cad6 = b.getDouble("tiempo");
        String cad7 = b.getString("imagen");
        Integer cad8 = b.getInt("calificacion");
        titulo.setText(cad);
        descripcion.setText(cad2);
        costo.setText("$" + cad3 + " " + getString(R.string.money));
        prioridad.setText(getString(R.string.priority) + " : " + cad4);
        web.setText(cad5);
        tiempo.setText(cad6 + " " + getString(R.string.hours));
        rt.setNumStars(5);
        rt.setRating((float) cad8);
        rt.setEnabled(false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        new DownloadImageTask(im).execute(cad7);
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                mIcon11 = BitmapFactory.decodeStream(in, new Rect(20, 20, 20, 20), options);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    private Bitmap loadImageFromNetwork(String url) throws IOException {
        imageUrl = new URL(url);
        conn = (HttpURLConnection) imageUrl.openConnection();
        conn.connect();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2

        Bitmap imagen = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);
        im.setImageBitmap(imagen);
        Log.e("imagen cargada", "");
        return imagen;
    }

    public void web(View v) {
        String cad = web.getText().toString();
        Util.irA(cad, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalles, menu);
        return true;
    }
}
