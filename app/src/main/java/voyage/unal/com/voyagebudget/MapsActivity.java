package voyage.unal.com.voyagebudget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.internal.ii;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import voyage.unal.com.voyagebudget.LN.LinnaeusDatabase;
import voyage.unal.com.voyagebudget.LN.MiLocationListener;
import voyage.unal.com.voyagebudget.LN.Node;
import voyage.unal.com.voyagebudget.LN.Util;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mapa;
    private ArrayList<LatLng> marcadores = new ArrayList<LatLng>();
    double latitud;
    double longitud;
    private LatLng posIni;
    private String[][] mat;
    private ArrayList<Node> nodos;
    private EditText presupuesto,tiempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        presupuesto=(EditText)findViewById(R.id.presupuesto);
        tiempo=(EditText)findViewById(R.id.tiempo);
    }

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
     * If it isn't installed {@link SupportMapFragment} (and
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
            mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mapa != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mapa} is not null.
     */
    private void setUpMap() {
        iniciarLocalService();
        mapa.setMyLocationEnabled(true);
        mapa.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                posIni = latLng;
            }
        });
        mapa.getUiSettings().setZoomControlsEnabled(true);
        MiLocationListener.mapa = mapa;
        latitud = MiLocationListener.lat;
        longitud = MiLocationListener.longi;
        if (latitud == longitud && latitud == 0) {
        } else {
            Util.animarCamara(latitud, longitud, 15, mapa);
            Util.mostrarMarcador(latitud, longitud, "Mi ubicacion", "lat: " + (latitud + "").substring(0, 8)
                    + " lon: " + (longitud + "").substring(0, 8), 0, marcadores, mapa);
        }
        LinnaeusDatabase lb = new LinnaeusDatabase(getApplicationContext());
        SQLiteDatabase db = openOrCreateDatabase(LinnaeusDatabase.DATABASE_NAME,
                MODE_WORLD_READABLE, null);
        String query = "select _id,longitud,latitud,costo,tiempo,prioridad,calificacion, nombre, descripcion, imagen, sitio_web from nodo";
        Cursor c = db.rawQuery(query, null);
        mat = Util.imprimirLista(c);
        nodos = new ArrayList<>();
        marcadores = new ArrayList<>();
        Node n = null;
        LatLng pos = null;
        for (int i = 0; i < mat.length; i++) {
            n = new Node(mat[i][0], mat[i][1], mat[i][2], mat[i][3], mat[i][4], mat[i][5]);
            nodos.add(n);
            pos = new LatLng(n.x, n.y);
            Util.mostrarMarcador(n.x, n.y, mat[i][7], mat[i][10], 0, marcadores, mapa);
            marcadores.add(pos);
        }
        c.close();
        db.close();
        c = null;
        db = null;
        nodos=null;
    }

    private void iniciarLocalService() {
        if (MiLocationListener.isEnableLocation) {

        } else {
            LocationManager milocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener milocListener = new MiLocationListener();
            MiLocationListener.appcont = this.getApplicationContext();
            LinnaeusDatabase lb = new LinnaeusDatabase(getApplicationContext());
            MiLocationListener.db = openOrCreateDatabase(LinnaeusDatabase.DATABASE_NAME,
                    MODE_WORLD_READABLE, null);
            try {

                milocManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 0, 0, milocListener);
            } catch (Exception e) {

            } finally {
                milocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        0, 0, milocListener);
            }
        }
    }

    public void sitios(View v) {
        Intent sites = new Intent(this, SitiosActivity.class);
        sites.putStringArrayListExtra("rows", Util.toArrayList(mat));
        startActivity(sites);
    }

    public void ruta(View v) {
        Intent ruta = new Intent(this, RutaActivity.class);
        ruta.putStringArrayListExtra("rows", Util.toArrayList(mat));
        ruta.putExtra("presupuesto",Double.parseDouble(presupuesto.getText().toString()) );
        ruta.putExtra("tiempo",Double.parseDouble(tiempo.getText().toString()));
        ruta.putExtra("latitud",MiLocationListener.lat);
        ruta.putExtra("longitud",MiLocationListener.longi);
        startActivity(ruta);
    }


}
