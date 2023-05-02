package com.vvwxx.bangkit.storyapp.ui.map

import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.vvwxx.bangkit.storyapp.R
import com.vvwxx.bangkit.storyapp.data.response.ListStoryItem
import com.vvwxx.bangkit.storyapp.databinding.FragmentMapBinding
import com.vvwxx.bangkit.storyapp.utils.ViewModelFactory
import java.io.IOException
import java.util.*

class MapFragment : Fragment() {

    private val boundsBuilder = LatLngBounds.Builder()
    private lateinit var mMap: GoogleMap
    private var _binding : FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: ViewModelFactory
    private val mapViewModel: MapViewModel by viewModels { factory }

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap

        mapViewModel.getUser.observe(viewLifecycleOwner) { user ->
            mapViewModel.getListMap(user.token)
        }

        mapViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        mapViewModel.listMap.observe(viewLifecycleOwner) { maps ->
            addManyMarker(maps)
        }

        mapViewModel.message.observe(viewLifecycleOwner) {
            showToast(it)
        }

        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isIndoorLevelPickerEnabled = true
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true

        getMyLocation()
        setMapStyle()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        factory = ViewModelFactory.getInstance(requireActivity())

    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (activity?.let {
                ContextCompat.checkSelfPermission(
                    it.applicationContext,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun addManyMarker(maps: List<ListStoryItem>) {
        maps.forEach { point ->
            val latLng = LatLng(point.lat, point.lon)
            val storiesName = getAddressName(point.lat, point.lon)
            mMap.addMarker(MarkerOptions().position(latLng).title(point.name).snippet(storiesName))
            boundsBuilder.include(latLng)
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )

    }

    private fun getAddressName(lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
                Log.d(TAG, "getAddressName: $addressName")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireActivity(), R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "MapFragment"
    }
}