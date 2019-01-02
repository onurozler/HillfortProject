package org.wit.androidhillfort.views.placemark

import android.annotation.SuppressLint
import android.content.Intent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.toast
import org.wit.androidhillfort.helpers.checkLocationPermissions
import org.wit.androidhillfort.helpers.createDefaultLocationRequest
import org.wit.androidhillfort.helpers.isPermissionGranted
import org.wit.androidhillfort.helpers.showImagePicker
import org.wit.androidhillfort.models.Location
import org.wit.androidhillfort.models.PlacemarkModel
import org.wit.androidhillfort.views.*

class PlacemarkPresenter(view: BaseView) : BasePresenter(view) {

  var map: GoogleMap? = null
  var placemark = PlacemarkModel()
  var defaultLocation = Location(52.245696, -7.139102, 15f)
  var edit = false;
  var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
  val locationRequest = createDefaultLocationRequest()

  init {
    if (view.intent.hasExtra("placemark_edit")) {
      edit = true
      placemark = view.intent.extras.getParcelable<PlacemarkModel>("placemark_edit")
      view.showPlacemark(placemark)
    } else {
      if (checkLocationPermissions(view)) {
        doSetCurrentLocation()
      }
    }
  }

  @SuppressLint("MissingPermission")
  fun doSetCurrentLocation() {
    locationService.lastLocation.addOnSuccessListener {
      locationUpdate(Location(it.latitude, it.longitude))
    }
  }

  @SuppressLint("MissingPermission")
  fun doResartLocationUpdates() {

    var locationCallback = object : LocationCallback() {
      override fun onLocationResult(locationResult: LocationResult?) {
        if (locationResult != null && locationResult.locations != null) {
          val l = locationResult.locations.last()
          locationUpdate(Location(l.latitude, l.longitude))
        }
      }
    }
    if (!edit) {
      locationService.requestLocationUpdates(locationRequest, locationCallback, null)
    }
  }

  override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    if (isPermissionGranted(requestCode, grantResults)) {
      doSetCurrentLocation()
    } else {
      locationUpdate(defaultLocation)
    }
  }

  fun doConfigureMap(m: GoogleMap) {
    map = m
    locationUpdate(placemark.location)
  }

  fun locationUpdate(location: Location) {
    placemark.location = location
    placemark.location.zoom = 15f
    map?.clear()
    map?.uiSettings?.setZoomControlsEnabled(true)
    val options = MarkerOptions().title(placemark.title).position(LatLng(placemark.location.lat, placemark.location.lng))
    map?.addMarker(options)
    map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(placemark.location.lat, placemark.location.lng), placemark.location.zoom))
    view?.showPlacemark(placemark)
  }


  fun doAddOrSave(title: String, description: String) {
    placemark.title = title
    placemark.description = description

    async(UI) {
      if (edit) {
        app.placemarks.update(placemark)
        view?.toast("eklenddddi")
      } else {
        app.placemarks.create(placemark)
        view?.toast("eklendi")
      }
      view?.finish()
    }
  }

  fun doCancel() {
    view?.finish()
  }

  fun doDelete() {
    async(UI) {
      app.placemarks.delete(placemark)
      view?.finish()
    }
  }

  fun doSelectImage() {
    view?.let {
      showImagePicker(view!!, IMAGE_REQUEST)
    }
  }

  fun doSetLocation() {
    view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(placemark.location.lat, placemark.location.lng, placemark.location.zoom))
  }

  override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    when (requestCode) {
      IMAGE_REQUEST -> {
        placemark.hImage = data.data.toString()
        view?.showPlacemark(placemark)
      }
      LOCATION_REQUEST -> {
        val location = data.extras.getParcelable<Location>("location")
        placemark.location = location
        locationUpdate(location)
      }
    }
  }
}