package voyage.unal.com.voyagebudget;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import voyage.unal.com.voyagebudget.LN.Node;
import voyage.unal.com.voyagebudget.LN.Step;
import voyage.unal.com.voyagebudget.LN.Travel;
import voyage.unal.com.voyagebudget.LN.Util;


public class RutaActivity extends ActionBarActivity {

    private GoogleMap mapa;
    private ArrayList<LatLng> marcadores = new ArrayList<LatLng>();
    private ArrayList<Step> pathOrder;
    private ArrayList<Node> nodos;
    double latitud;
    double longitud;
    private LatLng posIni;
    private String[][] mat;
    private PolylineOptions polyLine;
    private ArrayList<String> rows;
    private double time;
    private double budget;
    private Node current;
    private boolean recargable;
    private JSONObject jsonArray;
    private String readTwitterFeed;
    private ArrayList<LatLng> detailStep;

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mapa} is not null.
     * <p/>
     * If it isn't installed {@link com.google.android.gms.maps.SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mapa == null) {
            // Try to obtain the map from the SupportMapFragment.
            mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapRuta))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mapa != null) {
                setUpMap();
            }
        }
    }

    private void drawPolilyne(PolylineOptions options) {
        Polyline polyline = mapa.addPolyline(options);
    }

    private void drawLine(Node ini, Node fin) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        URL url;
        try {
            url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + ini.x + "," +
                    ini.y + "&destination=" + fin.x + "," + fin.y + "&sensor=true");
            new EventFetch().execute(url);
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        LatLng a = new LatLng(ini.x, ini.y);
        LatLng b = new LatLng(fin.x, fin.y);
        drawLine(a, b);
    }

    private void drawLine(LatLng ini, LatLng fin) {
        Random rnd = new Random();
        int r = rnd.nextInt(8);
        int color = Color.RED;
        switch (r) {
            case 0:
                color = Color.BLUE;
                break;
            case 1:
                color = Color.GREEN;
                break;
            case 2:
                color = Color.CYAN;
                break;
            case 3:
                color = Color.MAGENTA;
                break;
            case 4:
                color = Color.BLACK;
                break;
            case 5:
                color = Color.YELLOW;
                break;
            case 6:
                color = Color.GRAY;
                break;
        }
        polyLine.add(ini).color(color).width(5).geodesic(true);
        polyLine.add(fin).color(color).width(5).geodesic(true);
    }

    public void recargarLista() {
        Log.e("recargar","intentando");
        try {
            jsonArray = new JSONObject(readTwitterFeed);
            JSONArray rutas= (JSONArray) jsonArray.get("routes");
            JSONObject bounds=  rutas.getJSONObject(0);
            JSONObject steps= (JSONObject) bounds.get("steps");
            Log.e("rutas",steps.toString());

        } catch (Exception e) {
            Log.e("Error Eventos", e.toString());
        }
    }

    private class EventFetch extends AsyncTask<URL, Integer, String> {

        @Override
        protected String doInBackground(URL... params) {
            // TODO Auto-generated method stub
            detailStep=new ArrayList<>();
            readTwitterFeed = readJSONFeed(params[0]);
            recargable = true;
            recargarLista();
            return readTwitterFeed;
        }

        protected void onPostExecute(Long result) {
            recargarLista();
        }

        public String readJSONFeed(URL params) {
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(params.toString());
            try {
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "No se ha accedido al servidor de Eventos", Toast.LENGTH_SHORT)
                            .show();
                    Log.e(RutaActivity.class.toString(),
                            "Failed to download file");
                }

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return builder.toString();
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mapa} is not null.
     */
    private void setUpMap() {
        mapa.setMyLocationEnabled(true);
        mapa.getUiSettings().setZoomControlsEnabled(true);
        polyLine = new PolylineOptions();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta);
        setUpMapIfNeeded();
        Bundle b = getIntent().getExtras();
        rows = b.getStringArrayList("rows");
        nodos = new ArrayList<>();
        final String[][] mat = Util.toMatrix(rows);
        Node n = null;
        LatLng pos = null;
        for (int i = 0; i < mat.length; i++) {
            n = new Node(mat[i][0], mat[i][1], mat[i][2], mat[i][7], mat[i][3], mat[i][4], mat[i][5]);
            nodos.add(n);
            pos = new LatLng(n.x, n.y);
            Util.mostrarMarcador(n.x, n.y, n.name, mat[i][10], 2, marcadores, mapa);
            marcadores.add(pos);
        }
        Travel t = new Travel();
        budget = b.getDouble("presupuesto");
        time = b.getDouble("tiempo");
        latitud = b.getDouble("latitud");
        longitud = b.getDouble("longitud");
        Util.animarCamara(latitud, longitud, 14, mapa);
        current = new Node(0, latitud, longitud, "Mi ubicacion", 0, 0, 0);
        pathOrder = t.createPath(current, nodos, budget, time);
        /*Log.e("bud", budget + "");
        Log.e("time", time + "");
        Log.e("lat", latitud + "");
        Log.e("lon", longitud + "");
        Log.e("size Path", pathOrder.size() + "");
        */
        for (int i = 0; i < pathOrder.size(); i++) {
            Log.e("from ", pathOrder.get(i).from.toString());
            Log.e("to ", pathOrder.get(i).to.toString());
            drawLine(pathOrder.get(i).from, pathOrder.get(i).to);
            drawPolilyne(polyLine);
            polyLine = new PolylineOptions();
        }

    }

    public void pasos(View v) {
        Intent steps = new Intent(this, StepsActivity.class);
        startActivity(steps);
    }
}
