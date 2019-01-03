package org.wit.androidhillfort.views.placemark

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.abc_list_menu_item_checkbox.*
import kotlinx.android.synthetic.main.activity_placemark.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import org.wit.androidhillfort.R
import org.wit.androidhillfort.models.PlacemarkModel
import org.wit.androidhillfort.views.BaseView
import java.text.SimpleDateFormat
import java.util.*

class PlacemarkView : BaseView(), AnkoLogger {

  lateinit var presenter: PlacemarkPresenter
  var placemark = PlacemarkModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_placemark)

    init(toolbarAdd, true)

    presenter = initPresenter (PlacemarkPresenter(this)) as PlacemarkPresenter

    datevisited.isEnabled = false
    additonalnote.isEnabled = false

    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync {
      presenter.doConfigureMap(it)
      it.setOnMapClickListener { presenter.doSetLocation() }
    }

    checkBox.setOnCheckedChangeListener({
      buttonView, isChecked ->
      if (isChecked){
        datevisited.isEnabled = true
        additonalnote.isEnabled = true

        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = sdf.format(Date())
        datevisited.setText(currentDate)
      }else{
        datevisited.isEnabled = false
        additonalnote.isEnabled = false
        datevisited.setText("Visited Date")
      }
    })

    chooseImage.setOnClickListener { presenter.doSelectImage() }
  }

  override fun showPlacemark(placemark: PlacemarkModel) {
    placemarkTitle.setText(placemark.title)
    description.setText(placemark.description)
    datevisited.setText(placemark.vDate)
    additonalnote.setText(placemark.aNotes)
    if(placemarkTitle.text.toString() != ""){
      checkBox.isChecked = true
      datevisited.isEnabled = true
      additonalnote.isEnabled = true
    }

    Glide.with(this).load(placemark.hImage).into(placemarkImage);
    if (placemark.hImage != null) {
      chooseImage.setText(R.string.change_placemark_image)
    }
    lat.setText("%.6f".format(placemark.location.lat))
    lng.setText("%.6f".format(placemark.location.lng))
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_placemark, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.item_delete -> {
        presenter.doDelete()
      }
      R.id.item_save -> {
        if (placemarkTitle.text.toString().isEmpty()) {
          toast(R.string.enter_placemark_title)
        } else {
          presenter.doAddOrSave(placemarkTitle.text.toString(), description.text.toString(),additonalnote.text.toString(),datevisited.text.toString(),checkBox.isChecked)
        }
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (data != null) {
      presenter.doActivityResult(requestCode, resultCode, data)
    }
  }

  override fun onBackPressed() {
    presenter.doCancel()
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
    presenter.doResartLocationUpdates()
  }

  override fun onSaveInstanceState(outState: Bundle?) {
    super.onSaveInstanceState(outState)
    mapView.onSaveInstanceState(outState)
  }
}

