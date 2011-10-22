package Com.jaffa.chineseaday;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;


public class marketview extends Activity {
	WebView webView;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.marketview);
	
	    webView = (WebView) findViewById(R.id.webview);
	    webView.getSettings().setJavaScriptEnabled(true);
	    webView.loadUrl("https://market.android.com");	    
	}
}