package com.noahtownsend.carfox.datastore.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.noahtownsend.carfox.Vehicle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@androidx.room.Database(
    entities = [Vehicle::class],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao

    companion object {
        @Volatile
        private var INSTANCE: Database? = null

        fun getDatabase(context: Context): Database {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Database::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                CoroutineScope(Dispatchers.IO).launch {
                    instance.runInTransaction { return@runInTransaction } // force prepopulate
                }
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}