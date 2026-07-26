package com.example.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.SunshineYellow
import kotlinx.coroutines.delay

fun openYouTubeVideoExternal(context: Context, videoId: String) {
    val videoUri = Uri.parse("https://www.youtube.com/watch?v=$videoId")
    val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId")).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    val webIntent = Intent(Intent.ACTION_VIEW, videoUri).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    try {
        context.startActivity(appIntent)
    } catch (e: Exception) {
        try {
            context.startActivity(webIntent)
        } catch (e2: Exception) {
            Log.e("YouTubePlayer", "Error opening external video", e2)
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun YouTubePlayerComposable(
    youtubeId: String? = null,
    playlistId: String? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isLoading by remember(youtubeId, playlistId) { mutableStateOf(true) }

    val resolvedUrl = remember(youtubeId, playlistId) {
        when {
            !youtubeId.isNullOrBlank() && !playlistId.isNullOrBlank() ->
                "https://www.youtube.com/embed/$youtubeId?list=$playlistId&autoplay=1&playsinline=1&enablejsapi=1&rel=0&modestbranding=1"
            !youtubeId.isNullOrBlank() ->
                "https://www.youtube.com/embed/$youtubeId?autoplay=1&playsinline=1&enablejsapi=1&rel=0&modestbranding=1"
            !playlistId.isNullOrBlank() ->
                "https://www.youtube.com/embed/videoseries?list=$playlistId&autoplay=1&playsinline=1&enablejsapi=1&rel=0&modestbranding=1"
            else ->
                "https://www.youtube.com/embed/videoseries?list=PLHz1Xt0IaQWM&autoplay=1&playsinline=1&enablejsapi=1&rel=0&modestbranding=1"
        }
    }

    val htmlData = remember(resolvedUrl) {
        """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
            <style>
                body, html { margin: 0; padding: 0; width: 100%; height: 100%; background-color: #0F172A; overflow: hidden; }
                iframe { width: 100%; height: 100%; border: none; }
            </style>
        </head>
        <body>
            <iframe 
                src="$resolvedUrl" 
                frameborder="0" 
                width="100%" 
                height="100%"
                allow="autoplay; encrypted-media; picture-in-picture; accelerometer; gyroscope; fullscreen"
                allowfullscreen>
            </iframe>
        </body>
        </html>
        """.trimIndent()
    }

    LaunchedEffect(resolvedUrl) {
        isLoading = true
        delay(600)
        isLoading = false
    }

    Box(
        modifier = modifier
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

                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        mediaPlaybackRequiresUserGesture = false
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        allowFileAccess = true
                        allowContentAccess = true
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        userAgentString = "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Mobile Safari/537.36"
                    }
                    webChromeClient = WebChromeClient()
                    webViewClient = WebViewClient()
                    tag = resolvedUrl
                    loadDataWithBaseURL(
                        "https://www.youtube.com",
                        htmlData,
                        "text/html",
                        "UTF-8",
                        null
                    )
                }
            },
            update = { webView ->
                if (webView.tag != resolvedUrl) {
                    webView.tag = resolvedUrl
                    webView.loadDataWithBaseURL(
                        "https://www.youtube.com",
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
                    Log.e("YouTubePlayer", "Error releasing webview", e)
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
