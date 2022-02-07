package com.noahtownsend.carfox.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.noahtownsend.carfox.InternetUtils
import com.noahtownsend.carfox.R
import com.noahtownsend.carfox.databinding.VehicleDetailsFragmentBinding
import com.noahtownsend.carfox.ui.viewmodel.VehicleDetailsViewModel
import io.reactivex.rxjava3.schedulers.Schedulers

class VehicleDetailsFragment : Fragment() {

    private val args: VehicleDetailsFragmentArgs by navArgs()
    private lateinit var viewModel: VehicleDetailsViewModel
    private lateinit var binding: VehicleDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = VehicleDetailsViewModel(args.vehicle)
        binding =
            VehicleDetailsFragmentBinding.inflate(LayoutInflater.from(context), container, false)
        binding.viewmodel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            vehicleTitle.text = requireContext().resources.getString(
                R.string.vehicle_title,
                viewmodel!!.vehicle.year.toString(),
                viewmodel!!.vehicle.make,
                viewmodel!!.vehicle.model
            )

            vehicleDetails.text = requireContext().resources.getString(
                R.string.vehicle_details,
                viewmodel!!.vehicle.getReadablePrice(),
                viewmodel!!.vehicle.getReadableMileage()
            )

            if (viewmodel!!.vehicle.bitmap == null) {
                InternetUtils.getBitmapFromURL(viewmodel!!.vehicle.imageUrl)
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(
                        { image ->
                            (context as MainActivity).runOnUiThread {
                                vehicleImage.setImageBitmap(image)
                            }
                        },
                        { e ->
                            (context as MainActivity).runOnUiThread {
                                vehicleImage.setImageResource(android.R.color.transparent)
                            }
                            e.printStackTrace()
                        }
                    )
            } else {
                vehicleImage.setImageBitmap(viewmodel!!.vehicle.bitmap)
            }
        }
    }

}