package voyage.unal.com.voyagebudget;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import voyage.unal.com.voyagebudget.LN.Util;


public class WebActivity extends ActionBarActivity {

    String URL = "";
    WebView browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web);
        browser = (WebView) findViewById(R.id.webView);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setBuiltInZoomControls(true);
        browser.setWebViewClient(new WebViewClient() {
            // evita que los enlaces se abran fuera nuestra app en el navegador
            // de android
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

        });
        if (savedInstanceState == null) {
            Bundle b = getIntent().getExtras();
            URL = b.getString("paginaWeb");
            browser.loadUrl(URL);
            if (!Util.isOnline(this)) {
                Util.notificarRed(this);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("URL", browser.getUrl());
        Log.e("pagina actual", "" + browser.getUrl());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        URL = savedInstanceState.getString("URL");
        browser.loadUrl(URL);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        menu.getItem(3).setTitle(URL);
        menu.getItem(3).setTitleCondensed(URL);
        return super.onCreateOptionsMenu(menu);
    }

    public void volver(View v) {
        volver();
    }

    public void volver() {
        if (browser.canGoBack()) {
            browser.goBack();
        }
    }

    public void adelante() {
        if (browser.canGoForward()) {
            browser.goForward();
        }
    }

    public void recargar() {
        browser.reload();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ItemVolver:
                volver();
                return true;

            case R.id.ItemRefresh:
                recargar();
                return true;
            case R.id.ItemForward:
                adelante();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
