package vn.liverace;
import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceRequest;
public class MainActivity extends Activity {
 private WebView web;
 @Override public void onCreate(Bundle b) {
  super.onCreate(b);
  getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
  web = new WebView(this);
  web.getSettings().setJavaScriptEnabled(true);
  web.getSettings().setDomStorageEnabled(true);
  web.getSettings().setAllowFileAccess(false);
  web.getSettings().setAllowContentAccess(false);
  web.setWebViewClient(new WebViewClient() {
   @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) { return true; }
   @Override public android.webkit.WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
    if ("app.local".equals(request.getUrl().getHost()) && "/index.html".equals(request.getUrl().getPath())) {
     try { return new android.webkit.WebResourceResponse("text/html", "UTF-8", getAssets().open("index.html")); } catch (java.io.IOException e) { return null; }
    }
    return super.shouldInterceptRequest(view, request);
   }
  });
  setContentView(web);
  web.loadUrl("https://app.local/index.html");
 }
 @Override protected void onDestroy() { if(web != null) web.destroy(); super.onDestroy(); }
}
