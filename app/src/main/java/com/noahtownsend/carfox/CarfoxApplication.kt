package com.noahtownsend.carfox

import android.app.Application
import com.noahtownsend.carfox.datastore.database.Database

// I know your company is Carfax :)
class CarfoxApplication: Application() {
    val database: Database by lazy { Database.getDatabase(this) }

}