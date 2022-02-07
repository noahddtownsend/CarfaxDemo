package com.noahtownsend.carfox.ui.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.noahtownsend.carfox.InternetUtils
import com.noahtownsend.carfox.R
import com.noahtownsend.carfox.Vehicle
import com.noahtownsend.carfox.databinding.VehicleListItemBinding
import com.noahtownsend.carfox.ui.MainActivity
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject


class VehicleAdapter(private val context: Context, private val vehicles: MutableList<Vehicle>) :
    RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {

    val selectedVehicle: PublishSubject<Vehicle> = PublishSubject.create<Vehicle>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val binding = VehicleListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return VehicleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        holder.bind(context, vehicles[position], selectedVehicle)
    }

    override fun getItemCount(): Int {
        return vehicles.size
    }

    fun onVehicleSelected(): PublishSubject<Vehicle> {
        return selectedVehicle
    }

    class VehicleViewHolder(val binding: VehicleListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, vehicle: Vehicle, onClick: PublishSubject<Vehicle>) {
            binding.apply {
                root.setOnClickListener {
                    onClick.onNext(vehicle)
                }
                vehicleItemTitle.text = context.resources.getString(
                    R.string.vehicle_title,
                    vehicle.year.toString(),
                    vehicle.make,
                    vehicle.model
                )

                vehicleItemDetails.text = context.resources.getString(
                    R.string.vehicle_details,
                    vehicle.getReadablePrice(),
                    vehicle.getReadableMileage()
                )

                vehicleItemLocation.text = vehicle.location

                vehicleItemCallDealerButton.setOnClickListener {
                    val callIntent = Intent(Intent.ACTION_DIAL)
                    callIntent.data = Uri.parse("tel:${vehicle.dealerNumber}")
                    startActivity(context, callIntent, null)
                }

                if (vehicle.bitmap == null) {
                    InternetUtils.getBitmapFromURL(vehicle.imageUrl)
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(
                            { image ->
                                (context as MainActivity).runOnUiThread {
                                    vehicleItemImage.setImageBitmap(image)
                                }
                            },
                            { e ->
                                (context as MainActivity).runOnUiThread {
                                    vehicleItemImage.setImageResource(android.R.color.transparent)
                                }
                                e.printStackTrace()
                            }
                        )
                } else {
                    vehicleItemImage.setImageBitmap(vehicle.bitmap)
                }
            }
        }
    }
}