package com.example.spontaneouschef

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import java.net.HttpURLConnection
import java.net.URL


class ImageLoadTask(private val url: String, private val imageView: ImageView) :
    AsyncTask<Void?, Void?, Bitmap?>() {
    @Deprecated("Deprecated in Java")
    protected override fun doInBackground(vararg params: Void?): Bitmap? {
        try {
            val urlConnection = URL(url)
            val connection = urlConnection
                .openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input = connection.inputStream
            return BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        imageView.setImageBitmap(result)
    }
}