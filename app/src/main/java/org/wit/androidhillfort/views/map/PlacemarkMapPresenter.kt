package org.wit.androidhillfort.views.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.wit.androidhillfort.models.PlacemarkModel
import org.wit.androidhillfort.views.BasePresenter
import org.wit.androidhillfort.views.BaseView

class PlacemarkMapPresenter(view: BaseView) : BasePresenter(view) {

  fun doPopulateMap(map: GoogleMap, placemarks: List<PlacemarkModel>) {
    map.uiSettings.setZoomControlsEnabled(true)
    placemarks.forEach {
      val loc = LatLng(it.location.lat, it.location.lng)
      val options = MarkerOptions().title(it.title).position(loc)
      map.addMarker(options).tag = it
      map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.location.zoom))
    }
  }

  fun doMarkerSelected(marker: Marker) {
    val placemark = marker.tag as PlacemarkModel
    if (placemark != null) view?.showPlacemark(placemark)
  }

  fun loadPlacemarks() {
    async(UI) {
      view?.showPlacemarks(app.placemarks.findAll())
    }
  }
}