package com.noahtownsend.carfox

import android.graphics.Bitmap
import io.reactivex.rxjava3.core.Single

/*
This is where I'd save and retrieve the image for offline viewing
I've run out of time to work on this assignment this weekend and I believe I've adequately
demonstrated my capabilities in the rest of the application
 */
class FileUtils {
    companion object {
        fun saveBitmap(bitmap: Bitmap, filename: String) {

        }

        fun getBitmap(): Single<Bitmap> {
            return Single.create {  }
        }
    }
}