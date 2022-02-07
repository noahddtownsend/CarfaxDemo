package com.noahtownsend.carfox

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.rxjava3.core.Single
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL


class InternetUtils {
    companion object {
        const val API_URL = "https://carfax-for-consumers.firebaseio.com/assignment.json"

        fun getBitmapFromURL(src: String): Single<Bitmap> {
            return Single.create { single ->
                try {
                    val url = URL(src)
                    val connection: HttpURLConnection =
                        (url.openConnection() as HttpURLConnection).apply {
                            doInput = true
                            connect()
                        }

                    if (connection.responseCode == 200) {
                        single.onSuccess(BitmapFactory.decodeStream(connection.inputStream))
                    } else {
                        single.onError(Exception("Image call failed with HTTP response code ${connection.responseCode}"))
                    }
                } catch (e: Exception) {
                    single.onError(e)
                }
            }
        }

        fun get(src: String): Single<String> {
            return Single.create { single ->
                val url = URL(src)
                val connection = (url.openConnection() as HttpURLConnection).apply {
                    connect()
                }

                val bufferedReader = BufferedReader(InputStreamReader(connection.inputStream))

                val buffer = StringBuffer()
                var line = bufferedReader.readLine()

                while (line != null) {
                    buffer.append(line)
                    line = bufferedReader.readLine()
                }

                single.onSuccess(buffer.toString())
            }
        }
    }
}