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
    youtubeId: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isLoading by remember(youtubeId) { mutableStateOf(true) }
    val safeVideoId = if (youtubeId.isBlank()) "jYYvwC3L2kI" else youtubeId

    val htmlData = remember(safeVideoId) {
        """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
            <style>
                body, html { margin: 0; padding: 0; width: 100%; height: 100%; background-color: #000000; overflow: hidden; }
                #player { width: 100%; height: 100%; }
            </style>
        </head>
        <body>
            <div id="player"></div>
            <script>
                var tag = document.createElement('script');
                tag.src = "https://www.youtube-nocookie.com/iframe_api";
                var firstScriptTag = document.getElementsByTagName('script')[0];
                firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

                var player;
                function onYouTubeIframeAPIReady() {
                    player = new YT.Player('player', {
                        height: '100%',
                        width: '100%',
                        videoId: '$safeVideoId',
                        host: 'https://www.youtube-nocookie.com',
                        playerVars: {
                            'playsinline': 1,
                            'autoplay': 1,
                            'rel': 0,
                            'modestbranding': 1,
                            'origin': 'https://www.youtube-nocookie.com'
                        },
                        events: {
                            'onReady': function(event) {
                                event.target.playVideo();
                            }
                        }
                    });
                }
            </script>
        </body>
        </html>
        """.trimIndent()
    }

    LaunchedEffect(safeVideoId) {
        isLoading = true
        delay(1500)
        isLoading = false
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                WebView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setBackgroundColor(android.graphics.Color.BLACK)

                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        databaseEnabled = true
                        mediaPlaybackRequiresUserGesture = false
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        allowFileAccess = true
                        allowContentAccess = true
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    }
                    webChromeClient = WebChromeClient()
                    webViewClient = WebViewClient()
                    tag = safeVideoId
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
                if (webView.tag != safeVideoId) {
                    webView.tag = safeVideoId
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
                    Log.e("YouTubeWebView", "Error releasing webview", e)
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
