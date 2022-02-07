package com.noahtownsend.carfox.datastore.database

import androidx.room.*
import com.noahtownsend.carfox.Vehicle
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {
    @Insert
    fun insert(vehicle: Vehicle)

    @Query("SELECT * from vehicle")
    fun getVehicles(): List<Vehicle>

    @Query("DELETE from vehicle")
    fun deleteAll()
}