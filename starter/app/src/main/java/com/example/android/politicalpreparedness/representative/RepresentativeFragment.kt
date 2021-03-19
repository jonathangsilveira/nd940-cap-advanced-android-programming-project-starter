package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.data.ServiceLocator
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.util.checkPermissionGranted
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.snackbar.Snackbar
import java.util.*

class DetailFragment : Fragment() {

    companion object {
        private val TAG = DetailFragment::class.java.simpleName
        private const val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 33
        private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
        private const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
        private const val LOCATION_PERMISSION_INDEX = 0
        private const val BACKGROUND_LOCATION_PERMISSION_INDEX = 1
    }

    private val runningQOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    private val _viewModel: RepresentativeViewModel by viewModels {
        val repo = ServiceLocator.provideRepresentativesRepository()
        RepresentativeViewModel.Factory(repo)
    }

    private lateinit var binding: FragmentRepresentativeBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
                inflater,
                com.example.android.politicalpreparedness.R.layout.fragment_representative,
                container,
                false
        )
        binding.lifecycleOwner = this
        binding.viewModel = _viewModel

        binding.state.adapter = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.listRepresentatives.adapter = RepresentativeListAdapter()
        binding.buttonSearch.setOnClickListener {
            _viewModel.validateAndApplyAddress(
                    binding.addressLine1.text.toString(),
                    binding.addressLine2.text.toString(),
                    binding.city.text.toString(),
                    binding.state.selectedItem.toString(),
                    binding.zip.text.toString()
            )
        }
        binding.buttonLocation.setOnClickListener {
            checkPermissions()
        }
        return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requiredPermissionsDenied(grantResults, requestCode))
            showPermissionDeniedExplanation()
        else
            checkDeviceLocationSettings()
    }

    private fun checkPermissions() {
        if (foregroundAndBackgroundLocationPermissionApproved()) {
            checkDeviceLocationSettings()
        } else {
            requestForegroundAndBackgroundLocationPermissions()
        }
    }


    private fun getLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            val locationProviderClient =
                    LocationServices.getFusedLocationProviderClient(requireActivity())
            val locationResult = locationProviderClient.lastLocation
            locationResult.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Set the map's camera position to the current location of the device.
                    val lastKnownLocation = task.result ?: return@addOnCompleteListener
                    val address = geoCodeLocation(lastKnownLocation)
                    _viewModel.updateAddress(address)
                } else {
                    Log.d(TAG, "Current location is null. Using defaults.")
                    Log.e(TAG, "Exception: %s", task.exception)
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    _viewModel.createAddress(
                            address.thoroughfare,
                            address.subThoroughfare,
                            address.locality,
                            address.adminArea,
                            address.postalCode
                    )
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    /*
     *  Determines whether the app has the appropriate permissions across Android 10+ and all other
     *  Android versions.
     */
    @TargetApi(Build.VERSION_CODES.Q)
    private fun foregroundAndBackgroundLocationPermissionApproved(): Boolean {
        val foregroundLocationApproved =
                checkPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)
        val backgroundPermissionApproved =
                if (runningQOrLater) {
                    checkPermissionGranted(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                } else {
                    true
                }
        return foregroundLocationApproved && backgroundPermissionApproved
    }

    /*
     *  Uses the Location Client to check the current state of location settings, and gives the user
     *  the opportunity to turn on location services within our app.
     */
    private fun checkDeviceLocationSettings(resolve: Boolean = true) {
        val locationRequest = createLocationRequest()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(requireActivity())
        val locationSettingsResponseTask = settingsClient.checkLocationSettings(builder.build())
        locationSettingsResponseTask
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                        getLocation()
                }.addOnFailureListener { exception ->
                    if (exception is ResolvableApiException && resolve){
                        try {
                            exception.startResolutionForResult(requireActivity(),
                                    REQUEST_TURN_DEVICE_LOCATION_ON)
                        } catch (sendEx: IntentSender.SendIntentException) {
                            Log.d(TAG, "Error getting location settings resolution: " + sendEx.message)
                        }
                    } else {
                        showLocationRequiredMessage()
                    }
                }
    }

    private fun createLocationRequest(): LocationRequest {
        return LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
        }
    }

    private fun showLocationRequiredMessage() {
        Snackbar.make(
                binding.root,
                com.example.android.politicalpreparedness.R.string.location_required_error,
                Snackbar.LENGTH_INDEFINITE
        ).setAction(android.R.string.ok) {
            checkDeviceLocationSettings()
        }.show()
    }

    /*
     *  Requests ACCESS_FINE_LOCATION and (on Android 10+ (Q) ACCESS_BACKGROUND_LOCATION.
     */
    @TargetApi(Build.VERSION_CODES.Q)
    private fun requestForegroundAndBackgroundLocationPermissions() {
        if (foregroundAndBackgroundLocationPermissionApproved())
            return
        var permissionsArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        val resultCode = when {
            runningQOrLater -> {
                permissionsArray += Manifest.permission.ACCESS_BACKGROUND_LOCATION
                REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE
            }
            else -> REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
        }
        Log.d(TAG, "Request foreground only location permission")
        requestPermissions(
                permissionsArray,
                resultCode
        )
    }

    private fun requiredPermissionsDenied(
            grantResults: IntArray,
            requestCode: Int
    ): Boolean {
        return permissionRequestCancelled(grantResults) ||
                grantResults[LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED ||
                (requestCode == REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE &&
                        grantResults[BACKGROUND_LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED)
    }

    private fun showPermissionDeniedExplanation() {
        Snackbar.make(
                binding.root,
                com.example.android.politicalpreparedness.R.string.permission_denied_explanation,
                Snackbar.LENGTH_INDEFINITE
        ).setAction(com.example.android.politicalpreparedness.R.string.settings) {
            onConfirmPermissionDeniedExplanation()
        }.show()
    }

    private fun onConfirmPermissionDeniedExplanation() {
        startActivity(Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

    private fun permissionRequestCancelled(grantResults: IntArray) = grantResults.isEmpty()

}