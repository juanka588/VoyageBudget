package voyage.unal.com.voyagebudget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import voyage.unal.com.voyagebudget.LN.Node;
import voyage.unal.com.voyagebudget.LN.Util;


public class RutaActivity extends ActionBarActivity {

    private GoogleMap mapa;
    private ArrayList<LatLng> marcadores = new ArrayList<LatLng>();
    private ArrayList<Node> pathOrder;
    double latitud;
    double longitud;
    private LatLng posIni;
    private String[][] mat;
    private PolylineOptions polyLine;
    private ArrayList<String> rows;

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

    private void drawLine(LatLng ini, LatLng fin) {
        polyLine.add(ini);
        polyLine.add(fin);
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
        Bundle b=getIntent().getExtras();
        rows = b.getStringArrayList("rows");
        final String[][] mat= Util.toMatrix(rows);
        LatLng ini=null;
        LatLng fin=null;
        for(int i=0;i<mat.length;i++){
            ini=new LatLng(Double.parseDouble(mat[i][1]),Double.parseDouble(mat[i][2]));
            fin=new LatLng(Double.parseDouble(mat[i][1]),Double.parseDouble(mat[i][2]));
            drawLine(ini,fin);
        }
        /*for(Node n:pathOrder){
            drawLine();
        }*/
        drawPolilyne(polyLine);
    }

    public void pasos(View v) {
        Intent steps = new Intent(this, StepsActivity.class);
        startActivity(steps);
    }
}
