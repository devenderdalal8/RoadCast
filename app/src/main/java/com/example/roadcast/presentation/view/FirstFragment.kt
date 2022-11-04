package com.example.roadcast.presentation.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.roadcast.R
import com.example.roadcast.data.util.Utility.Companion.getBitmapFromVectorDrawable
import com.example.roadcast.databinding.FragmentFirstBinding
import com.example.roadcast.presentation.utils.PermissionUtils
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FirstFragment : Fragment(), OnMapReadyCallback,
    GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveStartedListener {
    private lateinit var mGoogleMap: GoogleMap
    var mLatitude: Double? = null
    var mLongitude: Double? = null
    private lateinit var mCurrentLocation: Marker


    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeMap()
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    private fun initializeMap() {
        // Initializing Map
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.myMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        mGoogleMap.setOnCameraIdleListener(this)
        mGoogleMap.uiSettings.isZoomGesturesEnabled = true
        mGoogleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mGoogleMap.setOnCameraMoveStartedListener(this)
        mGoogleMap.uiSettings.isMyLocationButtonEnabled = false
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mGoogleMap.isMyLocationEnabled = false
        mGoogleMap.uiSettings.isMapToolbarEnabled = false
        mGoogleMap.uiSettings.isTiltGesturesEnabled = true
    }

    private fun setUpLocationListener() {
        val locationRequest = LocationRequest().setInterval(20000).setFastestInterval(20000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        activity?.let {
            LocationServices.getFusedLocationProviderClient(
                it
            )
        }?.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        mLatitude = location.latitude
                        mLongitude = location.longitude
                        setLocation(mLatitude!!, mLongitude!!)
                    }
                }
            },
            Looper.myLooper()
        )
    }

    private fun setLocation(mLatitude: Double, mLongitude: Double) {
        val latLng = LatLng(mLatitude, mLongitude)
        val cameraPosition = CameraPosition.Builder().target(latLng).zoom(10F).build()

        if (!this::mCurrentLocation.isInitialized) {
            mCurrentLocation = mGoogleMap.addMarker(
                MarkerOptions().icon(
                    getBitmapFromVectorDrawable(context, R.drawable.ic_location_on)?.let {
                        BitmapDescriptorFactory.fromBitmap(
                            it
                        )
                    }
                ).position(latLng)
            )!!
        }
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))
    }

    override fun onStart() {
        super.onStart()
        when {
            PermissionUtils.isAccessFineLocationGranted(requireContext()) -> {
                when {
                    PermissionUtils.isLocationEnabled(requireContext()) -> {
                        setUpLocationListener()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(requireContext())
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    requireActivity() as AppCompatActivity,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(requireContext()) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(requireContext())
                        }
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.location_permission_not_granted),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onCameraIdle() {

    }

    override fun onCameraMoveStarted(p0: Int) {
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 999
    }
}