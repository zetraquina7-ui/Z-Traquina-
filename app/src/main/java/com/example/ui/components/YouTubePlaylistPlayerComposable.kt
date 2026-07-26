package com.example.ui.components

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.SunshineYellow
import kotlinx.coroutines.delay

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun YouTubePlaylistPlayerComposable(
    playlistId: String? = null,
    embedUrl: String? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    val resolvedEmbedUrl = embedUrl ?: when (playlistId) {
        "PLT7ZV5QsDKA4" -> "https://www.youtube-nocookie.com/embed/videoseries?list=PLT7ZV5QsDKA4"
        "PLHXyMYX6Yxxc" -> "https://www.youtube-nocookie.com/embed/videoseries?list=PLHXyMYX6Yxxc"
        else -> "https://www.youtube-nocookie.com/embed/videoseries?list=PLHz1Xt0IaQWM"
    }

    val htmlData = remember(resolvedEmbedUrl) {
        """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
            <style>
                body, html { margin: 0; padding: 0; width: 100%; height: 100%; background-color: #0F172A; overflow: hidden; }
                .container { width: 100%; height: 100%; overflow: hidden; border-radius: 16px; background: #0F172A; }
                iframe { width: 100%; height: 100%; border: none; border-radius: 16px; }
            </style>
        </head>
        <body>
            <div class="container">
              <iframe 
                src="$resolvedEmbedUrl" 
                frameborder="0" 
                width="100%" 
                height="100%"
                allow="autoplay; encrypted-media; fullscreen">
              </iframe>
            </div>
        </body>
        </html>
        """.trimIndent()
    }

    LaunchedEffect(resolvedEmbedUrl) {
        isLoading = true
        delay(600)
        isLoading = false
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(420.dp)
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .testTag("youtube_playlist_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF0F172A)),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    WebView(ctx).apply {
                        setLayerType(android.view.View.LAYER_TYPE_SOFTWARE, null)
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        setBackgroundColor(android.graphics.Color.parseColor("#0F172A"))

                        CookieManager.getInstance().setAcceptCookie(true)
                        CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            mediaPlaybackRequiresUserGesture = false
                            loadWithOverviewMode = true
                            useWideViewPort = true
                            allowFileAccess = true
                            allowContentAccess = true
                            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        }

                        webChromeClient = WebChromeClient()
                        webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): Boolean {
                                val url = request?.url?.toString() ?: return false
                                if (url.startsWith("intent:") || url.startsWith("vnd.youtube:")) {
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        }
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        Log.e("YouTube", "Error opening intent", e)
                                    }
                                    return true
                                }
                                return false
                            }
                        }

                        webViewRef = this
                        tag = resolvedEmbedUrl
                        loadDataWithBaseURL(
                            "https://www.youtube-nocookie.com",
                            htmlData,
                            "text/html",
                            "UTF-8",
                            null
                        )
                    }
                },
                update = { webView ->
                    webViewRef = webView
                    if (webView.tag != resolvedEmbedUrl) {
                        webView.tag = resolvedEmbedUrl
                        webView.loadDataWithBaseURL(
                            "https://www.youtube-nocookie.com",
                            htmlData,
                            "text/html",
                            "UTF-8",
                            null
                        )
                    }
                },
                onRelease = { webView ->
                    try {
                        webView.stopLoading()
                        webView.loadUrl("about:blank")
                        (webView.parent as? ViewGroup)?.removeView(webView)
                        webView.destroy()
                    } catch (e: Exception) {
                        Log.e("YouTubePlaylist", "Error releasing webview", e)
                    }
                }
            )

            if (isLoading) {
                CircularProgressIndicator(
                    color = SunshineYellow,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}
