package voyage.unal.com.voyagebudget.LN;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;


public class MiLocationListener implements LocationListener {
    public static double lat = 0;
    public static double longi = 0;
    public static Context appcont;
    public static SQLiteDatabase db;
    public static boolean changed = false;
    public static GoogleMap mapa;
    public static boolean isEnableLocation=false;

    @Override
    public void onLocationChanged(Location loc) {
        lat = loc.getLatitude();
        longi = loc.getLongitude();
        try {
            if (!changed) {
                buscarSede();
                changed = true;
                isEnableLocation=true;
            }
        } catch (Exception e) {
            Log.e("error LocationListener", e.toString());
        }
        /*
         * Toast.makeText(appcont, "posisiones cambiadas a lat: " + lat +
		 * " long: " + longi, 1) .show();
		 */
    }

    public void buscarSede() {
        String query = "SELECT nombre,min((latitud-" + lat
                + ")*(latitud-" + lat + ")+(longitud-" + longi + ")*(longitud-"
                + longi + ")) FROM Ciudad";
        if (longi < 0) {
            query = "SELECT nombre,min((latitud-" + lat
                    + ")*(latitud-" + lat + ")+(longitud+" + longi * -1
                    + ")*(longitud+" + longi * -1
                    + ")) FROM Ciudad";
            if (lat < 0) {
                query = "SELECT nombre,min((latitud+" + lat * -1
                        + ")*(latitud+" + lat * -1 + ")+(longitud+" + longi
                        * -1 + ")*(longitud+" + longi * -1
                        + ")) FROM Ciudad";
            }
        }
        if (lat < 0) {
            query = "SELECT nombre,min((latitud+" + lat * -1
                    + ")*(latitud+" + lat * -1 + ")+(longitud-" + longi
                    + ")*(longitud-" + longi
                    + ")) FROM Ciudad";
            if (longi < 0) {
                query = "SELECT nombre,min((latitud+" + lat * -1
                        + ")*(latitud+" + lat * -1 + ")+(longitud+" + longi
                        * -1 + ")*(longitud+" + longi * -1
                        + ")) FROM Ciudad";
            }
        }
        try {
            Cursor c = db.rawQuery(query, null);
            String[][] mat = Util.imprimirLista(c);
            Log.e("sede", query);
            Toast.makeText(appcont, Util.getcolumn(mat, 0)[0].trim(),
                    Toast.LENGTH_SHORT).show();
            String a = Util.getcolumn(mat, 0)[0].trim();
            c.close();
            db.close();
            Util.animarCamara(lat, longi, 14, mapa);
            Util.mostrarMarcador(lat, longi, "Mi ubicacion", "lat: " + (lat + "").substring(0, 10)
                    + " lon: " + (longi + "").substring(0, 10), 0, null, mapa);
        } catch (Exception e) {
            Log.e("EXC_LOCATION", e.toString());
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (!provider.contains("gps")) {
            Toast.makeText(appcont,
                    "Activa el Wi-Fi para Acceder a tu ubicacion", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onProviderEnabled(String provider) {
        if (!provider.contains("gps")) {
            Toast.makeText(appcont, "Activado el " + provider, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

}
