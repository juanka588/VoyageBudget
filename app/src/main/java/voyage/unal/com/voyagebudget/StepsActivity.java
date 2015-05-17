package voyage.unal.com.voyagebudget;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import voyage.unal.com.voyagebudget.LN.Node;
import voyage.unal.com.voyagebudget.LN.Step;


public class StepsActivity extends ActionBarActivity {
    private ListView lv;
    private ArrayList<Step> pasos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        lv = (ListView) findViewById(R.id.listSteps);
        String[] items = new String[30];
        for (int i=0;i<items.length;i++){
            items[i]=(i+1)+" source";
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Node ini=pasos.get(position).from;
                Node fin=pasos.get(position).to;
                Intent navigation = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?" + "saddr="
                                + ini.x + ","
                                + ini.y + "&daddr=" + fin.x + ","
                                + fin.y));
                startActivity(navigation);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_steps, menu);
        return true;
    }
}
