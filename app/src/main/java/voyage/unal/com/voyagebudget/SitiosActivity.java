package voyage.unal.com.voyagebudget;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import voyage.unal.com.voyagebudget.LN.LinnaeusDatabase;
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
        act=this;
        Bundle b=getIntent().getExtras();
        rows = b.getStringArrayList("rows");
        String[][] mat=Util.toMatrix(rows);
        MiAdaptador adapter = new MiAdaptador(this, Util.getcolumn(mat,7), Util.toDouble(Util.getcolumn(mat,6)));
        lv.setAdapter( adapter);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sitios, menu);
        return true;
    }
}
