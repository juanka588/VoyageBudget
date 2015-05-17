package voyage.unal.com.voyagebudget;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import voyage.unal.com.voyagebudget.LN.LinnaeusDatabase;
import voyage.unal.com.voyagebudget.LN.Util;


public class DetallesActivity extends ActionBarActivity {

    private TextView web;
    private ImageView im;
    private RatingBar rt;
    private URL imageUrl = null;
    private HttpURLConnection conn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);
        web = (TextView) findViewById(R.id.textWebPage);
        im = (ImageView) findViewById(R.id.imageDetalles);
        rt = (RatingBar) findViewById(R.id.calificacionDetalles);
        Bundle b = getIntent().getExtras();
        String cad = b.getString("titulo");
        LinnaeusDatabase lb = new LinnaeusDatabase(getApplicationContext());
        SQLiteDatabase db = openOrCreateDatabase(LinnaeusDatabase.DATABASE_NAME,
                MODE_WORLD_READABLE, null);
        String query = "select nombre, descripcion,costo,prioridad, sitio_web,tiempo," +
                "imagen,calificacion from nodo";

        Cursor c = db.rawQuery(query, null);
        String[][] mat = Util.imprimirLista(c);
        c.close();
        db.close();
        int ids[] = new int[]{R.id.textNombre, R.id.textDescripcion, R.id.textPrecio, R.id.textPrioridad,
                R.id.textWebPage, R.id.textTiempo};
        for (int i = 0; i < mat.length; i++) {
            TextView tx = (TextView) findViewById(ids[i]);
            tx.setText(mat[0][i]);
        }
        rt.setNumStars(5);
        rt.setRating(Float.parseFloat(mat[0][7]));
        rt.setEnabled(false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            loadImageFromNetwork(mat[0][6]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        c = null;
        db = null;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                return loadImageFromNetwork(urls[0]);
            } catch (Exception e) {
                return null;
            }
        }

        protected void onPostExecute(Bitmap result) {
            im.setImageBitmap(result);
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
