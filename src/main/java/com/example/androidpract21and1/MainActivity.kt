package com.example.androidpract21and1

import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    var siteURL:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        siteURL=getString(R.string.site)

        val webView = findViewById<WebView>(R.id.webViewMain)
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(getString(R.string.site))

        findViewById<Button>(R.id.loadButton).setOnClickListener {
            siteURL=findViewById<EditText>(R.id.URLEditText).text.toString()
            Thread {
                try {
                    val content = getContent(siteURL)
                    webView.post {
                        webView.loadDataWithBaseURL(
                            siteURL,
                            content,
                            "text/html",
                            "UTF-8",
                            siteURL
                        )
                        Toast.makeText(
                            applicationContext,
                            "Данные загружены",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (ex: IOException) {
                    Toast.makeText(
                        applicationContext, "Ошибка",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.start()
        }
    }
    @Throws(IOException::class)
    private fun getContent(path: String): String {
        var reader: BufferedReader? = null
        var stream: InputStream? = null
        var connection: HttpsURLConnection? = null
        return try {
            val url = URL(path)
            connection = url.openConnection() as HttpsURLConnection
            connection.requestMethod = "GET"
            connection!!.readTimeout = 10000
            connection.connect()
            stream = connection.inputStream
            reader = BufferedReader(InputStreamReader(stream))
            val buf = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                buf.append(line).append("\n")
            }
            buf.toString()
        }
        catch(e:Exception){
            Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
            return ""
        } finally {
            reader?.close()
            stream?.close()
            connection?.disconnect()
        }
    }
}