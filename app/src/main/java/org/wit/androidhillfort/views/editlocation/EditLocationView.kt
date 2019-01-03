package org.wit.androidhillfort.views.editlocation

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_edit_location.*
import org.wit.androidhillfort.R
import org.wit.androidhillfort.views.BaseView

class EditLocationView : BaseView(), GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener, OnMapClickListener {


  lateinit var presenter: EditLocationPresenter

  override fun onMapClick(p0: LatLng) {

    mapView.getMapAsync {
      presenter.doAddMarker(it,p0)
    }

    lat.setText(""+String.format("%.8f", p0.latitude).toDouble())
    lng.setText(""+String.format("%.8f", p0.longitude).toDouble())
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_edit_location)
    super.init(toolbar, true)

    presenter = initPresenter(EditLocationPresenter(this)) as EditLocationPresenter

    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync {
      it.setOnMarkerDragListener(this)
      it.setOnMarkerClickListener(this)
      it.setOnMapClickListener(this)
      presenter.doConfigureMap(it)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_edit_location, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.item_save -> {
        presenter.doSave()
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onMarkerDragStart(marker: Marker) {}

  override fun onMarkerDrag(marker: Marker) {
    lat.setText("%.6f".format(marker.position.latitude))
    lng.setText("%.6f".format(marker.position.longitude))
  }

  override fun onMarkerDragEnd(marker: Marker) {
    presenter.doUpdateLocation(marker.position.latitude, marker.position.longitude)
  }

  override fun onMarkerClick(marker: Marker): Boolean {
    presenter.doUpdateMarker(marker)
    return false
  }

  override fun onDestroy() {
    super.onDestroy()
    mapView.onDestroy()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mapView.onLowMemory()
  }

  override fun onPause() {
    super.onPause()
    mapView.onPause()
  }

  override fun onResume() {
    super.onResume()
    mapView.onResume()
  }

  override fun onSaveInstanceState(outState: Bundle?) {
    super.onSaveInstanceState(outState)
    mapView.onSaveInstanceState(outState)
  }
}