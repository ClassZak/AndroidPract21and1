package com.example.androidpract21and1

import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.IOException

import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val webView = findViewById<WebView>(R.id.webViewMain)
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://github.com/")

        findViewById<Button>(R.id.loadButton).setOnClickListener {

            Thread {
                try {
                    val content = ActivityResultContracts.GetContent()
                    webView.post {
                        webView.loadDataWithBaseURL(
                            "https://stackoverflow.com/",
                            content,
                            "text/html",
                            "UTF-8",
                            "https://stackoverflow.com/"
                        )
                        Toast.makeText(
                            applicationContext,
                            "Данные загружены",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    contentView.post { contentView.text = content }
                } catch (ex: IOException) {
                    contentView.post {
                        contentView.text = "Ошибка: " + ex.message
                        Toast.makeText(applicationContext, "Ошибка",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        }
    }

}