package com.noahtownsend.carfox.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.noahtownsend.carfox.CarfoxApplication
import com.noahtownsend.carfox.databinding.FragmentHomeBinding
import com.noahtownsend.carfox.datastore.VehicleDatastore
import com.noahtownsend.carfox.ui.adapter.VehicleAdapter
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding!!.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val datastore = VehicleDatastore((activity?.application as CarfoxApplication).database)
        datastore.getVehicles(requireContext()).subscribeOn(Schedulers.newThread())
            .subscribe { listings ->
                (context as MainActivity).runOnUiThread {
                    binding?.apply {
                        val adapter = VehicleAdapter(requireContext(), listings)
                        vehicles.adapter = adapter
                        vehicles.layoutManager = LinearLayoutManager(context)

                        adapter.onVehicleSelected().subscribe { vehicle ->
                            findNavController().navigate(
                                HomeFragmentDirections.actionHomeFragmentToVehicleDetailsFragment(
                                    vehicle
                                )
                            )
                        }
                    }
                }

            }
    }
}