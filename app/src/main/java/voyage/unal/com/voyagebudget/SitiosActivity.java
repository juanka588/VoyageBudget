package voyage.unal.com.voyagebudget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import voyage.unal.com.voyagebudget.LN.MiAdaptador;
import voyage.unal.com.voyagebudget.LN.Util;


public class SitiosActivity extends ActionBarActivity {
    private ListView lv;
    private Activity act;
    private ArrayList<String> rows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitios);
        lv = (ListView) findViewById(R.id.listSitios);
        act = this;
        Bundle b = getIntent().getExtras();
        try {
            rows = b.getStringArrayList("rows");
        } catch (Exception e) {
            Log.e("error Sitios", e.toString());
        }
        final String[][] mat = Util.toMatrix(rows);
        MiAdaptador adapter = new MiAdaptador(this, Util.getcolumn(mat, 7), Util.toDouble(Util.getcolumn(mat, 6)));
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Util.getcolumn(mat, 7)));
        //lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detalles = new Intent(act, DetallesActivity.class);
                Log.e("mat", Util.toString(mat));
                detalles.putExtra("titulo", mat[position][7]);
                detalles.putExtra("descripcion", mat[position][8]);
                detalles.putExtra("costo", Double.parseDouble(mat[position][3]));
                detalles.putExtra("prioridad", Integer.parseInt(mat[position][5]));
                detalles.putExtra("sitio_web", mat[position][10]);
                detalles.putExtra("tiempo", Double.parseDouble(mat[position][4]));
                detalles.putExtra("imagen", mat[position][9]);
                detalles.putExtra("calificacion", Integer.parseInt(mat[position][6]));
                act.startActivity(detalles);
                Toast.makeText(act, "selecionado " + mat[position][7], Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sitios, menu);
        return true;
    }
}
