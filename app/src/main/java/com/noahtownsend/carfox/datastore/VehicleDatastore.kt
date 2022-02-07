package com.noahtownsend.carfox.datastore

import android.content.Context
import android.net.ConnectivityManager
import com.noahtownsend.carfox.InternetUtils
import com.noahtownsend.carfox.Vehicle
import com.noahtownsend.carfox.datastore.database.Database
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONObject


class VehicleDatastore(val database: Database) {
    fun getVehicles(context: Context): Single<MutableList<Vehicle>> {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return Single.create { single ->
            if (connectivityManager.activeNetworkInfo?.isConnectedOrConnecting == true) {
                InternetUtils.get(InternetUtils.API_URL)
                    .subscribeOn(Schedulers.newThread())
                    .doOnError { e -> e.printStackTrace() }
                    .subscribe { json ->
                        val jsonObject = JSONObject(json)
                        val listingsJson = jsonObject.getJSONArray("listings")

                        val listings = ArrayList<Vehicle>()
                        for (i in 0 until listingsJson.length()) {
                            val vehicle = Vehicle.fromJson(listingsJson.getJSONObject(i))
                            listings.add(vehicle)

                            Observable.fromCallable {
                                database.vehicleDao().insert(vehicle)
                            }
                                .subscribeOn(Schedulers.io())
                                .subscribe()

                            Completable.fromRunnable {

                            }
                        }

                        database.vehicleDao().deleteAll()
                        single.onSuccess(listings)
                    }
            } else {
                Observable.fromCallable { database.vehicleDao().getVehicles() }
                    .subscribeOn(Schedulers.io())
                    .subscribe{
                        single.onSuccess(it as @NonNull MutableList<Vehicle>)
                    }
            }
        }
    }
}