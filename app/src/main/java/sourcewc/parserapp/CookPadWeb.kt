package sourcewc.parserapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebView

class CookPadWeb : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_view)

        val url = intent.getStringExtra("URL_KEY")


        val recipeWebView : WebView = findViewById(R.id.webview)
        recipeWebView.loadUrl(url)

    }
}