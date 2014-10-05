package net.matmas.unknowncallerlookup;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by matmas on 5/3/14.
 */
public class WebActivity extends Activity {

    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // After a fresh reboot this is somehow needed to turn the screen on when it is off and locked
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        // Bring this activity above the lock screen
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        // Unlock the non-security lock screen
        // Probably not a good idea because of user preferences
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        // Must be executed before calling setContentView
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_PROGRESS);

        webview = new WebView(this);
        setContentView(webview);


        // Enable Javascript
        webview.getSettings().setJavaScriptEnabled(true);

        // Disable local filesystem access
        webview.getSettings().setAllowFileAccess(false);

        // Enable zoom controls as well as pinch to zoom
        webview.getSettings().setBuiltInZoomControls(true);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                setProgressBarIndeterminateVisibility(true);
                setProgressBarVisibility(true);
                webview.loadUrl(url);
                return false;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(WebActivity.this, description, Toast.LENGTH_LONG).show();
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                WebActivity.this.setProgress(progress * 1000);
                if (progress == 100) {
                    setProgressBarIndeterminateVisibility(false);
                    setProgressBarVisibility(false);
                }
            }
        });

        String url = getIntent().getStringExtra("URL");
        webview.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Make back button work for going back in history
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_close:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
