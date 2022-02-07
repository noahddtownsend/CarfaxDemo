package com.noahtownsend.carfox

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.json.JSONObject
import java.io.Serializable
import java.text.DecimalFormat
import java.util.*

@Entity(tableName = "vehicle")
class Vehicle(
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "make") val make: String = "",
    @ColumnInfo(name = "model") val model: String = "",
    @ColumnInfo(name = "trim") val trim: String = "",
    @ColumnInfo(name = "price") val price: Double = 0.0,
    @ColumnInfo(name = "mileage") val mileage: Int = 0,
    @ColumnInfo(name = "dealerNumber") val dealerNumber: String = "",
    @ColumnInfo(name = "location") val location: String = "",
    @ColumnInfo(name = "interiorColor") val interiorColor: String = "",
    @ColumnInfo(name = "exteriorColor") val exteriorColor: String = "",
    @ColumnInfo(name = "driveType") val driveType: String = "",
    @ColumnInfo(name = "transmission") val transmission: TransmissionType = TransmissionType.MANUAL,
    @ColumnInfo(name = "engine") val engine: String = "",
    @ColumnInfo(name = "bodyStyle") val bodyStyle: String = "",
    @ColumnInfo(name = "fuel") val fuel: String = "",
    @ColumnInfo(name = "imageUrl") val imageUrl: String = "",
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Serializable {
    @Ignore
    var bitmap: Bitmap? = null
        set(value) {
            field = value

            if (value != null) {
                Thread {
                    Runnable {
                        FileUtils.saveBitmap(value, "$id")
                    }
                }.run() // this should really be done with workmanager
            }
        }

    fun getReadableMileage(): String {
        return millageFormat.format(mileage / 1000)
    }

    fun getReadablePrice(): String {
        return priceFormat.format(price)
    }

    companion object {
        private val millageFormat = DecimalFormat("#.#k")
        private val priceFormat = DecimalFormat("$#.##")

        fun fromJson(json: JSONObject): Vehicle {
            val dealer = json.getJSONObject("dealer")
            val dealerNumber = dealer.getString("phone")
            val location = "${dealer.getString("city")}, ${dealer.getString("state")}"
            val transmission = TransmissionType.valueOf(
                json.getString("transmission")
                    .uppercase(Locale.getDefault())
            )

            return Vehicle(
                json.getInt("year"),
                json.getString("make"),
                json.getString("model"),
                json.getString("trim"),
                json.getDouble("currentPrice"),
                json.getInt("mileage"),
                dealerNumber,
                location,
                json.getString("interiorColor"),
                json.getString("exteriorColor"),
                json.getString("drivetype"),
                transmission,
                json.getString("engine"),
                json.getString("bodytype"),
                json.getString("fuel"),
                json.getJSONObject("images").getJSONObject("firstPhoto").getString("large")
            )
        }
    }
}